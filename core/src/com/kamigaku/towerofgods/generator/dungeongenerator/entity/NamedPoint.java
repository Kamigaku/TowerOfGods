package com.kamigaku.towerofgods.generator.dungeongenerator.entity;

import java.awt.Point;

public class NamedPoint extends Point {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -719680617341243780L;
	private String letter;

	public NamedPoint(int x, int y, String letter) {
		super(x, y);
		this.letter = letter;
	}
	
	public String getLetter() {
		return this.letter;
	}
	
}
