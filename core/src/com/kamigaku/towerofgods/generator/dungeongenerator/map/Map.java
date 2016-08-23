package com.kamigaku.towerofgods.generator.dungeongenerator.map;

import java.awt.Point;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import com.kamigaku.towerofgods.entitiesModel.StatsEnemy;
import com.kamigaku.towerofgods.generator.dungeongenerator.entity.Enemy;
import com.kamigaku.towerofgods.generator.dungeongenerator.entity.Entity;
import com.kamigaku.towerofgods.generator.dungeongenerator.entity.NamedPoint;
import com.kamigaku.towerofgods.generator.dungeongenerator.entity.entities.Corridor;
import com.kamigaku.towerofgods.generator.dungeongenerator.entity.entities.Door;
import com.kamigaku.towerofgods.generator.dungeongenerator.entity.entities.RoomRectangular;
import com.kamigaku.towerofgods.level.Level;

public class Map implements Serializable {
	
	//TODO
	// - Faire en sorte que plus la map est grande plus les salles sont grandes
	// Bugs : 
	// - None
	
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7679959289849498636L;
	private String[][] map;
	private ArrayList<Entity> entities;
	private Entity mainRoom; // A voir plus tard
	private ArrayList<Door> doors;
	private int sizeX, sizeY;
	private Point startingPoint;
	
	public Map(int sizeX, int sizeY) {
		this.map = new String[sizeY][sizeX];
		this.entities = new ArrayList<Entity>();
		this.doors = new ArrayList<Door>();
		this.sizeX = sizeX;
		this.sizeY = sizeY;
	}
	
	public void generateMap() {
		fillEmptyDungeon();
		generateDungeon();
		changeWall();
	}
	

	private void addDoor(RoomRectangular room) {
		int xDeb = room.getBegin().x;
		int xFin = room.getEnd().x - 1;
		int yDeb = room.getBegin().y;
		int yFin = room.getEnd().y - 1;
		int numberDoor = (int)((Math.random() * 2) + 1);
		for(int i = 0; i < numberDoor; i++) {
			int xDoor = 0;
			int yDoor = 0;
			do {
				xDoor = (int) (Math.random() * 2);
				yDoor = (int) (Math.random() * 2);
				if(xDoor == 0 && yDoor == 0)  { // gauche
					xDoor = xDeb;
					yDoor = (int) ((Math.random() * (yFin - yDeb - 1)) + yDeb + 1);
				}
				else if(xDoor == 0 && yDoor == 1) { // bas
					yDoor = yFin;
					xDoor = (int) ((Math.random() * (xFin - xDeb - 1)) + xDeb + 1);
				}
				else if(xDoor == 1 && yDoor == 0 ) { // droite
					xDoor = xFin;
					yDoor = (int) ((Math.random() * (yFin - yDeb - 1)) + yDeb + 1);
				}
				else { // top
					yDoor = yDeb;
					xDoor = (int) ((Math.random() * (xFin - xDeb - 1)) + xDeb + 1);
				}
			} while(this.map[yDoor][xDoor] == "D" || this.map[yDoor + 1][xDoor] == "D"
					|| this.map[yDoor][xDoor - 1] == "D" || this.map[yDoor][xDoor + 1] == "D"
					|| this.map[yDoor - 1][xDoor] == "D");
			this.map[yDoor][xDoor] = "D";
			this.doors.add(new Door(xDoor, yDoor, room));
		}
	}

	private void addRoom(RoomRectangular room) {
		for(int y = 0; y < (room.getEnd().y - room.getBegin().y); y++) {
			for(int x = 0; x < (room.getEnd().x - room.getBegin().x); x++) {
				if(x == 0 || y == 0 || x == (room.getEnd().x - room.getBegin().x - 1) || y == (room.getEnd().y - room.getBegin().y - 1))
					this.map[y + room.getBegin().y][x + room.getBegin().x] = "#";
				else
					this.map[y + room.getBegin().y][x + room.getBegin().x] = " ";
			}				
		}	
		this.entities.add(room);
	}

	private void addObjectsInRooms(ArrayList<Entity> rooms) {
		for(Entity room : rooms) {
			if(room.getClass() == RoomRectangular.class)
				((RoomRectangular)room).generateObject();
		}
	}

	private boolean canWrite(int y, int x) {
		if(this.map[y][x] != "D" && this.map[y][x] != "#" && this.map[y][x] != " ")
			return true;
		return false;
	}

