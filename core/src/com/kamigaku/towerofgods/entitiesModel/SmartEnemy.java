package com.kamigaku.towerofgods.entitiesModel;

import java.awt.Point;
import java.util.ArrayList;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.kamigaku.towerofgods.level.Level;

public class SmartEnemy extends Enemy {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3586965095283661182L;
	private Vector2 chemin;

	public SmartEnemy(float x, float y, int collisionHeight, int collisionWidth) {
		super(x, y, collisionHeight, collisionWidth);
		chemin = null;
	}
	
	@Override
	public void update(Level map) {
		this.hasBeenUpdated = false;
		for(Player player : map.players) {
			float playerX = player.x;
			float playerY = player.y;
			int collisionWidth = player.collisionWidth;
			int collisionHeight = player.collisionHeight;
			this.tempX = 0;
			this.tempY = 0;
			Circle circle = new Circle(this.x + (this.collisionWidth / 2), this.y + (this.collisionHeight / 2), (float)this.stats.rangeDetection);
			if(circle.contains(playerX + (collisionWidth / 2), playerY + (collisionHeight / 2))) {
				int xPlayer = (int)playerX + (collisionWidth / 2) + player.diffCollisionWidth;
				int yPlayer = (int)playerY + (collisionHeight / 2) + player.diffCollisionHeight ;
				int xEnnemy = (int)this.x + (this.collisionWidth / 2) + diffCollisionWidth;
				int yEnnemy = (int)this.y + (this.collisionHeight / 2) + diffCollisionHeight;
				double norme = Math.sqrt((Math.pow((yPlayer - yEnnemy), 2) + Math.pow((xPlayer - xEnnemy), 2)));
				double newX = (xPlayer - xEnnemy) / norme;
				double newY = (yPlayer - yEnnemy) / norme;
				int result = checkNormeInWall(norme, newX, newY, map);
				if(result != 0) {
					if(result == 1)
						detect = true;
					if(!detect)
						return;
					getWay(map);
				}
				else {
						this.tempX = (float) newX;
						this.tempY = (float) newY;			
						this.detect = true;
				}	
				
				if(!collision(x, ((tempY * this.stats.speed) + y), map) && 
						!collisionInEntity(x + this.diffCollisionWidth, ((tempY * this.stats.speed) + y) + this.diffCollisionHeight, 
						player.x + player.diffCollisionWidth, player.y + player.diffCollisionHeight, player.collisionHeight, player.collisionWidth)) {
					setY((tempY * this.stats.speed) + y);
					this.hasBeenUpdated = true;
				}
				if(!collision(((tempX * this.stats.speed) + x), y, map) && 
						!collisionInEntity(((tempX * this.stats.speed) + x) + this.diffCollisionWidth, y + this.diffCollisionHeight,
						player.x + player.diffCollisionWidth, player.y + player.diffCollisionHeight, player.collisionHeight, player.collisionWidth)) {
					setX((tempX * this.stats.speed) + x);
					this.hasBeenUpdated = true;
				}
			}
			else {
				this.detect = false;
				regenHp();
			}
			setAnim(tempX, tempY);
		}
	}
	
	private void getWay(Level map) {
		ArrayList<Point> playersPostions = new ArrayList<Point>();
		for(Player p : map.players) {
			playersPostions.add(new Point((int) p.getX(), (int) p.getY()));
		}
		chemin = this.moveToDestination(map, new Point((int)this.x / map.tileWidth, (int)this.y / map.tileHeight), playersPostions);
		this.tempX = chemin.x;
		this.tempY = chemin.y;
//		chemin = Pathfinding.generatePath(map, new Point((int)this.x / map.tileWidth, (int)this.y / map.tileHeight), playersPostions);
//		int xEnnemy = (int)this.x;
//		int yEnnemy = (int)this.y;
//		try {
//		double norme = Math.sqrt( (Math.pow((yEnnemy - (chemin.y*map.tileHeight)), 2) 
//				+ Math.pow((xEnnemy - (chemin.x * map.tileWidth)), 2)));
//		this.tempX = (float)(((chemin.x * map.tileWidth) - xEnnemy) / norme);
//		this.tempY = (float)(((chemin.y * map.tileHeight) - yEnnemy) / norme);
//		} catch(NullPointerException e) {
//			System.out.println(chemin);
//		}
	}

}