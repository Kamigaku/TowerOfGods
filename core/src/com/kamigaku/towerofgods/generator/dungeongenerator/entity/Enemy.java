package com.kamigaku.towerofgods.generator.dungeongenerator.entity;

import java.util.ArrayList;

public class Enemy {

	private String name;
	private char letter;
	private Class<?> classCall;
	private ArrayList<String> stats;
	
	public Enemy(String name, char letter, String classRoad) {
		this.name = name;
		this.letter = letter;
		try {
			this.classCall = Class.forName(classRoad);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		this.stats = new ArrayList<String>();
	}
	
	public void addStats(ArrayList<String> stats) {
		this.stats.addAll(stats);
	}
	
	public Class<?> getClassCall() {
		return this.classCall;
	}
	
	public char getLetter() {
		return this.letter;
	}
	
	public String getNom() {
		return this.name;
	}
}
