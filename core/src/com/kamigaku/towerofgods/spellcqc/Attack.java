package com.kamigaku.towerofgods.spellcqc;

import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.kamigaku.towerofgods.entitiesModel.Enemy;
import com.kamigaku.towerofgods.entitiesModel.Sprite;
import com.kamigaku.towerofgods.level.Level;
import com.kamigaku.towerofgods.spell.SpellCQC;

public class Attack extends SpellCQC {
	
	public static final float cd = 0.5f;
	public static final int TOP = 3;
	public static final int LEFT = 0;
	public static final int RIGHT = 1;
	public static final int BOT = 2;
	public static final int NOMBRE_IMAGE = 5;
	public static final int NOMBRE_LIGNE = 4;
	public static final float DUREE_ANIM = 0.15f;

	public Attack(float x, float y, int collisionHeight,
			int collisionWidth) {
		super(x, y, collisionHeight, collisionWidth, 0);
		this.damage = 1;
		this.sprite = new Sprite("data/fireball.png", this.width, this.height, NOMBRE_IMAGE, NOMBRE_LIGNE, DUREE_ANIM);
		this.currentAnim = 0;
	}
	
	@Override
	public void update(Level map) {
		float vecteur[] = {this.x, this.y, this.x+this.collisionWidth, this.y, this.x+this.collisionWidth, this.y+this.collisionHeight, 
				this.x, this.y+this.collisionHeight};
		Polygon p = new Polygon(vecteur);
		p.setOrigin(this.x + (collisionWidth / 2), this.y);
		p.rotate(60);
		Rectangle spell = new Rectangle(x, y, collisionWidth, collisionHeight);		
		for(Enemy e : map.enemies) {
			Rectangle ennemy = new Rectangle(e.getX() + e.diffCollisionWidth, e.getY() + e.diffCollisionHeight,
					e.collisionWidth, e.collisionHeight);
			if(spell.overlaps(ennemy) || ennemy.overlaps(spell)) {
				e.stats.hp -= this.damage;
			}
		}
		alive = false;
	}
	
	

}