	private void changeWall() {
		char left, right, top, bot;
		ArrayList<NamedPoint> np = new ArrayList<NamedPoint>();
		for(int y = 0; y < this.map.length; y++) {
			for(int x = 0; x < this.map[0].length; x++) {
				if(y == this.map.length - 2) {
					if(this.map[y][x] == "#" && this.map[y-1][x] == " ") 
						this.map[y][x] = " ";
				}
				if(y == 1) {
					if(this.map[y][x] == "#" && this.map[y+1][x] == " ") 
						this.map[y][x] = " ";
				}
				if(x == 1) {
					if(this.map[y][x] == "#" && this.map[y][x+1] == " ") 
						this.map[y][x] = " ";
				}
				if(x == this.map[y].length - 2) {
					if(this.map[y][x] == "#" && this.map[y][x-1] == " ") 
						this.map[y][x] = " ";
				}
			}
		}
		for(int y = 0; y < this.map.length; y++) {
			for(int x = 0; x < this.map[y].length; x++) {
				if(this.map[y][x] == "#") {
					left = right = top = bot = '0';
					if(x > 0 && this.map[y][x - 1] == "#")
						left = '1';
					if(x < (this.map[y].length - 1) && this.map[y][x + 1] == "#")
						right = '1';
					if(y < (this.map.length - 1) && this.map[y + 1][x] == "#")
						bot = '1';
					if(y > 0 && this.map[y - 1][x] == "#")
						top = '1';
					np.add(new NamedPoint(x, y, "" + top + bot + left + right));
				}	
			}
		}
		drawDungeon();
		for(NamedPoint n : np) {
			this.map[n.y][n.x] = n.getLetter();
		}
	}
	
	private boolean checkPointInLimit(Point p) {
		if((p.x < (this.sizeX - 1) && p.x > 1) && (p.y < (this.sizeY - 1) && p.y > 1))
			return true;
		return false;
	}

	private boolean checkNoDoor() {
		boolean good = true;
		for(Entity e : this.entities) {
			if(e.getNumberOfDoor() == 0) {
				addDoor((RoomRectangular) e);
				good = false;
			}
		}
		return good;
	}

	private void createRoad() {
		ArrayList<ArrayList<Point>> chemins = new ArrayList<ArrayList<Point>>();
		for(Door d : this.doors) {
			Door closest = getClosestDoor(d);
			chemins.add(getRoad(d, closest));
		}
		drawRoad(chemins);
	}

	private void cleanDoor() {
		for(int y = 0; y < this.sizeY; y++) {
			for(int x = 0; x < this.sizeX; x++) {
				if(this.map[y][x] == "D") {
					int numberTag = 0;
					if(this.map[y + 1][x] == "#") numberTag++;
					if(this.map[y][x + 1] == "#") numberTag++;
					if(this.map[y - 1][x] == "#") numberTag++;
					if(this.map[y][x - 1] == "#") numberTag++;
					if(numberTag >= 3) {
						removeDoor(x, y);
						this.map[y][x] = "#";
					}
				}
			}
		}
	}

	private void cleanDungeon() {
		for(int y = 1; y < this.map.length - 3; y++) {
			for(int x = 1; x < this.map[0].length - 3; x++) {
				if(this.map[y][x] == " ") {
					if(this.map[y][x+1] == "#" && this.map[y][x+2] == "#" && this.map[y][x+3] == " ") {
						if(notARoom(x+1, y))
							this.map[y][x+1] = " ";
						if(notARoom(x+2, y)) 
							this.map[y][x+2] = " ";
					}
					if(this.map[y+1][x] == "#" && this.map[y+2][x] == "#" && this.map[y+3][x] == " ") {
						if(notARoom(x, y+1))
							this.map[y+1][x] = " ";
						if(notARoom(x, y+2))
							this.map[y+2][x] = " ";
					}
				}
			}
		}
		for(int y = 1; y < this.map.length - 1; y++) {
			for(int x = this.map[0].length - 2; x > 0; x--) {
				subClean(y, x);
			}
		}
		for(int y = 1; y < this.map.length - 1; y++) {
			for(int x = 1; x < this.map[0].length - 1; x++) {
				subClean(y, x);
			}
		}
	}

	public void drawDungeon() {
		System.out.println("Nombre de salles : " + this.entities.size());
		for(int y = 0; y < this.sizeY; y++) {
			if(y < 10)
				System.out.print(y + "   -    ");
			else if (y < 100)
				System.out.print(y + "  -    ");
			else
				System.out.print(y + " -    ");
			for(int x = 0; x < this.sizeX; x++) {
				System.out.print(this.map[y][x]);
			}
			System.out.println();
		}		
	}

