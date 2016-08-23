/*
 * This file is part of the MapGeneratorRender project.
 *    
 * Copyright 2014-2042 Kamigaku. All Rights Reserved.
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

package com.kamigaku.towerofgods.generator.xmlmapper;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class XMLGenerator {
	
	private FileWriter file;
	private String[] datas;
	private int tileWidth, tileHeight;
	private int GROUND, WALL;
	private String[] allDatas;
	private int[][] finalTabDatas;
	private int currentLine;

	/**
	 * Constructor of the XMLGenerator.
	 * 
	 * @param nomfichier : Road + name of the file where the map will be rendered.
	 * @param tileset : Road + name of the header for the tileset.
	 * @param dossierTiles : Road + type name of all the tiles to apply.
	 * @param maps : The generated map.
	 */
	public XMLGenerator(String nomfichier, String tileset, String dossierTiles, String[][] map, int linePerFile) {
		try {
			this.file = new FileWriter(new File(nomfichier), false);
			this.allDatas = new String[map.length * linePerFile];
			this.datas = new String[linePerFile];
			for(int i = 0; i < this.datas.length; i++) {
				this.datas[i] = "";
			}
			writeTileset(tileset, map, linePerFile);
			writeCalque(map[0].length * linePerFile, map.length * linePerFile, map, dossierTiles, linePerFile);
			cleanDatas();
			writeDatas();
			this.file.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Write the header of the file according to the tile width and height and the width and height of the map.
	 * 
	 * @param width : Width of the map.
	 * @param height : Height of the map.
	 */
	private void writeEntete(int width, int height) {
		try {
			this.file.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
						"<map version=\"1.0\" orientation=\"orthogonal\" width=\"" + width + "\" height=\"" + height + "\" " +
								"tilewidth=\"" + this.tileWidth + "\" tileheight=\"" + this.tileHeight + "\">\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Write the information about the tileset according to the file.
	 * 
	 * @param tileset : The road + name of the tileset file.
	 * @param map 
	 * @param linePerFile 
	 */
	private void writeTileset(String tileset, String[][] map, int linePerFile) {
		try {
			FileReader tileSetReader = new FileReader(new File(tileset));
			BufferedReader in = new BufferedReader(tileSetReader); 
			String str;
			int currentTile = 0;
			while ((str=in.readLine()) != null) {
				if(str.contains("<tileset")) {
					List<String> extractedDatas = extractDigit(str);
					this.tileWidth = Integer.parseInt(extractedDatas.get(1));
					this.tileHeight = Integer.parseInt(extractedDatas.get(2));
					writeEntete(map[0].length * linePerFile, map.length * linePerFile);
				}
				if(str.contains("<tile id=")) {
					List<String> extractedDatas = extractDigit(str);
					currentTile = Integer.parseInt(extractedDatas.get(0));
				}
				if(str.contains("<property name=")) {
					checkTileType(str, currentTile);
				}
				this.file.write(str + "\n");
			} 
			tileSetReader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Write inside the main file every layer of the map.
	 * This takes only the default element of the rules. If you want to add yours, implement this method
	 * or modify it.
	 * 
	 * @param width : Width of the map.
	 * @param height : Height of the map.
	 * @param map : The map.
	 * @param dossierTiles : Road + type name of all the tiles to apply.
	 * @throws Exception
	 */
	private void writeCalque(int width, int height, String[][] map, String dossierTiles, int linePerFile) throws Exception {
		try {
			this.file.write(" <layer name=\"Map layer\" width=\"" + width + "\" height=\"" + height + "\">\n" + 
								"  <data encoding=\"csv\">\n");
			for(int i = 0; i <= map.length - 1; i++) {
				for(int j = 0; j < map[0].length; j++) {
					String val = map[i][j];
					if(val == "__|")
						storeDatas("basdroite", dossierTiles);
					else if(val.length() >= 4)
						storeDatas(val, dossierTiles);
					else if(val.equals("|__"))
						storeDatas("basgauche", dossierTiles);
					else if(val.equals("| |"))
						storeDatas("milieu", dossierTiles);
					else if(val.equals("|¯¯"))
						storeDatas("hautgauche", dossierTiles);
					else if(val.equals("¯¯|"))
						storeDatas("hautdroite", dossierTiles);
					else if(val.equals("==="))
						storeDatas("gauchetodroite", dossierTiles);
					else if(val.equals("#") || val.equals("|||"))
						storeDatas("wall", dossierTiles);
					else if(val.equals("   ") || val.equals("/"))
						storeDatas("empty", dossierTiles);
					else if(val.equals("S") || val.equals("D") || val.equals(" "))
						storeDatas("ground", dossierTiles);
					else {
						storeDatas("ground", dossierTiles);
//						throw new Exception("This element is not implemented. Add this case to the switch : " + map[i][j]);
					}
				}
				if(i == 0) {
					this.datas[this.datas.length - 1] = 
						this.datas[this.datas.length - 1].substring(0, this.datas[this.datas.length - 1].length() - 1);
				}
				for(int k = 0; k < this.datas.length; k++) {
					this.allDatas[k + this.currentLine] = this.datas[k];
					this.datas[k] = "";
				}
				this.currentLine += linePerFile;
			}
			correctTheMap(linePerFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void cleanDatas() {
		for(int y = 0; y < this.finalTabDatas.length; y++) {
			for(int x = 0; x < this.finalTabDatas[0].length; x++) {
				if(this.finalTabDatas[y][x] == this.WALL) {
					// check au dessus
					if(y == 1) {
						this.finalTabDatas[y - 1][x] = this.WALL;
					}
					if(y == this.finalTabDatas.length - 2)
						this.finalTabDatas[y + 1][x] = this.WALL;
					if(y >= 2) {
						if(this.finalTabDatas[y - 1][x] != this.GROUND && this.finalTabDatas[y - 2][x] == this.WALL) {
							this.finalTabDatas[y - 1][x] = this.WALL;
						}
					}
					// check en dessous
					if(y <= this.finalTabDatas.length - 3) {
						if(this.finalTabDatas[y + 1][x] != this.GROUND && this.finalTabDatas[y + 2][x] == this.WALL) {
							this.finalTabDatas[y + 1][x] = this.WALL;
						}						
					}
					if(x >= 2) {
						if(this.finalTabDatas[y][x - 1] != this.GROUND && this.finalTabDatas[y][x - 2] == this.WALL) {
							this.finalTabDatas[y][x - 1] = this.WALL;
						}
					}
					// check en dessous
					if(x <= this.finalTabDatas[0].length - 3) {
						if(this.finalTabDatas[y][x + 1] != this.GROUND && this.finalTabDatas[y][x + 2] == this.WALL) {
							this.finalTabDatas[y][x + 1] = this.WALL;
						}						
					}
				}
			}			
		}
	}
	
	/**
	 * Write all the datas stored and clean the datas array.
	 */
	private void writeDatas() {
		try {
			for(int i = 0; i < this.finalTabDatas.length; i++) {
				for(int j = 0; j < this.finalTabDatas[i].length; j++) {
					if(i == this.finalTabDatas.length -1 && j == this.finalTabDatas[i].length -1)
						this.file.write(this.finalTabDatas[i][j] + "");
					else
						this.file.write(this.finalTabDatas[i][j] + ", ");
				}
				this.file.write("\n");
			}
			this.file.write("</data></layer></map>");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Store the datas to write line by line.
	 * 
	 * @param type : The type of the tiles to write.
	 * @param dossierTiles : Road + type name of all the tiles to apply.
	 */
	private void storeDatas(String type, String dossierTiles) {
		FileReader layerReader;
		try {
			layerReader = new FileReader(
					new File(dossierTiles + type + ".txt"));
			BufferedReader in = new BufferedReader(layerReader);
			String str; 
			int i = 0;
			while ((str=in.readLine()) != null) {
				this.datas[i] += str;
				i++;
			} 
			layerReader.close();	
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void correctTheMap(int linePerFile) {
		ArrayList<List<String>> entities = new ArrayList<List<String>>();
		for(int i = 0; i < this.allDatas.length; i++) {
			entities.add(Arrays.asList(this.allDatas[i].trim().split(",")));
		}
		int sizeMax = 0;
		for(int i = 0; i < entities.size(); i++) {
			if(entities.get(i).size() > sizeMax)
				sizeMax = entities.get(i).size();
		}
		this.finalTabDatas = new int[entities.size()][sizeMax];
		for(int i = 0; i < entities.size(); i++) {
			for(int j = 0; j < entities.get(i).size(); j++) {
				this.finalTabDatas[i][j] = Integer.parseInt(entities.get(i).get(j));
			}
		}
	}


	private List<String> extractDigit(String str) {
		str = str.replaceAll("[^0-9]+", " ");
		List<String> tableau = Arrays.asList(str.trim().split(" "));
		return tableau;
	}
	
	
	private void checkTileType(String str, int currentTile) {
		if(str.contains("GROUND")) {
			this.GROUND = currentTile+1;
		}
		else if(str.contains("BLOCK")) {
			this.WALL = currentTile+1;
		}
	}
}