package com.kamigaku.towerofgods.multiplayer;

import java.awt.Point;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.kamigaku.towerofgods.entitiesModel.Enemy;
import com.kamigaku.towerofgods.entitiesModel.Player;
import com.kamigaku.towerofgods.generator.dungeongenerator.map.Map;
import com.kamigaku.towerofgods.level.Level;
import com.kamigaku.towerofgods.level.levels.Dungeon;
import com.kamigaku.towerofgods.log.Log;

public class PlayerConnected {

	private Player player;
	private Socket socket;
	private ObjectInputStream objIn;
	private ObjectOutputStream objOut;
	private Level map;
	public boolean actif = true;
	
	// Datas
	private String datas;
	private boolean toUpdate = false;
	private String datasInputs = "";
	
	@SuppressWarnings("unchecked")
	public PlayerConnected(String ipAdresse, int port) {
		try {
			this.socket = new Socket(ipAdresse, port);
			this.socket.setTcpNoDelay(true);
			if(this.socket.isConnected()) {
				this.objOut = new ObjectOutputStream(this.socket.getOutputStream());
				this.objIn = new ObjectInputStream(this.socket.getInputStream());
				
				/*
				 * TODO
				 * Modifier la structure de génération du level, passer une factory, ici création unique d'un Dugeon
				 * format : this.map = LevelFactory.generate();
				 */
				
				Map m = (Map) objIn.readObject();
				this.map = new Dungeon(m);
				this.map.setEnemies((ArrayList<Enemy>) objIn.readObject());
				Point starting = this.map.getStartingPoint();
				this.player = new Player(starting.x, starting.y);
				this.map.addPlayer(this.player, false);
				objOut.writeObject(this.player);
				this.map.setPlayers((ArrayList<Player>) objIn.readObject());
				
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
							datasInputs = datasInputs.replace("" + keycode + "|", "");
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
							datasInputs += "" + keycode + "|";
						}
						return false;
					}
				});
				
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						while(!socket.isClosed()) {
							try {
								datas = (String) objIn.readUTF();
								toUpdate = true;
							} catch (IOException e) {
								Log.writeLog("playerConnected", "[ERREUR] Erreur lors de la recepetion des données : " + e.toString() + " || Données : " + datas);
								actif = false;
							}
						}
						try {
							this.finalize();
						} catch (Throwable e) {
							e.printStackTrace();
						}
					}
				}).start();
			}
		} catch (UnknownHostException e) {
			Log.writeLog("playerConnected", "[ERREUR] Erreur, hôte inconnu : " + e.getMessage());
			this.actif = false;
		} catch (IOException e) {
			Log.writeLog("playerConnected", "[ERREUR] Erreur lors de la réception / envoi des données : " + e.getMessage());
			this.actif = false;
		} catch (ClassNotFoundException e) {
			Log.writeLog("playerConnected", "[ERREUR] Erreur lors de la réception / envoi des données, classe incorrect : " + e.getMessage());
			this.actif = false;
		}
	}

	public void update() {
		if(!this.socket.isClosed()) {
			
			if(datasInputs != "" && datasInputs.length() > 0) {
				try {
					this.objOut.reset();
					this.objOut.writeUTF(datasInputs);
				} catch (IOException e) {
					Log.writeLog("playerConnected", "[ERREUR] Erreur lors de la réception / envoi des données : " + e.getMessage());
					this.actif = false;
				}
			}
			
			if(this.toUpdate) {
				toDoTask();
			}
			
			synchronized(this.map.players) {
				this.map.multiplayerRender();
			}
		}
		
		if(Gdx.input.isKeyPressed(Keys.ESCAPE)) {
			try {
				this.socket.close();
				this.actif = false;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void toDoTask() {
		String[] datasTo = this.datas.split("\\|", -1);
		
		// Test des différents passages, en fonction du message, l'execution est différente
		if(datasTo[0].equals("datasPlayer")) {
			this.map.addNewPlayers(datasTo);
		}
		else if(datasTo[0].equals("datasEnemy")) {
			
		}
		else if(datasTo[0].equals("datasSpell")) {
			
		}
		else if(datasTo[0].equals("datasRemove")) {
			this.map.removePlayer(datasTo);
		}
		System.out.println(this.datas);
		this.toUpdate = false;		
	}
}
