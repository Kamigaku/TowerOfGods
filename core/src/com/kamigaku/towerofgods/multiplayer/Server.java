package com.kamigaku.towerofgods.multiplayer;


import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Vector;

import com.kamigaku.towerofgods.GameLauncher;
import com.kamigaku.towerofgods.entitiesModel.Enemy;
import com.kamigaku.towerofgods.level.Level;
import com.kamigaku.towerofgods.level.levels.Dungeon;
import com.kamigaku.towerofgods.log.Log;
import com.kamigaku.towerofgods.screen.MultiplayerInterface;


public class Server extends Thread {

	private ServerSocket serverSocket;
	private Vector<GestionPlayer> gestionPlayers;
	private Level level;
	private int nbrPlayers;
	private ArrayList<String> datasToSend;
	
	public Server(String ipAdresse, int port) {
		try {
			this.serverSocket = new ServerSocket(port, 5, InetAddress.getByName(ipAdresse));
			this.gestionPlayers = new Vector<GestionPlayer>();
			this.level = new Dungeon();
			this.level.create();
			
			this.datasToSend = new ArrayList<String>();
			
			this.nbrPlayers = 0;
			System.out.println("Serveur lancé à l'adresse " + this.serverSocket.getInetAddress().getHostAddress());
			this.listenConnection();
		} catch (IOException e) {
			Log.writeLog("serverCreation", "[ERREUR] Erreur lors de la création du serveur : " + e.getMessage());
			MultiplayerInterface.messageErreur = "Erreur de lors de la création du serveur.";
			GameLauncher.nextGame = new MultiplayerInterface();
		}		
	}
		
	public void listenConnection() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				while(!serverSocket.isClosed()) {
					try {
						Socket s = serverSocket.accept();
						System.out.println("[SERVER] Player connected.");
						ObjectOutputStream objOut = new ObjectOutputStream(s.getOutputStream());
						objOut.writeObject(level.mapGenerate);
						objOut.writeObject(level.enemies);
						objOut.reset();
						gestionPlayers.add(new GestionPlayer(s, objOut));
						objOut.writeObject(level.players);
						nbrPlayers += 1;
						level.addPlayer(gestionPlayers.get(nbrPlayers - 1).getPlayer(), true);
						System.out.println("[SERVER] Player correctly added.");
					} catch (IOException e) {
						Log.writeLog("clientListening", "[ERREUR] Erreur lors de l'écoute de la connexion d'un joueur : " + e.getMessage());
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

	@Override
	public void run() {
		while(!this.serverSocket.isClosed()) {
			if(this.gestionPlayers.size() > 0 && this.level.players.size() > 0) {
				int index = -1;
				boolean send = false;
				
				// ================================================================================
				// INPUT PROCESS : Met à jour la position du joueur quand le client envoit un input
				// ================================================================================
				synchronized(this.level.players) {
					if(this.level.players.size() == this.gestionPlayers.size()) {
						this.datasToSend.clear();
						for(int i = 0; i < this.gestionPlayers.size(); i++) {
							if(this.gestionPlayers.get(i).isActif()) {
								if(this.gestionPlayers.get(i).toUpdate) {
									this.level.players.get(i).serverUpdate(this.level);
									this.datasToSend.add("datasPlayer|" + this.level.players.get(i).getX() + "|" + this.level.players.get(i).getY() + "|" + this.level.players.get(i).getGeneratedNumber());
									this.gestionPlayers.get(i).toUpdate = false;
									if(this.level.players.get(i).getUpdated())
										send = true;
								}
							}
							else {
								index = i;
							}
						}
					}
				}
				if(index > -1) {
					this.datasToSend.add("datasRemove|" + this.gestionPlayers.get(index).getPlayer().getGeneratedNumber());
					System.out.println("ICI ");
					this.gestionPlayers.remove(index);
					this.level.players.remove(index);
					this.nbrPlayers--;
					send = true;
				}
				
				// ===================================================================================
				// ENNEMY UPDATE : Update des données de l'ennemis
				// ===================================================================================
				int i = 0;
			    for(Enemy e : this.level.enemies) {
			    	e.update(this.level);
			    	if(e.hasBeenUpdated)
			    		this.datasToSend.add("datasEnemy|" + e.getX() + "|" + e.getY() + "|" + i);
		    		i++;
			    }
				
				// ===================================================================================
				// DATA SEND : Envoi des données aux joueurs
				// ===================================================================================
				if(send) {
					synchronized(this.gestionPlayers) {
						for(GestionPlayer gp : this.gestionPlayers) {
							gp.sendDatas(this.datasToSend);
						}
					}
				}
				// ===================================================================================
			}
			
			// Ici le server dort pendant 16 millisecondes, il ne se met à jour que 60 fois par seconde.
			try {
				Thread.sleep(16);
			} catch (InterruptedException e) {
				Log.writeLog("serverUpdate", "[ERREUR] Erreur grave lors de la pause du thread principale : " + e.getMessage());
				MultiplayerInterface.messageErreur = "Erreur lors de la mise à jour du serveur.";
				GameLauncher.nextGame = new MultiplayerInterface();
			}
		}
		try {
			this.finalize();
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	public Level getLevel() {
		return this.level;
	}
	
	public void closeServer() {
		try {
			this.gestionPlayers.clear();
			this.datasToSend.clear();
			this.level.dispose();
			this.serverSocket.close();
			System.out.println("Fermeture compléte du serveur");
		} catch (IOException e) {
			Log.writeLog("serverClosure", "[ERREUR] Erreur lors de la fermeture du serveur : " + e.getMessage());
		}
	}
}
