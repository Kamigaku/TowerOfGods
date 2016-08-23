package com.kamigaku.towerofgods.spellrange;

import com.badlogic.gdx.math.Vector2;
import com.kamigaku.towerofgods.GameLauncher;
import com.kamigaku.towerofgods.entitiesModel.Sprite;
import com.kamigaku.towerofgods.level.Level;
import com.kamigaku.towerofgods.spell.SpellRange;

public class FireBall extends SpellRange {
	
	public static final float cd = 2f;
	public static final int TOP = 3;
	public static final int LEFT = 0;
	public static final int RIGHT = 1;
	public static final int BOT = 2;
	public final static int NOMBRE_IMAGE = 5;
	public final static int NOMBRE_LIGNE = 4;
	public final static float DUREE_ANIM = 0.15f;
	
	public static final int MANACOST = 0;
	
	public FireBall(float x, float y, int collisionHeight, int collisionWidth, int rangeMax, float vitesseProjectile, Vector2 mouseCursor) {
		super(x, y, collisionHeight, collisionWidth, rangeMax, vitesseProjectile, MANACOST);
		this.width = 26;
		this.height = 26;
		this.damage = 3;
		calculateDirection(mouseCursor);
		if(!GameLauncher.online) {
			this.sprite = new Sprite("data/fireball.png", this.width, this.height, NOMBRE_IMAGE, NOMBRE_LIGNE, DUREE_ANIM);
			double angle45 = Math.sqrt(2)/2;
			if(vectorX > angle45 && vectorY < angle45 && vectorY > -angle45) {
				this.currentAnim = RIGHT;
			}
			else if(vectorY > angle45 && vectorX < angle45 && vectorX > -angle45) {
				this.currentAnim = TOP;
			}
			else if(vectorX < -angle45 && vectorY < angle45 && vectorY > -angle45) {
				this.currentAnim = LEFT;
			}
	 		else 
				this.currentAnim = BOT;
		}
	}
	
	@Override
	public void update(Level map) {
		if(	this.x >= initialX && 
			this.x <= (initialX + (rangeMax * this.vectorX)) && 
			this.y >= initialY &&
			this.y <= (initialY + (rangeMax * this.vectorY))
		) {
			this.x += this.vitesseProjectile * this.vectorX;
			this.y += this.vitesseProjectile * this.vectorY;
		}
		else if(this.x <= initialX && 
				this.x >= (initialX + (rangeMax * this.vectorX)) && 
				this.y >= initialY &&
				this.y <= (initialY + (rangeMax * this.vectorY))
		) {
			this.x += this.vitesseProjectile * this.vectorX;
			this.y += this.vitesseProjectile * this.vectorY;			
		}
		else if(this.x <= initialX && 
				this.x >= (initialX + (rangeMax * this.vectorX)) && 
				this.y <= initialY &&
				this.y >= (initialY + (rangeMax * this.vectorY))
		) {
			this.x += this.vitesseProjectile * this.vectorX;
			this.y += this.vitesseProjectile * this.vectorY;		
		}
		else if(this.x >= initialX && 
				this.x <= (initialX + (rangeMax * this.vectorX)) && 
				this.y <= initialY &&
				this.y >= (initialY + (rangeMax * this.vectorY))
		) {
			this.x += this.vitesseProjectile * this.vectorX;
			this.y += this.vitesseProjectile * this.vectorY;		
		}
		else {
			alive = false;
		}
		if(checkCollisionToEntity(map)) {
			alive = false;
		}
	}
}