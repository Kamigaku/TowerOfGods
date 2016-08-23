package com.kamigaku.towerofgods.spell;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.kamigaku.towerofgods.entitiesModel.Entity;
import com.kamigaku.towerofgods.level.Level;

public class SpellAOE extends Spell {
	
	private int radius;
	private boolean attackAlly = false;
	private boolean attackEnnemy = true;
	private float xCaster;
	private float yCaster;
	private float secondTick;
	public boolean firstTick = false;

	public SpellAOE(float xSpell, float ySpell, float xCaster, float yCaster, int manaCost, int radius) {
		super(xSpell, ySpell, 0, 0, manaCost);
		this.xCaster = xCaster;
		this.yCaster = yCaster;
		this.radius = radius;
		this.secondTick = 0;
	}
	
	public static boolean isInRange(float xPlayer, float yPlayer, Vector2 mouseCursor, int range) {
		float resultX = Math.abs(mouseCursor.x);
		float resultY = Math.abs(mouseCursor.y);
		if(resultX >= 0 && resultX <= range && resultY >= 0 && resultY <= range) 
			return true;
		return false;
	}
	
	@Override
	public boolean checkCollisionToEntity(Level map) {
		Circle aoe = new Circle(new Vector2(this.x, this.y), this.radius);
		if(!attackEnnemy) {
			for(Entity e : map.enemies) {
				Rectangle entity = new Rectangle(e.getX(), e.getY(), e.collisionWidth, e.collisionWidth);
				if(Intersector.overlaps(aoe, entity)) {
					e.stats.hp -= this.damage;
				}
			}
		}
		if(!attackAlly) {
			for(Entity e : map.players) {
				Rectangle entity = new Rectangle(e.getX(), e.getY(), e.collisionWidth, e.collisionWidth);
				if(Intersector.overlaps(aoe, entity)) {
					e.stats.hp -= this.damage;
				}
			}	
		}
		return false;
	}
	
	public boolean launchToWall(Level map) {
		double norme = Math.sqrt((Math.pow((this.yCaster - this.y), 2) + Math.pow((this.xCaster - this.x), 2)));
		double xNorme = (this.xCaster - this.x) / norme;
		double yNorme = (this.yCaster - this.y) / norme;
		for(int i = 0; i < norme; i++) {
			if(map.collisionLayer.getCell(
					(int)(this.x + (xNorme * i)) / map.tileWidth,
					(int)(this.y + (yNorme * i)) / map.tileHeight).getTile().getProperties().containsKey("wall")) {
				return true;
			}
		}
		return false;
	}
	
	public boolean hasTicked() {
		this.secondTick += Gdx.graphics.getDeltaTime();
		if(this.secondTick >= 1.0f) {
			this.secondTick = 0f;
			return true;
		}
		return false;
	}

}