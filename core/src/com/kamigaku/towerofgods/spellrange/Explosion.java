package com.kamigaku.towerofgods.spellrange;

import com.kamigaku.towerofgods.entitiesModel.Sprite;
import com.kamigaku.towerofgods.level.Level;
import com.kamigaku.towerofgods.spell.SpellAOE;

public class Explosion extends SpellAOE {
	
	public static final float cd = 2f;
	public static final int NOMBRE_IMAGE = 10;
	public static final int NOMBRE_LIGNE = 1;
	public static final float DUREE_ANIM = 0.05f;
	public static final int MANACOST = 0;
	public static final int RANGE = 300;
	public static final int RADIUS = 100;

	public Explosion(float xSpell, float ySpell, float xCaster, float yCaster) {
		super(xSpell, ySpell, xCaster, yCaster, MANACOST, RADIUS);
		this.damage = 10;
		this.width = 128;
		this.height = 128;
		this.sprite = new Sprite("data/explosion.png", this.width, this.height, NOMBRE_IMAGE, NOMBRE_LIGNE, DUREE_ANIM);
		this.currentAnim = 0;
	}
	
	@Override
	public void update(Level map) {
		if(launchToWall(map))
			alive = false;
		if(this.sprite.animationFinish()) {
			alive = false;
		}
		if(hasTicked() || !this.firstTick) {
			this.firstTick = true;
			checkCollisionToEntity(map);
		}
	}
	
	

}
