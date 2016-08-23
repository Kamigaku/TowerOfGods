package com.kamigaku.towerofgods.level.levels;

import java.awt.Point;
import java.util.ArrayList;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.kamigaku.towerofgods.GameLauncher;
import com.kamigaku.towerofgods.entitiesModel.Enemy;
import com.kamigaku.towerofgods.entitiesModel.Player;
import com.kamigaku.towerofgods.generator.mazegenerator.Generator;
import com.kamigaku.towerofgods.generator.xmlmapper.XMLGenerator;
import com.kamigaku.towerofgods.level.Level;
import com.kamigaku.towerofgods.spell.Spell;

public class Maze extends Level implements ApplicationListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private BitmapFont fps;
	private Point startingPoint;
	private static final int numberLine = 10;
	
    @Override
    public void create() {
    	generateMap(numberLine);
        batch = new SpriteBatch();
        map = new TmxMapLoader().load(GameLauncher.PATH_FILE + "generator/mapgenerate.tmx");
        this.mapHeight = map.getProperties().get("height", Integer.class);
        this.mapWidth = map.getProperties().get("width", Integer.class);
        this.tileHeight = map.getProperties().get("tileheight", Integer.class);
        this.tileWidth = map.getProperties().get("tilewidth", Integer.class);
        collisionLayer = (TiledMapTileLayer) map.getLayers().get(0);
    	int startingX = (this.startingPoint.x) * numberLine * this.tileWidth + ((numberLine / 2) * this.tileWidth);
    	int startingY = (this.startingPoint.y) * numberLine * this.tileHeight + ((numberLine / 2) * this.tileHeight);
    	this.players = new ArrayList<Player>();
        this.players.add(new Player(startingX, startingY));
        renderer = new OrthogonalTiledMapRenderer(map);
//		camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
//		camera.position.set(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2, 0);
//        camera.update();
        fps = new BitmapFont();
        enemies = new ArrayList<Enemy>();
//        camera.zoom = 0.8f; //A voir plus tard
    }

	@Override
	public void render() {
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		
    	renderer.setView(camera);   	
    	mouvementCamera();
        camera.update();   
        renderer.render();
        batch.setProjectionMatrix(camera.combined);
        
        batch.begin();
        int number = 0;
        for(Player player : this.players) {
	    	player.update(this);
		    for(int i = 0; i < player.getSpell().size(); i++) {
		    	Spell s = player.getSpell().get(i);
				    if(s.getalive()) {
				    	s.update(this);
				    	batch.draw(s.getTextureRegion(s.currentAnim), s.getX(), s.getY());
				    }
				    else {
				    	player.removeElements(s);
				    	i--;
				    }
		    }
		    batch.draw(this.playersSprite.get(number).getTextureRegion(player.current_anim), player.getX(), player.getY());
        }
		batch.end();
	}
	
    @Override
    public void pause() {
    }	    
	@Override
	public void resume() {
	}
	@Override
	public void dispose() {
		fps.dispose();
    	batch.dispose();
    	for(Player player : this.players) {
        	for(Spell s : player.getSpell())
        		s.getTexture().dispose();
    	}
    	map.dispose();
    	renderer.dispose();
	}

	private void generateMap(int numberLine) {
		GameLauncher.PATH_FILE = this.getClass().getClassLoader().getResource("").getPath();
		if(GameLauncher.PRODUCTION) {
			GameLauncher.PATH_FILE += "external/";
		}
		Generator generator = new Generator(25, 14, "|||");
		generator.addCommunRules();
		while(generator.getMapSize() < 15)
			generator.generateMap();
		this.startingPoint = generator.getStartingPoint();
 		String[][] map = generator.getMap();
 		String tilesetsFile = GameLauncher.PATH_FILE + "generator/Tilesets/dungeon_wall.txt";
 		String mapFile = GameLauncher.PATH_FILE + "generator/mapgenerate.tmx";
 		String tilesFile = GameLauncher.PATH_FILE + "generator/Maps/Maze/";		
 		new XMLGenerator(mapFile, tilesetsFile, tilesFile, map, numberLine);
	}
}
