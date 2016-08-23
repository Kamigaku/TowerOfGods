package com.kamigaku.towerofgods.entitiesModel;

import java.io.Serializable;
import java.util.ArrayList;

import com.badlogic.gdx.math.Rectangle;
import com.kamigaku.towerofgods.level.Level;
import com.kamigaku.towerofgods.spell.Spell;
import com.kamigaku.towerofgods.tools.Ticker;

public abstract class Entity implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = -2701516370137092212L;
	public int current_anim;
	public int collisionHeight;
	public int collisionWidth;
	public int diffCollisionHeight;
	public int diffCollisionWidth;
	protected float x;
	protected float y;
	public Stats stats;
	public String sprite;
	protected ArrayList<Spell> spell;
	protected Ticker statTicker;
	
	public void update(Level map) {};
	
	public boolean collision(float x, float y, Level map) {
		int posXleft = (int) (x / map.tileWidth);
		int posYbot = (int) (y / map.tileHeight);
		int posXright = (int) ((x + collisionWidth) / map.tileWidth);
		int posYtop = (int) ((y + collisionHeight) / map.tileHeight);

		if(map.collisionLayer.getCell(posXleft, posYbot).getTile().getProperties().containsKey("blocked")) {
			return true;
		}
		if(map.collisionLayer.getCell(posXleft, posYtop).getTile().getProperties().containsKey("blocked")) {
			return true;
		}
		if(map.collisionLayer.getCell(posXright, posYbot).getTile().getProperties().containsKey("blocked")) {
			return true;
		}
		if(map.collisionLayer.getCell(posXright, posYtop).getTile().getProperties().containsKey("blocked")) {
			return true;
		}
		if(map.collisionLayer.getCell(posXleft, posYbot).getTile().getProperties().containsKey("wall")) {
			return true;
		}
		if(map.collisionLayer.getCell(posXleft, posYtop).getTile().getProperties().containsKey("wall")) {
			return true;
		}
		if(map.collisionLayer.getCell(posXright, posYbot).getTile().getProperties().containsKey("wall")) {
			return true;
		}
		if(map.collisionLayer.getCell(posXright, posYtop).getTile().getProperties().containsKey("wall")) {
			return true;
		}
		
		
		return false;
	};
	
	protected boolean collisionInEntity(float x, float y, float xObject, float yObject, int collisionHeight, int collisionWidth) {
		Rectangle player = new Rectangle(x, y, this.collisionWidth, this.collisionHeight);
		Rectangle ennemy = new Rectangle(xObject, yObject, collisionWidth, collisionHeight);
		if(player.overlaps(ennemy) || ennemy.overlaps(player)) {
			return true;
		}
		return false;
	}
	
	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}
	
	public ArrayList<Spell> getSpell() {
		return spell;
	}
	
	public Spell getSpecificSpell(int number) {
		return this.spell.get(number);
	}
	
	public void removeElements(Spell s) {
		spell.remove(s);
	}
	
	public Stats getStats() {
		return this.stats;
	}

	public boolean isAlive() {
		if(stats.hp > 0)
			return true;
		return false;
	}
	
	protected void setAnim(float tempX, float tempY) {
	}	
}