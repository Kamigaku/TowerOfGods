package com.kamigaku.towerofgods.spell;

import java.io.Serializable;

import com.badlogic.gdx.Gdx;

public class Cooldown implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2074368951452184414L;
	public float whenCast;
	private boolean endCast;
	private float cd;
	
	public Cooldown(float cd) {
		this.cd = cd;
		this.whenCast = 0;
		endCast = true;
	}
	
	public void update() {
		float delta = Gdx.graphics.getDeltaTime();
		if(!endCast) {
			whenCast += delta;
		}
		if(whenCast > cd)
			endCast = true;
	}
	
	public boolean getendCast() {
		return endCast;
	}
	
	public void setendCast(boolean b) {
		this.endCast = b;
		resetCast();
	}
	
	public void resetCast() {
		this.whenCast = 0;
	}
	
}