	private void drawRoad(ArrayList<ArrayList<Point>> chemins) {
		for(ArrayList<Point> chemin : chemins) {
			int i = 0;
			for(Point p : chemin) {
				if(i > 0) {
					this.map[p.y][p.x] = " ";
					if(canWrite(p.y + 1, p.x))
						this.map[p.y + 1][p.x] = "#";
					if(canWrite(p.y - 1, p.x))
						this.map[p.y - 1][p.x] = "#";
					if(canWrite(p.y, p.x + 1))
						this.map[p.y][p.x + 1] = "#";
					if(canWrite(p.y, p.x - 1))
						this.map[p.y][p.x - 1] = "#";
				}
				i++;
			}
		}
	}

	private void fillBorder() {
		for(int y = 0; y < this.sizeY; y++) {
			for(int x = 0; x < this.sizeX; x++) {
				if(x == 0 || y == 0 || x == (this.sizeX - 1) || y == (this.sizeY - 1))
					this.map[y][x] = "#";
			}
		}
	}

	private void fillEmptyDungeon() {
		for(int y = 0; y < this.sizeY; y++) {
			for(int x = 0; x < this.sizeX; x++) {
				if(x == 0 || y == 0 || x == (this.sizeX - 1) || y == (this.sizeY - 1))
					this.map[y][x] = "#";
				else
					this.map[y][x] = "/";
			}
		}
	}

	private void generateDungeon() {
		int numberOfRooms = (int) ((Math.random() * ((this.sizeX / 5) - 5) + 5));
		for(int i = 0; i < numberOfRooms; i++) {
			// a prevoir plus tard, un switch pour d'autres types de room mais pas pour le moment
			RoomRectangular currentRoom;
			int numberOfTry = 0;
			boolean create = true;
			do {
				int positionX = (int) ((Math.random() * (this.sizeX - 4)) - 2);
				int positionY = (int) ((Math.random() * (this.sizeX - 4)) - 2);
				int widthRoom = (int) ((Math.random() * (RoomRectangular.maxWidthRoom - RoomRectangular.minWidthRoom)) + RoomRectangular.minWidthRoom);
				int heightRoom = (int) ((Math.random() * (RoomRectangular.maxHeightRoom - RoomRectangular.minHeightRoom)) + RoomRectangular.minHeightRoom);
				currentRoom = new RoomRectangular(new Point(positionX, positionY), 
						new Point(positionX + widthRoom, positionY + heightRoom));
				numberOfTry++;
				if(numberOfTry == 100)
					create = false;
			} while(!spaceEmpty(currentRoom) && numberOfTry < 100);
			if(create) {
				addRoom(currentRoom);
				addDoor(currentRoom);
			}
		}
		do {
			cleanDoor();
		} while(!checkNoDoor());
		addObjectsInRooms(this.entities);
		setMainRoom();
		createRoad();
		cleanDungeon();
		fillBorder();
	}
	
	private boolean notARoom(int x, int y) {
		for(Entity e : this.entities) {
			if(e.getClass() == RoomRectangular.class) {
				if(((RoomRectangular)e).getEnd().x > x && e.getBegin().x <= x &&
						((RoomRectangular)e).getEnd().y >= y && e.getBegin().y <= y)
					return false;
			}
		}
		return true;
	}

	private void removeDoor(int x, int y) {
		ArrayList<Door> tempTab = new ArrayList<Door>();
		for(Door d : this.doors) {
			if(d.equals(new Point(x, y)))
				d.removeDoor();	
			else
				tempTab.add(d);
		}
		this.doors = tempTab;
	}

	private void subClean(int y, int x) {
		if(notARoom(x, y)) {
			if(this.map[y][x] == "/") {
				int numberOfDiez = 0;
				if(this.map[y+1][x] == "#")
					numberOfDiez = numberOfDiez + 1;
				if(this.map[y-1][x] == "#")
					numberOfDiez = numberOfDiez + 1;
				if(this.map[y][x+1] == "#")
					numberOfDiez = numberOfDiez + 1;
				if(this.map[y][x-1] == "#")
					numberOfDiez = numberOfDiez + 1;
				if(numberOfDiez > 3) {
					this.map[y][x] = " ";
					if(notARoom(x, y+1) && y+1 < this.map.length - 1)
						this.map[y+1][x] = " ";
					if(notARoom(x, y-1) && y > 0)
						this.map[y-1][x] = " ";
					if(notARoom(x+1, y) && x+1 < this.map[0].length - 1)
						this.map[y][x+1] = " ";
					if(notARoom(x-1, y) && x > 0)
						this.map[y][x-1] = " ";
					this.map[y][x] = " ";
				}
				if(this.map[y+1][x+1] == " " || this.map[y-1][x+1] == " " ||
						this.map[y+1][x-1] == " " || this.map[y-1][x-1] == " " ) {
					this.map[y][x] = "#";
				}
			}
			if(this.map[y][x] == "#") {
				int numberSpace = 0;
				if(this.map[y+1][x] == " " || y+1 == this.map.length - 1)
					numberSpace = numberSpace + 1;
				if(this.map[y-1][x] == " " || y-1 == 0)
					numberSpace = numberSpace + 1;
				if(this.map[y][x+1] == " " || x+1 == this.map[0].length - 1)
					numberSpace = numberSpace + 1;
				if(this.map[y][x-1] == " "  || x-1 == 0)
					numberSpace = numberSpace + 1;
				if(numberSpace >= 3)
					this.map[y][x] = " ";
			}
		}
	}

