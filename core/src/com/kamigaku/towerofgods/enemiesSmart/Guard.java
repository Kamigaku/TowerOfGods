package com.kamigaku.towerofgods.enemiesSmart;

import com.kamigaku.towerofgods.entitiesModel.SmartEnemy;
import com.kamigaku.towerofgods.entitiesModel.StatsEnemy;

public class Guard extends SmartEnemy {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7204155883680313616L;
	public static final int IDLE = 4;
	public static final int TOP = 2;
	public static final int LEFT = 3;
	public static final int RIGHT = 1;
	public static final int BOT = 0;
	
	public Guard(float x, float y, int collisionWidth, int collisionHeight, int rayonDetection) {
		super(x, y,  collisionHeight, collisionWidth);
		this.stats = new StatsEnemy(15, 100, 20, 0, 0, 0, 0, 1.3f, rayonDetection, 5);
		this.sprite = "data/guard.png";
		this.current_anim = 0;
	}
	
	public Guard(float x, float y, int collisionWidth, int collisionHeight, StatsEnemy stats) {
		super(x, y,  collisionHeight, collisionWidth);
		this.WIDTH = 22;
		this.HEIGHT = 26;
		this.NOMBRE_IMAGE = 10;
		this.NOMBRE_LIGNE = 5;
		this.DUREE_ANIM = 0.15f;
		this.diffCollisionHeight = (HEIGHT / 2) - (collisionHeight / 2);
		this.diffCollisionWidth = (WIDTH / 2) - (collisionWidth / 2);
		this.stats = stats;
		this.sprite = "data/guard.png";
		this.current_anim = 4;
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