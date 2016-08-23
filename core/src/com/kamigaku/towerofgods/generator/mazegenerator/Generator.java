/*
 * This file is part of the MapGeneratorRender project.
 *    
 * Copyright 2014-2020 Kamigaku. All Rights Reserved.
 * Email me at : kamigaku@gmail.com
 *
 * is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 2 of the License.
 *
 * is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with . If not, see <http://www.gnu.org/licenses>.
 */

package com.kamigaku.towerofgods.generator.mazegenerator;

import java.awt.Point;
import java.util.ArrayList;

import com.kamigaku.towerofgods.generator.mazegenerator.rules.Rules;

public class Generator {
	
	private String[][] map;
	private ArrayList<Point> positionIntegre;
	private ArrayList<Point> lastPointPlaced;
	private ArrayList<Point> toBePlaced;
	private Point startingPoint;
	private ArrayList<Rules> rules;
	private String wall;
	private int width, height;
	
	/**
	 * 
	 * Constructor of the Map Generator. 
	 * Generate an empty array.
	 * 
	 * @param width : The width of your generated map.
	 * @param height : The height of your generated map.
	 * @param wall : This is a default value for your wall. (ex. : |||)
	 */
	public Generator(int width, int height, String wall) {
		map = new String[height][width];
		rules = new ArrayList<Rules>();
		this.wall = wall;
		this.width = width;
		this.height = height;
		this.positionIntegre = new ArrayList<Point>();
		this.lastPointPlaced = new ArrayList<Point>();
		this.toBePlaced = new ArrayList<Point>();
	}

	/**
	 * Allow you to add a single rule to your map generator.
	 * 
	 * @param r : The rule you want to add.
	 */
	public void addRule(Rules r) {
		rules.add(r);
	}
	
	/**
	 * Allow you to add multiple rules in the same time.
	 * 
	 * @param rs : An arraylist with your rules inside.
	 */
	public void addRules(ArrayList<Rules> rs) {
		rules.addAll(rs);
	}
	
	/**
	 * If you don't want to create your own rules, use this premade rules.
	 * 
	 */
	public void addCommunRules() {
		String[] element = new String[10];
		
		element[0] = "|__";
		element[1] = "__|";
		element[2] = "| |";
		element[3] = "|¯¯";
		element[4] = "¯¯|";
		element[5] = "===";
		element[6] = "   ";
		
		String[] toLeft;
		String[] toRight;
		String[] toTop;
		String[] toBot;
		
		toLeft = new String[] { element[0], element[3], element[5], element[6] };
		toRight = new String[] { };
		toTop = new String[] {  };
		toBot = new String[] { element[0], element[1], element[2], element[6] };
		Rules r1 = new Rules("¯¯|", "Rules number 1", toLeft, toRight, toTop, toBot);
		
		toLeft = toRight = toTop = toBot = null;
		
		toLeft = new String[] { };
		toRight = new String[] { };
		toTop = new String[] { element[2], element[3], element[4], element[6] };
		toBot = new String[] { element[0], element[1], element[2], element[6] };
		Rules r2 = new Rules("| |", "Rules number 2", toLeft, toRight, toTop, toBot);
		
		toLeft = toRight = toTop = toBot = null;
		
		toLeft = new String[] { };
		toRight = new String[] { element[1], element[4], element[5], element[6] };
		toTop = new String[] { };
		toBot = new String[] { element[0], element[1], element[2], element[6] };
		Rules r3 = new Rules("|¯¯", "Rules number 3", toLeft, toRight, toTop, toBot);
		
		toLeft = toRight = toTop = toBot = null;
		
		toLeft = new String[] { element[0], element[3], element[5], element[6] };
		toRight = new String[] { };
		toTop = new String[] { element[2], element[3], element[4], element[6] };
		toBot = new String[] { };
		Rules r4 = new Rules("__|", "Rules number 4", toLeft, toRight, toTop, toBot);
				
		toLeft = toRight = toTop = toBot = null;

		toLeft = new String[] { };
		toRight = new String[] { element[1], element[4], element[5], element[6] };
		toTop = new String[] { element[2], element[3], element[4], element[6] };
		toBot = new String[] { };
		Rules r5 = new Rules("|__", "Rules number 5", toLeft, toRight, toTop, toBot);	
		
		toLeft = toRight = toTop = toBot = null;

		toLeft = new String[] { element[0], element[3], element[5], element[6] };
		toRight = new String[] { element[1], element[4], element[5], element[6] };
		toTop = new String[] { };
		toBot = new String[] { };
		Rules r6 = new Rules("===", "Rules number 6", toLeft, toRight, toTop, toBot);
		
		toLeft = new String[] { element[0], element[3], element[5], element[6] };
		toRight = new String[] { element[1], element[4], element[5], element[6] };
		toTop = new String[] { element[2], element[3], element[4], element[6] };
		toBot = new String[] { element[0], element[1], element[2], element[6] };
		Rules r7 = new Rules("   ", "Rules number 7", toLeft, toRight, toTop, toBot);
		
		ArrayList<Rules> rules = new ArrayList<Rules>();
		rules.add(r1);
		rules.add(r2);
		rules.add(r3);
		rules.add(r4);
		rules.add(r5);
		rules.add(r6);
		rules.add(r7);
				
		this.addRules(rules);
	}
	

	@Override
	public String toString() {
		String msg = "This generator is composed of " + rules.size() + " rules which are :\n";
		for(Rules r : this.rules) {
			msg += "   - " + r.getName() + "\n";
		}
		return msg;
	}
	
