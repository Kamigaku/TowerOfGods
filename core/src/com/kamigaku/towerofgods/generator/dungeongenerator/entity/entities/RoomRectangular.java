package com.kamigaku.towerofgods.generator.dungeongenerator.entity.entities;

import java.awt.Point;

import com.kamigaku.towerofgods.generator.dungeongenerator.entity.Entity;

public class RoomRectangular extends Entity {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8632162568546804657L;

	private Point end;
	
	public static int maxWidthRoom = 15;
	public static int maxHeightRoom = 12;
	public static int minWidthRoom = 7;
	public static int minHeightRoom = 5;
	
	public RoomRectangular(Point begin, Point end) {
		super(begin);
		this.end = end;
	}
	
	public RoomRectangular(int xBegin, int xEnd, int yBegin, int yEnd) {
		super(xBegin, yBegin);
		this.end = new Point(xEnd, yEnd);
	}
	
	public void generateObject() {
		
	}
	
	public Point getEnd() {
		return this.end;
	}

	public static void setMaxWidthRoom(int maxWidthRoom) {
		RoomRectangular.maxWidthRoom = maxWidthRoom;
	}

	public static void setMaxHeightRoom(int maxHeightRoom) {
		RoomRectangular.maxHeightRoom = maxHeightRoom;
	}

	public static void setMinWidthRoom(int minWidthRoom) {
		RoomRectangular.minWidthRoom = minWidthRoom;
	}

	public static void setMinHeightRoom(int minHeightRoom) {
		RoomRectangular.minHeightRoom = minHeightRoom;
	}
	
}
