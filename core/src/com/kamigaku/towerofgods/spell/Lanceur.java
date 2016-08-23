package com.kamigaku.towerofgods.spell;

import java.io.Serializable;
import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.kamigaku.towerofgods.entitiesModel.Player;

public class Lanceur implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private ArrayList<Integer> keysAssign;
	private ArrayList<String> spells;
	private ArrayList<Cooldown> cooldown;
	
	public Lanceur() {
		this.keysAssign = new ArrayList<Integer>();
		this.spells = new ArrayList<String>();
		this.cooldown = new ArrayList<Cooldown>();
	}
	
	public void update(Player player, OrthographicCamera camera) {		
		isKeyPressed(player, camera);
	}
			
	public void addSpells(int key, String spell, float cd) {
		this.keysAssign.add(key);
		this.spells.add(spell);
		this.cooldown.add(new Cooldown(cd));
	}
	
	public void isKeyPressed(Player player, OrthographicCamera camera) {
		int i = 0;
		for(Cooldown cd : this.cooldown) {
			cd.update();
		}
		for(int k : this.keysAssign) {
			if((Gdx.input.isKeyPressed(k) || (Gdx.input.isButtonPressed(k) && k < 3)) && this.cooldown.get(i).getendCast()) {
				
				Vector2 centerPosition = new Vector2(player.getX() + (player.collisionWidth / 2), player.getY() + (player.collisionHeight / 2));
			    Vector3 worldCoordinates = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
			    camera.unproject(worldCoordinates);
			    Vector2 mouseCursor = new Vector2(worldCoordinates.x, worldCoordinates.y);
			    mouseCursor.sub(centerPosition);
			    
				Spell s = SpellFactory.buildSpell(this.spells.get(i), player, mouseCursor);
				if(s != null) {
					player.addSortLance(s);
					this.cooldown.get(i).setendCast(false);
				}
			}
			i++;
		}
	}

	public ArrayList<Integer> getKeysAssign() {
		return keysAssign;
	}

	public ArrayList<String> getSpells() {
		return spells;
	}

	public ArrayList<Cooldown> getCooldown() {
		return cooldown;
	}	
	
	public void setEverything(ArrayList<Cooldown> cooldown, ArrayList<String> spells, ArrayList<Integer> keysAssign) {
		this.cooldown = cooldown;
		this.spells = spells;
		this.keysAssign = keysAssign;
	}
}
