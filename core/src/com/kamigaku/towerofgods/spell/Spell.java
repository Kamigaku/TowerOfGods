package com.kamigaku.towerofgods.spell;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.kamigaku.towerofgods.entitiesModel.Entity;
import com.kamigaku.towerofgods.entitiesModel.Sprite;
import com.kamigaku.towerofgods.level.Level;

public abstract class Spell {

	private Animation animation;
	protected float x;
	protected float y;
	protected float timeWhenCast;
	private float timeCast;
	protected int collisionHeight;
	protected int collisionWidth;
	protected int damage;
	protected int manaCost;
	public int currentAnim;
	protected boolean detection;
	protected boolean alive;
	protected Sprite sprite;
	protected Texture texture;
	protected int width, height;
	
	public Spell(float x, float y, int collisionHeight, int collisionWidth, int manaCost) {
		this.x = x;
		this.y = y;
		this.collisionHeight = collisionHeight;
		this.collisionWidth = collisionWidth;
		this.timeCast = 0;
		this.alive = true;
		this.manaCost = manaCost;
	}

	public void update(Level map) {}
	
	public boolean checkCollisionToEntity(Level map) {
		if(this.x <= 0 || this.y <= 0)
			return false;
		Rectangle spell = new Rectangle(this.x, this.y, this.collisionWidth, this.collisionHeight);
		for(Entity e : map.enemies) {
			Rectangle entity = new Rectangle(e.getX(), e.getY(), e.collisionWidth, e.collisionWidth);
			if(spell.overlaps(entity) || entity.overlaps(spell)) {
				e.stats.hp-= damage;
				return true;
			}
		}
		if(collision(x, y, map))
			return true;
		return false;
	}

	private boolean collision(float x, float y, Level map) {
		int posXleft = (int) ((x - (collisionWidth / 2)) / map.tileWidth);
		int posYbot = (int) ((y - (collisionWidth / 2)) / map.tileHeight);
		int posXright = (int) ((x + (collisionWidth / 2)) / map.tileWidth);
		int posYtop = (int) ((y + (collisionHeight / 2)) / map.tileHeight);

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
	}
	
	public boolean getalive() {
		return alive;
	}

	public Animation getAnimation() {
		return animation;
	}
	
	public TextureRegion getTextureRegion(int current_frame) {
		return sprite.getTextureRegion(current_frame);
	}

	public float getX() {
		return (x - (this.sprite.getLargeurImage() / 2));
	}

	public float getY() {
		return (y - (this.sprite.getHauteurImage() / 2));
	}

	public float getTimeCast() {
		return timeCast;
	}

	public int getCollisionHeight() {
		return collisionHeight;
	}

	public int getCollisionWidth() {
		return collisionWidth;
	}

	public Texture getTexture() {
		return texture;
	}
	
	public int getManaCost() {
		return this.manaCost;
	}
	
	public int getWidth() {
		return this.width;
	}
	
	public int getHeight() {
		return this.width;
	}
	
	public void setAnimation(Animation animation) {
		this.animation = animation;
	}

	public void setX(float x) {
		this.x = x;
	}

	public void setY(float y) {
		this.y = y;
	}

	public void setTimeCast(float timeCast) {
		this.timeCast = timeCast;
	}

	public void setCollisionHeight(int collisionHeight) {
		this.collisionHeight = collisionHeight;
	}

	public void setCollisionWidth(int collisionWidth) {
		this.collisionWidth = collisionWidth;
	}

	public void setTexture(Texture texture) {
		this.texture = texture;
	};
}