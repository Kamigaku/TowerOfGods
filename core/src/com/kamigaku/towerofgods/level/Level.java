package com.kamigaku.towerofgods.level;

import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;

import box2dLight.Light;
import box2dLight.RayHandler;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.kamigaku.towerofgods.entitiesModel.Enemy;
import com.kamigaku.towerofgods.entitiesModel.Player;
import com.kamigaku.towerofgods.entitiesModel.Sprite;
import com.kamigaku.towerofgods.generator.dungeongenerator.map.Map;
import com.kamigaku.towerofgods.hud.HUD;

public class Level implements ApplicationListener, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	// Player datas
	public ArrayList<Player> players;
	protected ArrayList<Sprite> playersSprite;
	
	// Enemy datas
	public ArrayList<Enemy> enemies;
	protected ArrayList<Sprite> enemiesSprite;
	
	public SpriteBatch batch;
    public TiledMap map;
	public TiledMapTileLayer collisionLayer;
    public OrthogonalTiledMapRenderer renderer;
    public Light light;
    public HUD hud;
    public int mapWidth;
    public int mapHeight;
    public int tileHeight;
    public int tileWidth;
    public Map mapGenerate;
	public RayHandler handler;
	protected Point startingPoint;
		
	protected OrthographicCamera camera;
	
	
	@Override
	public void create() {
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void render() {
	}
	
	public void multiplayerRender() {
		try {
			throw new Exception("Multiplayer render non implementé dans la classe appellante.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
	}
	
	public void mouvementCamera() { 
		if(this.players.get(0).getX() >= (Gdx.graphics.getWidth() * camera.zoom) / 2 && 
				this.players.get(0).getX() <= (this.mapWidth * this.tileWidth) - ((Gdx.graphics.getWidth() * camera.zoom) / 2)) {
			camera.position.x = this.players.get(0).getX();
		}
		if(this.players.get(0).getY() >= (Gdx.graphics.getHeight() * camera.zoom) / 2 && 
				this.players.get(0).getY() <= (this.mapHeight * this.tileHeight) - ((Gdx.graphics.getHeight() * camera.zoom) / 2)) {
			camera.position.y = this.players.get(0).getY();
		}
	}

	public void updateEnnemy() {
	}

	public void addPlayer(Player p, boolean isServer) {
    	this.players.add(p);
    	if(!isServer)
    		this.playersSprite.add(new Sprite(p.sprite, p.WIDTH, p.HEIGHT, p.NOMBRE_IMAGE, p.NOMBRE_LIGNE, p.DUREE_ANIM));
	}

	public void addNewPlayers(String[] datas) {
		boolean find = false;
		for(Player player : this.players) {
			if(player.getGeneratedNumber() == Integer.parseUnsignedInt(datas[3])) {
				player.setX(Float.parseFloat(datas[1]));
				player.setY(Float.parseFloat(datas[2]));
				find = true;
			}
		}
		if(!find) {
			this.players.add(new Player(Float.parseFloat(datas[1]), Float.parseFloat(datas[2]), Integer.parseUnsignedInt(datas[3])));
			Player p = this.players.get(this.players.size() - 1);
			this.playersSprite.add(new Sprite(p.sprite, p.WIDTH, p.HEIGHT, p.NOMBRE_IMAGE, p.NOMBRE_LIGNE, p.DUREE_ANIM));
		}
	}

	public void removePlayer(String[] datasTo) {
		int number = -1;
		for(int i = 0; i < this.players.size(); i++) {
			if(this.players.get(i).getGeneratedNumber() == Integer.parseInt(datasTo[1])) {
				number = i;
			}
		}
		if(number > -1) {
			this.players.remove(number);
			this.playersSprite.remove(number);
		}
	}
	
	public void setEnemies(ArrayList<Enemy> tempEnemy) {
		this.enemies = tempEnemy;
	    for(Enemy e : this.enemies) {
	    	this.enemiesSprite.add(new Sprite(e.sprite, e.WIDTH, e.HEIGHT, e.NOMBRE_IMAGE, e.NOMBRE_LIGNE, e.DUREE_ANIM));
	    }
	}

	public void setPlayers(ArrayList<Player> players) {
		this.players.addAll(players);
	}

	public ArrayList<Enemy> getEnemy() {
		return this.enemies;
	}
	
	public Point getStartingPoint() {
		return this.startingPoint;
	}
	    
    public float getAngle(Point depart, Point target) {
    	return (float) Math.toDegrees(Math.atan2(target.y - depart.y, target.x - depart.x));
    }

	public OrthographicCamera getCamera() {
		return this.camera;
	}
}