package com.kamigaku.towerofgods.level.levels;

import java.util.ArrayList;

import box2dLight.Light;
import box2dLight.PointLight;
import box2dLight.RayHandler;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.kamigaku.towerofgods.GameLauncher;
import com.kamigaku.towerofgods.entitiesModel.Enemy;
import com.kamigaku.towerofgods.entitiesModel.Player;
import com.kamigaku.towerofgods.entitiesModel.Sprite;
import com.kamigaku.towerofgods.generator.dungeongenerator.entity.Spawner;
import com.kamigaku.towerofgods.generator.dungeongenerator.map.Map;
import com.kamigaku.towerofgods.generator.xmlmapper.XMLGenerator;
import com.kamigaku.towerofgods.generator.xmlreader.XMLReader;
import com.kamigaku.towerofgods.hud.HUD;
import com.kamigaku.towerofgods.level.Level;
import com.kamigaku.towerofgods.spell.Spell;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Logger;

public class Dungeon extends Level implements ApplicationListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private BitmapFont fps;
	private World world;
	public final int numberLine = 3;
	private ArrayList<Light> lightPlayers;
	
	public Dungeon() {
	}

	// Multiplayer creation
	public Dungeon(Map m) {
        this.enemies = new ArrayList<Enemy>();
    	this.playersSprite = new ArrayList<Sprite>();
        this.enemiesSprite = new ArrayList<Sprite>();
        
        // Stuff
        this.mapGenerate = m;
		GameLauncher.PATH_FILE = this.getClass().getClassLoader().getResource("").getPath();
		if(GameLauncher.PRODUCTION) {
			GameLauncher.PATH_FILE += "external/";
		}

		// Map generation
		String[][] mapTab = this.mapGenerate.getMap();
 		String tilesetsFile = GameLauncher.PATH_FILE + "generator/Tilesets/dungeon_wall.txt";
 		String mapFile = GameLauncher.PATH_FILE + "generator/mapgenerate.tmx";
 		String tilesFile = GameLauncher.PATH_FILE + "generator/Maps/Dungeon/";
 		new XMLGenerator(mapFile, tilesetsFile, tilesFile, mapTab, numberLine);
 		

	    // World generation and datas
    	this.world = new World(new Vector2(0, -9.8f), true);
        this.batch = new SpriteBatch();
        this.map = new TmxMapLoader().load(GameLauncher.PATH_FILE + "generator/mapgenerate.tmx");
        this.mapHeight = map.getProperties().get("height", Integer.class);
        this.mapWidth = map.getProperties().get("width", Integer.class);
        this.tileHeight = map.getProperties().get("tileheight", Integer.class);
        this.tileWidth = map.getProperties().get("tilewidth", Integer.class);
        this.collisionLayer = (TiledMapTileLayer) map.getLayers().get(0);
        
		this.startingPoint = m.getStartPoint();
		this.startingPoint.x = (this.startingPoint.x) * numberLine * this.tileWidth + ((numberLine / 2) * this.tileWidth);
		this.startingPoint.y = (this.startingPoint.y) * numberLine * this.tileHeight + ((numberLine / 2) * this.tileHeight);
        
        // Light
        this.lightPlayers = new ArrayList<Light>();
    	createLight();
        this.handler = new RayHandler(this.world);
        this.handler.setBlurNum(3);
        this.handler.setAmbientLight(0.6f, 0.3f, 0.8f, 0.1f);
        
        // Player
    	this.players = new ArrayList<Player>();
    	
        this.renderer = new OrthogonalTiledMapRenderer(map);
        
        // Other
        this.fps = new BitmapFont(Gdx.files.internal("hud/default.fnt")); 
        this.hud = new HUD(this.batch);
		this.camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		this.camera.zoom = 0.5f;
		this.camera.position.set(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2, 0);
        this.camera.update();
	}
	
    @Override
    public void create() {
		this.enemies = new ArrayList<Enemy>();
    	this.enemiesSprite = new ArrayList<Sprite>();
    	this.playersSprite = new ArrayList<Sprite>();
            try {
                generateMap(numberLine);
            } catch (NoSuchMethodException ex) {
                Logger.getLogger(Dungeon.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            } catch (InstantiationException ex) {
                Logger.getLogger(Dungeon.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            } catch (IllegalAccessException ex) {
                Logger.getLogger(Dungeon.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            } catch (IllegalArgumentException ex) {
                Logger.getLogger(Dungeon.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            } catch (InvocationTargetException ex) {
                Logger.getLogger(Dungeon.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            }
    	
    	// World stuff
    	this.world = new World(new Vector2(0, -9.8f), true);
        this.lightPlayers = new ArrayList<Light>();
        this.handler = new RayHandler(this.world);
        this.handler.setBlurNum(3);
        this.handler.setAmbientLight(0.6f, 0.3f, 0.8f, 0.1f);
        
        this.batch = new SpriteBatch();
        
        // Map & map datas
        this.map = new TmxMapLoader().load(GameLauncher.PATH_FILE + "generator/mapgenerate.tmx");
        this.mapHeight = map.getProperties().get("height", Integer.class);
        this.mapWidth = map.getProperties().get("width", Integer.class);
        this.tileHeight = map.getProperties().get("tileheight", Integer.class);
        this.tileWidth = map.getProperties().get("tilewidth", Integer.class);
        this.collisionLayer = (TiledMapTileLayer) map.getLayers().get(0);

    	// Player stuff
        this.players = new ArrayList<Player>();
        if(!GameLauncher.online) {
	    	int startingX = (this.startingPoint.x) * numberLine * this.tileWidth + ((numberLine / 2) * this.tileWidth);
	    	int startingY = (this.startingPoint.y) * numberLine * this.tileHeight + ((numberLine / 2) * this.tileHeight);
	        this.addPlayer(new Player(startingX, startingY), false);
        }
        
        this.renderer = new OrthogonalTiledMapRenderer(map);
        this.fps = new BitmapFont(Gdx.files.internal("hud/default.fnt"));
       
    	createLight();
        
        this.hud = new HUD(this.batch);
		this.camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		this.camera.position.set(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2, 0);
        this.camera.update();
        this.camera.zoom = 0.5f;
    }

	private void preRenderStuff() {
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		//Camera stuff
		this.renderer.setView(this.camera);
		this.renderer.render();
		this.batch.setProjectionMatrix(this.camera.combined);
	    this.handler.setCombinedMatrix(this.camera.combined, this.camera.position.x,
	    		this.camera.position.y, this.camera.viewportWidth * this.camera.zoom,
	    		this.camera.viewportHeight * this.camera.zoom);
	    this.handler.updateAndRender();
	}

	private void generateMap(int numberLine) throws NoSuchMethodException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		GameLauncher.PATH_FILE = this.getClass().getClassLoader().getResource("").getPath();
		if(GameLauncher.PRODUCTION) {
			GameLauncher.PATH_FILE += "external/";
		}
		this.mapGenerate = new Map(40, 20);
		mapGenerate.generateMap();
	    Spawner spawner = new Spawner();
		String[][] map = this.mapGenerate.getMap();
		this.startingPoint = mapGenerate.getStartPoint();
		this.startingPoint.y = map.length - 1 - this.startingPoint.y;
		String tilesetsFile = GameLauncher.PATH_FILE + "generator/Tilesets/dungeon_wall.txt";
		String mapFile = GameLauncher.PATH_FILE + "generator/mapgenerate.tmx";
		String tilesFile = GameLauncher.PATH_FILE + "generator/Maps/Dungeon/";
		new XMLGenerator(mapFile, tilesetsFile, tilesFile, map, numberLine);
	    XMLReader reader = new XMLReader("xml/castle.xml");
	    spawner.addMultipleElements(reader.getEnemies());
	    reader.getStats();
	    mapGenerate.spawnEnnemies(spawner.getEnemies(), this.enemies, this, reader.getEnemiesName(), reader.getStatsForEnemies());
	    for(Enemy e : this.enemies) {
	    	this.enemiesSprite.add(new Sprite(e.sprite, e.WIDTH, e.HEIGHT, e.NOMBRE_IMAGE, e.NOMBRE_LIGNE, e.DUREE_ANIM));
	    }
	}

	private void createLight() {
		for(int y = 0; y < this.mapHeight; y++) {
			for(int x = 0; x < this.mapWidth; x++) {
				if(this.collisionLayer.getCell(x, y).getTile().getProperties().containsKey("BLOCK")) {
					BodyDef bodyDef = new BodyDef();
				    bodyDef.type = BodyType.StaticBody;
				    bodyDef.position.set(new Vector2(x * this.tileWidth + (this.tileWidth / 2), y * this.tileHeight + (this.tileHeight / 2)));
				    Body body = this.world.createBody(bodyDef);
				    PolygonShape shape = new PolygonShape();
				    shape.setAsBox(this.tileWidth / 2, this.tileHeight / 2);
				    FixtureDef fixture = new FixtureDef();
				    fixture.friction = 0.3f;
				    fixture.shape = shape;
				    body.createFixture(fixture);
				    shape.dispose();
				}
				else if(this.collisionLayer.getCell(x, y).getTile().getProperties().containsKey("BOTTOMRIGHT") ||
						this.collisionLayer.getCell(x, y).getTile().getProperties().containsKey("TOPLEFT")) {
					BodyDef bodyDef = new BodyDef();
				    bodyDef.type = BodyType.StaticBody;
				    bodyDef.position.set(new Vector2(x * this.tileWidth + (this.tileWidth / 2), y * this.tileHeight + (this.tileHeight / 2)));
				    Body body = this.world.createBody(bodyDef);
				    PolygonShape shape = new PolygonShape();
				    Vector2[] vertices = new Vector2[4];
				    vertices[0] = new Vector2(-16, 16);
				    vertices[1] = new Vector2(16, -16);
				    vertices[2] = new Vector2(16, -15);
				    vertices[3] = new Vector2(-15, 16);
				    shape.set(vertices);
				    FixtureDef fixture = new FixtureDef();
				    fixture.friction = 0.3f;
				    fixture.shape = shape;
				    body.createFixture(fixture);
				    shape.dispose();
				}
				else if(this.collisionLayer.getCell(x, y).getTile().getProperties().containsKey("BOTTOMLEFT") ||
						this.collisionLayer.getCell(x, y).getTile().getProperties().containsKey("TOPRIGHT")) {
					BodyDef bodyDef = new BodyDef();
				    bodyDef.type = BodyType.StaticBody;
				    bodyDef.position.set(new Vector2(x * this.tileWidth + (this.tileWidth / 2), y * this.tileHeight + (this.tileHeight / 2)));
				    Body body = this.world.createBody(bodyDef);
				    PolygonShape shape = new PolygonShape();
				    Vector2[] vertices = new Vector2[4];
				    vertices[0] = new Vector2(16, 16);
				    vertices[1] = new Vector2(-16, -16);
				    vertices[2] = new Vector2(-16, -15);
				    vertices[3] = new Vector2(15, 16);
				    shape.set(vertices);
				    FixtureDef fixture = new FixtureDef();
				    fixture.friction = 0.3f;
				    fixture.shape = shape;
				    body.createFixture(fixture);
				    shape.dispose();					
				}
			}
		}
	}

	@Override
	public void render() {
		preRenderStuff();
        
        // Entities update
		if(this.players.size() > 0) {
	        mouvementCamera();
	        this.camera.update();
			batch.begin();
			playerUpdate();
//	        this.hud.draw(this.players.get(0));
	        batch.end();
		}
	}

	private void playerUpdate() {
		int number = 0;
		for(Player player : this.players) {
			player.update(this);
			this.lightPlayers.get(number).setPosition(player.getX() + (player.WIDTH / 2), player.getY() + (player.HEIGHT / 2));
		    spellUpdate(player);
		    ennemyUpdate(number, player);
		    batch.draw(this.playersSprite.get(number).getTextureRegion(player.current_anim), player.getX(), player.getY());
		    batch.flush();
		    number++;
		}
	}

	private void ennemyUpdate(int number, Player player) {
		for(int i = 0; i < this.enemies.size(); i++) {
			if(this.enemies.get(i).isAlive()) {
				this.enemies.get(i).update(this);
				if(this.enemies.get(i).isInLight(lightPlayers.get(number)))
					batch.draw(this.enemiesSprite.get(i).getTextureRegion(this.enemies.get(i).current_anim), this.enemies.get(i).getX(), this.enemies.get(i).getY());
			}
			else {
				player.getStats().exp += this.enemies.get(i).getStats().exp;
				enemies.remove(this.enemies.get(i));
				break;
			}
		}
	}

	private void spellUpdate(Player player) {
		for(int i = 0; i < player.getSpell().size(); i++) {
			Spell s = player.getSpell().get(i);
			    if(s.getalive()) {
			    	s.update(this);
			    	if(s.getalive()) {
			    		batch.draw(s.getTextureRegion(s.currentAnim), s.getX(), s.getY());
			    	}
			    }
			    else {
			    	player.removeElements(s);
			    	i--;
			    }
		}
	}

	@Override
	public void multiplayerRender() {
		preRenderStuff();
		int number = 0;
		if(this.players.size() > 0) {
	        mouvementCamera();
	        this.camera.update();
			batch.begin();
	        for(Player player : this.players) {
	        	this.lightPlayers.get(number).setPosition(player.getX() + (player.WIDTH / 2), player.getY() + (player.HEIGHT / 2));
			    for(int i = 0; i < player.getSpell().size(); i++) {
			    	Spell s = player.getSpell().get(i);
			    	batch.draw(s.getTextureRegion(s.currentAnim), s.getX(), s.getY());
			    }
			    for(Enemy e : enemies) {
		    		if(e.isInLight(lightPlayers.get(number)))
		    			batch.draw(this.enemiesSprite.get(number).getTextureRegion(e.current_anim), e.getX(), e.getY());
			    }
			    batch.draw(this.playersSprite.get(number).getTextureRegion(player.current_anim), player.getX(), player.getY());
			    batch.flush();
	        }
	        this.hud.draw(this.players.get(0));
	        batch.end();			
		}
	}
	
    @Override
	public void addPlayer(Player p, boolean isServer) {
    	this.players.add(p);
    	if(!isServer) { 
			this.playersSprite.add(new Sprite(p.sprite, p.WIDTH, p.HEIGHT, p.NOMBRE_IMAGE, p.NOMBRE_LIGNE, p.DUREE_ANIM));
			this.lightPlayers.add(new PointLight(this.handler, 400, new Color(1f, 1f, 0f, 0.7f), 400, p.getX(), p.getY()));
    	}
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
		handler.dispose();
		for(Player player : this.players) {
	    	for(Spell s : player.getSpell())
	    		s.getTexture().dispose();
		}
		for(Enemy enemy : this.enemies) {
			if(enemy.getSpell() != null) {
	    		for(Spell s : enemy.getSpell()) {
	    			s.getTexture().dispose();
	    		}
			}
		}
		map.dispose();
		renderer.dispose();
		hud.dispose();
		players.clear();
		playersSprite.clear();
		enemies.clear();
		enemiesSprite.clear();
	}
}
