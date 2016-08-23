package com.kamigaku.towerofgods.generator.dungeongenerator.entity.entities;

import java.awt.Point;
import java.io.Serializable;

import com.kamigaku.towerofgods.generator.dungeongenerator.entity.Entity;

public class Door implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1154422053974034074L;
	private Point position;
	private Entity assignTo;
	
	public Door(Point position, Entity entity) {
		this.position = position;
		this.assignTo = entity;
		this.assignTo.addNumberOfDoor();
	}
	
	public Door(int x, int y, Entity entity) {
		this.position = new Point(x, y);
		this.assignTo = entity;
		this.assignTo.addNumberOfDoor();
	}
	
	public Point getPosition() {
		return this.position;
	}
	
	public void removeDoor() {
		this.assignTo.removeNumberOfDoor();
	}
	
	public boolean equals(Point p) {
		if(p.x == this.position.x && p.y == this.position.y)
			return true;
		return false;
	}
	
	public RoomRectangular getRoom() {
		return (RoomRectangular)this.assignTo;
	}

	public boolean sameRoom(Door currentDoor, Door doorToFind) {
//		System.out.println(currentDoor.getRoom().getBegin().x + " to " + doorToFind.getRoom().getBegin().x);
//		System.out.println(currentDoor.getRoom().getBegin().y + " to " + doorToFind.getRoom().getBegin().y);
//		System.out.println(currentDoor.getRoom().getEnd().x + " to " + doorToFind.getRoom().getEnd().x);
//		System.out.println(currentDoor.getRoom().getEnd().y + " to " + doorToFind.getRoom().getEnd().y);
//		System.out.println("===================");
		if(currentDoor.getRoom().getBegin().x == doorToFind.getRoom().getBegin().x && 
				currentDoor.getRoom().getBegin().y == doorToFind.getRoom().getBegin().y &&
				currentDoor.getRoom().getEnd().x == doorToFind.getRoom().getEnd().x &&
				currentDoor.getRoom().getEnd().y == doorToFind.getRoom().getEnd().y)
			return true;
		return false;
	}
}
