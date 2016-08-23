package com.kamigaku.towerofgods.ai;

import java.awt.Point;
import java.util.ArrayList;

import com.kamigaku.towerofgods.level.Level;

public class Pathfinding {

    public static Point generatePath(Level area, Point positionEnnemy, ArrayList<Point> playersPositions) {
        int map[][] = new int[area.mapHeight][area.mapWidth];
    	generateDatas(area, map, playersPositions);
    	
    	ArrayList<ArrayList<Node>> liste;
    	Point tabRetour = null;
        int currentLigne = 0;
        boolean pointTrouve[][] = new boolean[map.length][map[0].length];
        boolean findWay = false;
        
        for(int i = 0; i < pointTrouve.length; i++) {
        	for(int j = 0; j < pointTrouve[0].length; j++) {
        		pointTrouve[i][j] = false;
        	}
        }
        
        liste = new ArrayList<ArrayList<Node>>();
        liste.add(new ArrayList<Node>());
        liste.get(0).add(new Node(positionEnnemy.x, positionEnnemy.y, null, true));
        
        while(!findWay && currentLigne <= map.length) {
	        for(Node n : liste.get(currentLigne)) {
	            liste.add(new ArrayList<Node>());
	            for(int x = -1; x <= 1; x++) {
	                if(n.x+x >= 0 && n.x+x < map[0].length && x != 0 && !findWay) {
	                    if(map[n.y][n.x+x] == 0) {
	                    	if(!pointTrouve[n.y][n.x+x]) {
	                            liste.get(currentLigne+1).add(new Node(n.x+x, n.y, n));
	                            pointTrouve[n.y][n.x+x] = true;
	                        }
	                    }
	                    if(map[n.y][n.x+x] == 2) {
	                    	liste.get(currentLigne+1).add(new Node(n.x+x, n.y, n));
	                    	findWay = true;
	                    	tabRetour = remonterInverse(liste.get(currentLigne+1).get(liste.get(currentLigne+1).size()-1));
	                    	break;
	                    }
	                }
	            }
	            if(!findWay) {
		            for(int y = -1; y <= 1; y++) {
		                if(n.y+y >= 0 && n.y+y < map[0].length && y != 0 && !findWay) {
		                    if(map[n.y+y][n.x] == 0) {
		                    	if(!pointTrouve[n.y+y][n.x]) {
		                            liste.get(currentLigne+1).add(new Node(n.x, n.y+y, n));
		                            pointTrouve[n.y+y][n.x] = true;
		                        }
		                    }
		                    if(map[n.y+y][n.x] == 2) {
		                    	liste.get(currentLigne+1).add(new Node(n.x, n.y+y, n));
		                    	tabRetour = remonterInverse(liste.get(currentLigne+1).get(liste.get(currentLigne+1).size()-1));
		                    	findWay = true;
		                    	break;
		                    }
		                }
		            }
	            }
	        }
	        currentLigne++;
        }
        if(findWay)
        	return tabRetour;
        else 
        	return null;
    }

	private static void generateDatas(Level area, int[][] map, ArrayList<Point> playerPosition) {
        for(int y = 0; y < map.length; y++) {
            for(int x = 0; x < map[0].length; x++) {
                map[y][x] = 0;
            }
        }
        
        for(Point player : playerPosition)
        	map[(int)player.y / area.tileHeight][(int)player.x / area.tileWidth] = 2;
        
        for(int y = 0; y < map.length; y++) {
            for(int x = 0; x < map[y].length; x++) {
            	try {
	                if(area.collisionLayer.getCell(x, y).getTile().getProperties().containsKey("blocked") ||
	                		area.collisionLayer.getCell(x, y).getTile().getProperties().containsKey("wall"))
	                	map[y][x] = 1;
            	}
            	catch(NullPointerException e) {
            		System.out.println("Erreur en coordonnées : " + y + " | " + x + ".");
            	}
            }
        }
	}

	private static Point remonterInverse(Node node) {
		Point currentPoint = null;
		currentPoint = new Point(node.x, node.y);
		boolean end = false;
		Node currentNode = node.parent;
		while(!end) {
			if(currentNode.racine) {
				end = true;
				break;
			}
			currentPoint = new Point(currentNode.x, currentNode.y);
			currentNode = currentNode.parent;
		}
	return currentPoint;
	}
	
}