package com.kamigaku.towerofgods.multiplayer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import com.kamigaku.towerofgods.entitiesModel.Player;
import com.kamigaku.towerofgods.log.Log;

public class GestionPlayer extends Thread {

	
	// Thread ??
	private Socket socket;
	public Player player;
	private ObjectOutputStream objectOut;
	private ObjectInputStream objectIn;
	private boolean actif = true;
	public boolean toUpdate = true;
	public boolean isNew = true;
	
	public GestionPlayer(Socket socket, ObjectOutputStream objOut) {
		try {
			this.socket = socket;
			this.objectOut = objOut;
			this.objectIn = new ObjectInputStream(this.socket.getInputStream());
			this.player = (Player) objectIn.readObject();
			System.out.println("[SERVER] Player received with the ID : " + this.player.getGeneratedNumber() + ".");
			this.start();
		} catch (IOException e) {
			Log.writeLog("gestionPlayer", "[ERREUR] Erreur lors de la connexion d'un joueur au serveur : " + e.getMessage());
			System.out.println("[SERVER] Client disconnected.");
		} catch (ClassNotFoundException e) {
			Log.writeLog("gestionPlayer", "[ERREUR] Erreur lors de la réception des données : " + e.getMessage());
		}
	}

	@Override
	public void run() {
		while(!this.socket.isClosed()) {
				try {
					String inputs = (String) objectIn.readUTF();
					this.player.setInputs(inputs);
					this.toUpdate = true;
				} catch (IOException e) {
					try {
						this.socket.close();
						this.actif = false;
					} catch (IOException e1) {
						Log.writeLog("gestionPlayer", "[ERREUR] Erreur lors de la fermeture du socket : " + e.getMessage());
					}
				}
		}
		System.out.println("Socket closed");
	}
	
	public Socket getSocket() {
		return this.socket;
	}
	
	public void resetObjOut() {
		try {
			this.objectOut.reset();
		} catch (IOException e) {
			Log.writeLog("gestionPlayer", "[ERREUR] Erreur lors du reset de l'object output : " + e.getMessage());
			this.actif = false;
		}
	}
	
	public void sendDatas(ArrayList<String> datasToSend) {
		try {
			for(String dataToSend : datasToSend) {
				this.objectOut.writeUTF(dataToSend);
				this.objectOut.reset();
			}
		} catch (IOException e) {
			Log.writeLog("gestionPlayer", "[ERREUR] Erreur lors de l'envoi du joueur : " + e.getMessage());
			this.actif = false;
		}
	}

	public boolean isActif() {
		return this.actif;
	}

	public Player getPlayer() {
		return this.player;
	}
	
	public synchronized ObjectOutputStream getObjectOutput() {
		return this.objectOut;
	}
	
}
