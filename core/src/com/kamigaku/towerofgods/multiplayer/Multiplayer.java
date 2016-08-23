package com.kamigaku.towerofgods.multiplayer;

import com.badlogic.gdx.ApplicationListener;
import com.kamigaku.towerofgods.GameLauncher;
import com.kamigaku.towerofgods.screen.MultiplayerInterface;

public class Multiplayer implements ApplicationListener {
	
	private Server server;
	private PlayerConnected player;
	public static String ipAdresse;
	public static int port;
	public static boolean isHost;

	@Override
	public void create() {
		GameLauncher.online = true;
		if(Multiplayer.isHost) {
			this.server = new Server(Multiplayer.ipAdresse, Multiplayer.port);
			this.server.start();
		}
		this.player = new PlayerConnected(Multiplayer.ipAdresse, Multiplayer.port);
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void render() {
		if(this.player.actif)
			this.player.update();
		else {
			if(Multiplayer.isHost) {
				this.server.closeServer();
				this.server.interrupt();
			}
			GameLauncher.nextGame = new MultiplayerInterface();
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
		this.server.closeServer();
	}
	
	

}
