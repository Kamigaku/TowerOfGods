package com.kamigaku.towerofgods;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.kamigaku.towerofgods.screen.Menu;

public class GameLauncher extends ApplicationAdapter {
	public static ApplicationListener game;
	public static ApplicationListener nextGame;
	// Changer plus tard d'ApplicationListener à Class
	
	public static String title = "Tower of XXXX";
	public static boolean fullScreen = false;
	public static boolean vSync = false;
	public static boolean online = false;
	public static int width = 1280;
	public static int height = 720;
	public static String PATH_FILE = "";
	public static final boolean PRODUCTION = true;
	
	@Override
	public void create() {
		Gdx.graphics.setTitle(title);
		GameLauncher.game = new Menu();
		GameLauncher.nextGame = new Menu();
		GameLauncher.PATH_FILE = this.getClass().getClassLoader().getResource("").getPath();
		if(GameLauncher.PRODUCTION) {
			GameLauncher.PATH_FILE += "external/";
		}
		game.create();
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void render() {
		if(game.getClass() != nextGame.getClass()) {
			GameLauncher.game = GameLauncher.nextGame;
			GameLauncher.game.create();
		}
		GameLauncher.game.render();
		
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {	
	}

}
