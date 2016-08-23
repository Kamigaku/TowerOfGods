package com.kamigaku.towerofgods.generator.dungeongenerator.entity;

import java.awt.Point;
import java.io.Serializable;

public class Entity implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 95959429978374397L;
	private Point begin;
	private int numberOfDoor;
	
	public Entity(Point begin) {
		this.begin = begin;
		this.numberOfDoor = 0;
	}
	
	public Entity(int x, int y) {
		this.begin = new Point(x, y);
		this.numberOfDoor = 0;
	}
	
	public Point getBegin() {
		return this.begin;
	}
	
	public void addNumberOfDoor() {
		this.numberOfDoor++;
	}
	
	public void removeNumberOfDoor() {
		this.numberOfDoor--;
	}
	
	public int getNumberOfDoor() {
		return numberOfDoor;
	}
}
