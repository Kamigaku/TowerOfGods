package com.kamigaku.towerofgods.entitiesModel;

import java.awt.Point;
import java.util.ArrayList;

import box2dLight.Light;

import com.badlogic.gdx.math.Vector2;
import com.kamigaku.towerofgods.ai.Pathfinding;
import com.kamigaku.towerofgods.level.Level;

public class Enemy extends Entity {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2692485904505313607L;
	
	//Animations & sprite datas
	public int WIDTH = 0;
	public int HEIGHT = 0;
	public int NOMBRE_IMAGE = 0;
	public int NOMBRE_LIGNE = 0;
	public float DUREE_ANIM = 0;
	
	private float xOrigin, yOrigin;
	
	protected int rayonDetection;
	protected float tempX;
	protected float tempY;
	protected boolean detect;
	
	public boolean hasBeenUpdated = false;

	public Enemy(float x, float y, int collisionHeight, int collisionWidth) {
		this.x = this.xOrigin = x;
		this.y = this.yOrigin = y;
		this.collisionWidth = collisionWidth;
		this.collisionHeight = collisionHeight;
		this.detect = false;
	}
	
	@Override
	public void update(Level map) {
		
	}

	protected int checkNormeInWall(double norme, double newX, double newY, Level map) {
		for(int i = 1; i <= norme; i++) {
			if(map.collisionLayer.getCell(
					(int)(this.x + (newX * i)) / map.tileWidth,
					(int)(this.y + (newY * i)) / map.tileHeight).getTile().getProperties().containsKey("blocked"))
				return 1;
			
			if(map.collisionLayer.getCell(
					(int)(this.x + (newX * i)) / map.tileWidth,
					(int)(this.y + (newY * i)) / map.tileHeight).getTile().getProperties().containsKey("wall")) {
				return 2;
			}
		}
		return 0;
	}
	
	protected void regenHp() {
		if(this.stats.hp < this.stats.maxHp) {
			this.stats.hp += this.stats.regenHp;
			if(this.stats.hp > this.stats.maxHp)
				this.stats.hp = this.stats.maxHp;
		}
	}

	public boolean isInLight(Light light) {
		if(light.contains(this.x + (this.collisionWidth / 2), this.y + (this.collisionHeight / 2)))
			return true;
		return false;
	}
	
	public void returnToPosition() {
		if(!this.detect && this.x != this.xOrigin && this.y != this.yOrigin) {
			
		}
	}
	
	public Vector2 moveToDestination(Level map, Point source, ArrayList<Point> destination) {
		Point chemin = Pathfinding.generatePath(map, source, destination);
		int xEnnemy = source.x;
		int yEnnemy = source.y;
		System.out.println("Ennemi : " + source.x + " " + source.y);
		double norme = Math.sqrt( (Math.pow((yEnnemy - (chemin.y*map.tileHeight)), 2) 
				+ Math.pow((xEnnemy - (chemin.x * map.tileWidth)), 2)));
		System.out.println("Norme : " + norme);
		System.out.println("Chemin : " + chemin.x + " " + chemin.y);
		float pointX = (float) (((chemin.x * map.tileWidth) - xEnnemy) / norme);
		float pointY = (float) (((chemin.y * map.tileHeight) - yEnnemy) / norme);
		return new Vector2(pointX, pointY); 
	}
}