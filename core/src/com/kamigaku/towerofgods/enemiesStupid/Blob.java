package com.kamigaku.towerofgods.enemiesStupid;

import com.kamigaku.towerofgods.entitiesModel.StatsEnemy;
import com.kamigaku.towerofgods.entitiesModel.StupidEnemy;

public class Blob extends StupidEnemy {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5964660323740751133L;
	public static final int IDLE = 2;
	public static final int TOP = 0;
	public static final int LEFT = 3;
	public static final int RIGHT = 1;
	public static final int BOT = 2;

	public Blob(float x, float y, int collisionWidth, int collisionHeight, int rayonDetection) {
		super(x, y,  collisionHeight, collisionWidth);
		this.WIDTH = 22;
		this.HEIGHT = 22;
		this.NOMBRE_IMAGE = 6;
		this.NOMBRE_LIGNE = 4;
		this.DUREE_ANIM = 0.15f;
		this.diffCollisionHeight = (HEIGHT / 2) - collisionHeight / 2;
		this.diffCollisionWidth = (WIDTH / 2) - collisionWidth / 2;
		this.stats = new StatsEnemy(5, 100, 5, 0, 0, 0, 0, 1, rayonDetection, 1);
		this.sprite = "data/blob.png";
		this.current_anim = 0;
	}
	
	public Blob(float x, float y, int collisionWidth, int collisionHeight, StatsEnemy stats) {
		super(x, y, collisionHeight, collisionWidth);
		this.WIDTH = 22;
		this.HEIGHT = 22;
		this.NOMBRE_IMAGE = 6;
		this.NOMBRE_LIGNE = 4;
		this.DUREE_ANIM = 0.15f;
		this.diffCollisionHeight = (HEIGHT / 2) - collisionHeight / 2;
		this.diffCollisionWidth = (WIDTH / 2) - collisionWidth / 2;
		this.stats = stats;
		this.sprite = "data/blob.png";
		this.current_anim = 0;
	}
	
	@Override
	protected void setAnim(float tempX, float tempY) {
		if(tempX == 0 && tempY == 0)
			this.current_anim = IDLE;
		else if(tempX == 0 && tempY > 0) 
			this.current_anim = TOP;
		else if(tempX == 0 && tempY < 0) 
			this.current_anim = BOT;
		else if(tempX < 0) 
			this.current_anim = LEFT;
		else if(tempX > 0) 
			this.current_anim = RIGHT;
	}
}