	/**
	 * Main function of who execute everything who needs to be done to create your map.
	 * 
	 */
	public void generateMap() {
		resetGeneration();
		System.out.println("Starting the generation...");
		determineStartPoint();
		System.out.println("Beginning point is picked...\nGenerating the map...");
		generateRoad();
	}
	
	/**
	 * Reinitialize the map and add only wall.
	 * 
	 */
	private void resetGeneration() {
		for(int i = 0; i < height; i++) {
			for(int j = 0; j < width; j++) {
				map[i][j] = this.wall;
			}
		}
		this.positionIntegre = new ArrayList<Point>();
		this.lastPointPlaced = new ArrayList<Point>();
		this.toBePlaced = new ArrayList<Point>();
	}

	/**
	 * Generate the point where the generation will start. It makes a choice between the top or the bottom and the left of the bot.
	 * 
	 */
	private void determineStartPoint() {
		int firstPosition = (int) Math.round(Math.random() + 1);
		int whatRulesToStart = (int) Math.round(Math.random() * (rules.size() - 1));
		if(firstPosition == 1) {
			int secondPosition = (int) Math.round(Math.random() * (height - 1));
			this.map[secondPosition][0] = this.rules.get(whatRulesToStart).getMain();
			this.startingPoint = new Point(0, secondPosition);
		}
		else {
			int secondPosition = (int) Math.round(Math.random() * (width - 1));
			this.map[0][secondPosition] = this.rules.get(whatRulesToStart).getMain();
			this.startingPoint = new Point(secondPosition, 0);
		}
		this.lastPointPlaced.add(this.startingPoint);
		System.out.println(this.startingPoint);
	}
	
	/**
	 * Main function generating the map.	 * 
	 * 
	 */
	private void generateRoad() {
		boolean endGeneration = false;
		while(!endGeneration) {
			if(this.lastPointPlaced.size() == 0) 
				endGeneration = true;
			else {
				for(Point currentPos : this.lastPointPlaced) {
					if(this.map[currentPos.y][currentPos.x] != this.wall) {
						Rules currentRule = getCurrentRules(currentPos);
						String[] elements = null;
						if(currentPos.x - 1 >= 0 && !alreadyGenerated(currentPos.x - 1, currentPos.y)) {
							elements = currentRule.getToSomething(Rules.TOLEFT);
							if(elements.length > 0)
								this.addElement(elements, currentPos.x - 1, currentPos.y);
						}
						if(currentPos.x + 1 < map[0].length && !alreadyGenerated(currentPos.x + 1, currentPos.y)) {
							elements = currentRule.getToSomething(Rules.TORIGHT);
							if(elements.length > 0)
								this.addElement(elements, currentPos.x + 1, currentPos.y);
						}
						if(currentPos.y + 1 < map.length && !alreadyGenerated(currentPos.x, currentPos.y + 1)) {
							elements = currentRule.getToSomething(Rules.TOTOP);
							if(elements.length > 0)
								this.addElement(elements, currentPos.x, currentPos.y + 1);
						}
						if(currentPos.y - 1 >= 0 && !alreadyGenerated(currentPos.x, currentPos.y - 1)) {
							elements = currentRule.getToSomething(Rules.TOBOT);
							if(elements.length > 0)
								this.addElement(elements, currentPos.x, currentPos.y - 1);
						}
					}
				}
				this.positionIntegre.addAll(lastPointPlaced);
				this.lastPointPlaced.clear();
				this.lastPointPlaced.addAll(toBePlaced);
				this.toBePlaced.clear();
			}
		}
	}
	
	/**
	 * Check if a point is not already existing in the map.
	 * 
	 * @param x : The x coordinate
	 * @param y : The y coordinate
	 * @return : True if the value is already generated, else false.
	 */
	private boolean alreadyGenerated(int x, int y) {
		if(this.positionIntegre.contains(new Point(x, y)))
			return true;
		return false;
	}

	/**
	 * Add a point to the map.
	 * 
	 * @param elements : The element corresponding to the rules choose.
	 * @param x : The x coordinate of the point to add.
	 * @param y : The y coordinate of the point to add.
	 */
	private void addElement(String[] elements, int x, int y) {
		double wallAdd = Math.random() * 100 * this.width;
		if((int)wallAdd < this.positionIntegre.size()) {
			this.map[y][x] = this.wall;
		}
		else {
			int whatToAdd = (int) Math.floor(Math.random() * elements.length);
			this.map[y][x] = elements[whatToAdd];
			this.toBePlaced.add(new Point(x, y));
		}
	}
	
	/**
	 * Allow us to know what rules is at the current point.
	 * 
	 * @param currentPos : The current position.
	 * @return : The current rules.
	 */
	private Rules getCurrentRules(Point currentPos) {
		for(Rules r : this.rules) {
			if(r.getMain() == this.map[currentPos.y][currentPos.x])
				return r;
		}
		return null;
	}

	/**
	 * Draw the map.
	 */
	public void drawMap() {
		for(int i = this.height - 1; i >= 0; i--) {
			for(int j = 0; j < this.width; j++) {
				System.out.print(this.map[i][j]);
			}
			System.out.println();
		}		
	}
	
	/**
	 * Get the number of points added to the map.
	 * 
	 * @return : The number of points added.
	 */
	public int getMapSize() {
		return this.positionIntegre.size();
	}
	
	/**
	 * Get the map.
	 * 
	 * @return : The map.
	 */
	public String[][] getMap() {	
		return this.map;
	}
	
	public Point getStartingPoint() {
		return this.startingPoint;
	}
}