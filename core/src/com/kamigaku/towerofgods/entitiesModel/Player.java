package com.kamigaku.towerofgods.entitiesModel;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.kamigaku.towerofgods.level.Level;
import com.kamigaku.towerofgods.spell.Lanceur;
import com.kamigaku.towerofgods.spell.Spell;
import com.kamigaku.towerofgods.spellcqc.Attack;
import com.kamigaku.towerofgods.spellrange.Explosion;
import com.kamigaku.towerofgods.spellrange.FireBall;
import com.kamigaku.towerofgods.tools.Ticker;

public class Player extends Entity {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8302886864545528011L;
	public static final int IDLE = 4;
	public static final int TOP = 0;
	public static final int LEFT = 3;
	public static final int RIGHT = 2;
	public static final int BOT = 1;
	public final int WIDTH = 32; // largeur de l'image
	public final int HEIGHT = 32; // longueur de l'image
	public final int NOMBRE_IMAGE = 3;
	public final int NOMBRE_LIGNE = 5;
	public final float DUREE_ANIM = 0.15f;
	public boolean inCombat;
	private Lanceur lanceur;
	private int generatedNumber;
	private boolean[] keyInput;
	private boolean updated;
		
	public Player(float x, float y) {
		this.generatedNumber = (int) (Math.random() * Integer.MAX_VALUE);
		basicPlayerCreation(x, y);
		Gdx.input.setInputProcessor(new InputProcessor() {
			
			@Override
			public boolean touchUp(int screenX, int screenY, int pointer, int button) {
				return false;
			}
			
			@Override
			public boolean touchDragged(int screenX, int screenY, int pointer) {
				return false;
			}
			
			@Override
			public boolean touchDown(int screenX, int screenY, int pointer, int button) {
				return false;
			}
			
			@Override
			public boolean scrolled(int amount) {
				return false;
			}
			
			@Override
			public boolean mouseMoved(int screenX, int screenY) {
				return false;
			}
			
			@Override
			public boolean keyUp(int keycode) {
				if(keycode < 255 && keycode >= 0) {
						keyInput[keycode] = false;
				}
				return false;
			}
			
			@Override
			public boolean keyTyped(char character) {
				return false;
			}
			
			@Override
			public boolean keyDown(int keycode) {
				if(keycode < 255 && keycode >= 0) {
						keyInput[keycode] = true;
				}
				return false;
			}
		});		
	}
	
	public Player(float x, float y, int generatedNumber) {
		this.generatedNumber = generatedNumber;
		basicPlayerCreation(x, y);
	}

	public void basicPlayerCreation(float x, float y) {
		this.inCombat = false;
		this.updated = false;
		this.x = x;
		this.y = y;
		spell = new ArrayList<Spell>();
		this.lanceur = new Lanceur();
		this.sprite = "data/perso.png";
		this.current_anim = 0;
		
		// A changer peut-être plus tard
		this.collisionWidth = 22;
		this.collisionHeight = 22;
		//
		
		this.diffCollisionHeight = (HEIGHT / 2) - (collisionHeight / 2);
		this.diffCollisionWidth = (WIDTH / 2) - (collisionWidth / 2);
		this.stats = new StatsPlayer(1000, 80, 20, 2, 10, 1, 0, 2f);
		this.keyInput = new boolean[255];
		this.statTicker = new Ticker(1000);

		// Lanceur
		this.getLanceur().addSpells(Keys.E, "FireBall", FireBall.cd);
		this.getLanceur().addSpells(Buttons.LEFT, "Attack", Attack.cd);
		this.getLanceur().addSpells(Keys.R, "Explosion", Explosion.cd);
	}
	
	@Override
	public void update(Level map) {				
		float oldY = this.y;
		float oldX = this.x;
		
		mouvementPlayer(map, oldY, oldX);
		
		if(this.x == oldX && this.y == oldY) 
			this.current_anim = IDLE;
		if(this.statTicker.hasTicked()) {
			this.stats.update(inCombat);
			this.statTicker.setTicked(false);
		}
		this.getLanceur().update(this, map.getCamera());
	}

	public void serverUpdate(Level map) {
		float oldY = this.y;
		float oldX = this.x;
		
		this.updated = true;
		
		mouvementPlayer(map, oldY, oldX);
		
		if(this.x == oldX && this.y == oldY) {
			this.updated = false;
		}
		
		this.stats.update(inCombat);
//		this.getLanceur().update(this);		
	}

	private void mouvementPlayer(Level map, float oldY, float oldX) {
		if(this.keyInput[Keys.Z]) {
			if(y+collisionHeight < map.mapHeight * map.tileHeight) {
				y += this.stats.speed;
			}
			this.current_anim = TOP;
		}
		if(this.keyInput[Keys.S]) {
			if(y >= 0)
				y -= this.stats.speed;
			this.current_anim = BOT;
		}
		if(this.keyInput[Keys.Q]) {
			if(x >= 0)
				x -= this.stats.speed;
			this.current_anim = LEFT;
		}
		if(this.keyInput[Keys.D]) {
			if(x+collisionWidth < map.mapWidth * map.tileWidth)
				x += this.stats.speed;
			this.current_anim = RIGHT;
		}
		
		if(gestion_collision(x, oldY, map)) {
			this.x = oldX;
		}
		if(gestion_collision(oldX, y, map)) {
			this.y = oldY;
		}
	}

	public boolean gestion_collision(float x, float y, Level map) {
		if(collision(x, y, map))
			return true;
		if(!map.getEnemy().isEmpty()) {
			for(Enemy e : map.getEnemy()) {
				if(collisionInEntity(
						x + this.diffCollisionWidth,
						y + this.diffCollisionHeight,
						e.x + e.diffCollisionWidth,
						e.y + e.diffCollisionHeight,
						e.collisionHeight,
						e.collisionWidth))
					return true;
			}
		}
		return false;
	}
	
	public void addExp(int value) {
		this.stats.exp += value;
	}
	
	public void addSortLance(Spell spell) {
		this.spell.add(spell);
	}
	
	public void resetInputs() {
		for(int i = 0; i < this.keyInput.length; i++) {
			if(this.keyInput[i])
				this.keyInput[i] = false;
		}
	}
    
	public void setInputs(String keyInputs) {
		String[] inputs = keyInputs.split("\\|");
		resetInputs();
		for(int i = 0; i < inputs.length; i++) {
			if(inputs[i] != "")
				this.keyInput[Integer.parseInt(inputs[i])] = true;
		}
	}

	public int getCurrentExp() {
		return this.stats.exp;
	}

	public Lanceur getLanceur() {
		return lanceur;
	}
	
	public int getGeneratedNumber() {
		return this.generatedNumber;
	}
	
	public boolean[] getInputs() {
		return this.keyInput;
	}
	
	public boolean getUpdated() {
		return this.updated;
	}
}