	private void setMainRoom() {
		int mainRoom = (int)((Math.random() * (this.entities.size() - 2)) + 1);
		this.mainRoom = this.entities.get(mainRoom);
		this.map[this.mainRoom.getBegin().y + 1][this.mainRoom.getBegin().x + 1] = "S";
		this.startingPoint = new Point(this.mainRoom.getBegin().x + 1, this.mainRoom.getBegin().y + 1);
	}

	public void spawnEnnemies(ArrayList<Enemy> enemies, ArrayList<com.kamigaku.towerofgods.entitiesModel.Enemy> enemy, Level dungeon, ArrayList<String> nameEnemies, ArrayList<StatsEnemy> statsEnemies) throws NoSuchMethodException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		for(int i = 0; i < this.entities.size(); i++) {
			if(this.entities.get(i).getClass() == RoomRectangular.class && this.entities.get(i) != this.mainRoom) {
				int ennemy = (int) (Math.random() * enemies.size());
				Enemy e = enemies.get(ennemy);
				StatsEnemy stats = null;
				for(int j = 0; j < nameEnemies.size(); j++) {
					if(nameEnemies.get(j).equals(e.getNom())) {
						stats = statsEnemies.get(j);
					}
				}
				this.map[this.entities.get(i).getBegin().y + 1][this.entities.get(i).getBegin().x + 1] = Character.toString(e.getLetter());
				Constructor<?> constructor;
				//try {
					constructor = e.getClassCall().getConstructor(new Class[] {Float.TYPE, Float.TYPE, Integer.TYPE, Integer.TYPE, StatsEnemy.class});
					enemy.add((com.kamigaku.towerofgods.entitiesModel.Enemy) constructor.newInstance(
							this.entities.get(i).getBegin().x * 96 + 96, 
							((this.sizeY - 1) - this.entities.get(i).getBegin().y) * 96 - 96, 20, 20, stats));
					constructor = null;
				/*} catch (NoSuchMethodException | SecurityException | IllegalArgumentException | InstantiationException |
						IllegalAccessException | InvocationTargetException e1) {
					e1.printStackTrace();
				}*/
				//Plus la salle sera grande, plus il y aura de spawn
			}
		}
	}

	// Only for rectangular room
	private boolean spaceEmpty(RoomRectangular room) {
		for(int y = room.getBegin().y; y <= room.getEnd().y; y++)  {
			for(int x = room.getBegin().x; x <= room.getEnd().x; x++)  {
				if(!checkPointInLimit(new Point(x, y)))
					return false;
				if(this.map[y][x] == "/")
					continue;
				else
					return false;
			}	
		}
		return true;
	}

	public String[][] getMap() {
		return this.map;
	}
	
	private ArrayList<Point> getRoad(Door d, Door closest) {
		return Corridor.generateRoad(this.map, d.getPosition(), closest.getPosition());
	}

	public Point getStartPoint() {
		return this.startingPoint;
	}

	//TODO refaire la fonction
	private Door getClosestDoor(Door currentDoor) {
		int xLower = this.sizeX;
		int yLower = this.sizeY;
		Door closestDoor = null;
		for(Door doorToFind : this.doors) {
			int currentX = 0;
			int currentY = 0;
			if(!doorToFind.sameRoom(currentDoor, doorToFind)) {
				currentX = Math.abs(currentDoor.getPosition().x - doorToFind.getPosition().x);
				currentY = Math.abs(currentDoor.getPosition().y - doorToFind.getPosition().y);
				if(currentX < xLower && currentY < yLower)
					closestDoor = doorToFind;
			}
		}
		return closestDoor;
	}
	
	public ArrayList<Entity> getEntities() {
		return this.entities;
	}

	public void setMap(String[][] map) {
		this.map = map;
	}

}
