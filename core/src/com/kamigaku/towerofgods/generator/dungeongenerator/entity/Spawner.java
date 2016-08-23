package com.kamigaku.towerofgods.generator.dungeongenerator.entity;

import java.util.ArrayList;

public class Spawner {

	private ArrayList<Enemy> enemies;
	
	public Spawner() {
		this.enemies = new ArrayList<Enemy>();
	}
	
	public Spawner(ArrayList<Enemy> enemies) {
		this.enemies = enemies;
	}
	
	public void addElement(Enemy e) {
		this.enemies.add(e);
	}
	
	public void addMultipleElements(ArrayList<Enemy> e) {
		this.enemies.addAll(e);
	}

	public ArrayList<Enemy> getEnemies() {
		return this.enemies;
	}
	
}
