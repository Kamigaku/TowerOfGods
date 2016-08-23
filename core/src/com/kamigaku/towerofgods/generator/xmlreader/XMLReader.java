package com.kamigaku.towerofgods.generator.xmlreader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import com.kamigaku.towerofgods.GameLauncher;
import com.kamigaku.towerofgods.entitiesModel.StatsEnemy;
import com.kamigaku.towerofgods.generator.dungeongenerator.entity.Enemy;

public class XMLReader {
	
	private Document dungeonDoc, statsMob;
	private Element rootDungeon, rootStats;
	private String statsRoad = "xml/statsMob.xml";
	private ArrayList<String> enemies;
	private ArrayList<StatsEnemy> enemiesStats;
	
	public XMLReader(String dungeonDoc) {
		SAXBuilder builder = new SAXBuilder();
		try {
			this.dungeonDoc = builder.build(new File(GameLauncher.PATH_FILE + dungeonDoc));
			this.rootDungeon = this.dungeonDoc.getRootElement();
			this.statsMob = builder.build(new File(GameLauncher.PATH_FILE + this.statsRoad));
			this.rootStats = this.statsMob.getRootElement();
		} catch (JDOMException e) {
			e.printStackTrace();
		} 
	}
	
	public ArrayList<Enemy> getEnemies() {
		ArrayList<Enemy> enemies = new ArrayList<Enemy>();
		String classRoad = "";
		char lettre;
		String nom = "";
		for(Element cur : this.rootDungeon.getChild("enemies").getChildren("enemy")) {
			nom = cur.getChildText("name");
			lettre = cur.getChildText("letter").charAt(0);
			classRoad = cur.getChildText("class");
			enemies.add(new Enemy(nom, lettre, classRoad));
		}
		return enemies;
	}
	
	public void getStats() {
		enemies = new ArrayList<String>();
		enemiesStats = new ArrayList<StatsEnemy>();
		for(Element current : this.rootStats.getChild("enemies").getChildren("enemy")) {
			enemies.add(current.getChildText("name"));
			enemiesStats.add(new StatsEnemy(
					Integer.parseInt(current.getChildText("hp")),
					Integer.parseInt(current.getChildText("rangeAttack")),
					Integer.parseInt(current.getChildText("sizeWeapon")),
					Integer.parseInt(current.getChildText("damage")),
					Integer.parseInt(current.getChildText("mana")),
					Float.parseFloat(current.getChildText("regenHp")),
					Float.parseFloat(current.getChildText("regenMana")),
					Float.parseFloat(current.getChildText("speed")),
					Integer.parseInt(current.getChildText("rangeDetection")),
					Integer.parseInt(current.getChildText("experience"))));
		}
	}
	
	public ArrayList<String> getEnemiesName() {
		return this.enemies;
	}
	
	public ArrayList<StatsEnemy> getStatsForEnemies() {
		return this.enemiesStats;
	}
}