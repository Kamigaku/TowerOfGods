package com.kamigaku.towerofgods.generator.dungeongenerator.entity.entities;

import java.awt.Point;
import java.util.ArrayList;

import com.kamigaku.towerofgods.ai.Node;

public class Corridor {

	public static ArrayList<Point> generateRoad(String[][] map, Point begin, Point end) {	
		ArrayList<ArrayList<Node>> nodes = new ArrayList<ArrayList<Node>>();
		
		boolean[][] mapFind = new boolean[map.length][map[0].length];
		boolean roadDraw = false;
		int currentLigne = 0;
		
		mapFind = fillFalse(mapFind);
		mapFind[begin.y][begin.x] = true;
		nodes.add(new ArrayList<Node>());
		nodes.get(currentLigne).add(new Node(begin.x, begin.y, null, true));
		while(!roadDraw) {
			nodes.add(new ArrayList<Node>());
			for(Node n : nodes.get(currentLigne)) {
				for(int x = -1; x <= 1; x += 2) {
					if((map[n.y][n.x + x] == "/" || map[n.y][n.x + x] == "D") && !mapFind[n.y][n.x + x]) {
						if(n.y == end.y && (n.x + x) == end.x) {
	                    	nodes.get(currentLigne + 1).add(new Node(n.x+x, n.y, n));
	                    	roadDraw = true;
	                    	return getRoad(nodes.get(currentLigne+1).get(nodes.get(currentLigne+1).size()-1));				
						}
						else {
							nodes.get(currentLigne + 1).add(new Node(n.x+x, n.y, n));
							mapFind[n.y][n.x + x] = true;
						}
					}
				}
				for(int y = -1; y <= 1; y += 2) {
					if((map[n.y + y][n.x] == "/" || map[n.y + y][n.x] == "D") && !mapFind[n.y + y][n.x]) {
						if((n.y + y) == end.y && n.x == end.x) {
	                    	nodes.get(currentLigne + 1).add(new Node(n.x, n.y + y, n));
	                    	roadDraw = true;
	                    	return getRoad(nodes.get(currentLigne+1).get(nodes.get(currentLigne+1).size()-1));
						}
						else {
							nodes.get(currentLigne + 1).add(new Node(n.x, n.y + y, n));
							mapFind[n.y + y][n.x] = true;
						}						
					}
				}
			}
			currentLigne++;
		}
		return null;
	}
	
	private static boolean[][] fillFalse(boolean[][] mapFind) {
		for(int i = 0; i < mapFind.length; i++) {
			for(int j = 0; j < mapFind[i].length; j++) {
				mapFind[i][j] = false;
			}
		}
		return mapFind;
	}
	
	public static ArrayList<Point> getRoad(Node node) {
		ArrayList<Point> road = new ArrayList<Point>();
		while(!node.racine) {
			road.add(new Point(node.x, node.y));
			node = node.parent;
		}
		return road;
	}
	
	public static void drawDungeon(boolean[][] mapFind) {
		for(int i = 0; i < mapFind.length; i++) {
			for(int j = 0; j < mapFind[i].length; j++) {
				if(mapFind[i][j] == false)
					System.out.print("X");
				else
					System.out.print(" ");
			}
			System.out.println();
		}
	}
	
}