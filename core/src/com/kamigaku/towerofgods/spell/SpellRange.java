package com.kamigaku.towerofgods.spell;

import com.badlogic.gdx.math.Vector2;

public class SpellRange extends Spell {
	
	protected int rangeMax;
	protected int manaCost;
	protected float initialX;
	protected float initialY;
	protected float vitesseProjectile;
	protected double vectorX;
	protected double vectorY;
	protected boolean alive;

	public SpellRange(float x, float y, int collisionHeight, int collisionWidth, int rangeMax, float vitesseProjectile, int manaCost) {
		super(x, y, collisionHeight, collisionWidth, manaCost);
		this.rangeMax = rangeMax;
		this.initialX = this.x;
		this.initialY = this.y;
		this.alive = true;
		this.vitesseProjectile = vitesseProjectile;
	}

	protected void calculateDirection(Vector2 mouseCursor) {
		double norme = Math.sqrt(Math.pow(mouseCursor.y, 2) + Math.pow(mouseCursor.x, 2));
		this.vectorY = mouseCursor.y / norme;
		this.vectorX = mouseCursor.x / norme;
	}
	
	public boolean getalive() {
		return this.alive;
	}
	
	public void setDirection(double vectorX, double vectorY) {
		this.vectorX = vectorX;
		this.vectorY = vectorY;
	}
	
	@Override
	public void setX(float x) {
		this.x = x;
		this.initialX = x;
	}
	
	@Override
	public void setY(float y) {
		this.y = y;
		this.initialY = y;
	}
}