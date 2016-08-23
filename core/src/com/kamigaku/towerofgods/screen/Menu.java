package com.kamigaku.towerofgods.screen;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.kamigaku.towerofgods.GameLauncher;
import com.kamigaku.towerofgods.level.levels.Dungeon;

public class Menu implements ApplicationListener {
	
	protected SpriteBatch batch;
	private int currentChoice = 0;
	private String[] options = {
		"Single player game",
		"Multiplayer game",
		"Options",
		"Quit"
	};
	private BitmapFont activeFont;
	private BitmapFont defaultFont;
	
	@Override
	public void resize(int width, int height) {
	}


	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
		activeFont.dispose();
		defaultFont.dispose();
		batch.dispose();
	}

	@Override
	public void create() {
		batch = new SpriteBatch();	
		defaultFont = new BitmapFont();
		defaultFont.setColor(new com.badlogic.gdx.graphics.Color(255, 255, 255, 1));
		activeFont = new BitmapFont();
		activeFont.setColor(new com.badlogic.gdx.graphics.Color(255, 0, 0, 1));
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
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
				return false;
			}
			
			@Override
			public boolean keyTyped(char character) {
				return false;
			}
			
			@Override
			public boolean keyDown(int keycode) {
				if(keycode == Keys.DPAD_DOWN) {
					currentChoice++;
					if(currentChoice == options.length)
						currentChoice = 0;					
				}
				if (keycode == Keys.DPAD_UP) {
					currentChoice--;
					if(currentChoice == -1)
						currentChoice = options.length - 1;
				}
				if(keycode == Keys.ENTER) {
					if(currentChoice == 0) {
						GameLauncher.online = false;
						GameLauncher.nextGame = new Dungeon();
					}
					if(currentChoice == 1) {
//						GameLauncher.online = true;
//						Multiplayer.ipAdresse = "127.0.0.1";
//						Multiplayer.port = 2302;
						GameLauncher.nextGame = new MultiplayerInterface();
					}
					if(currentChoice == 2)
						Gdx.app.exit();
				}
				return false;
			}
		});
		batch.begin();
		for(int i = 0; i < options.length; i++) {
			if(currentChoice == i)
				activeFont.draw(batch, options[i], (Gdx.graphics.getWidth() / 2) - 50, (Gdx.graphics.getHeight() / 2) - (50 * i));
			else
				defaultFont.draw(batch, options[i], (Gdx.graphics.getWidth() / 2) - 50, (Gdx.graphics.getHeight() / 2) - (50 * i));
		}
		batch.end();
	}

}