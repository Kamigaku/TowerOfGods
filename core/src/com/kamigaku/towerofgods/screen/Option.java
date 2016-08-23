package com.kamigaku.towerofgods.screen;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.kamigaku.towerofgods.GameLauncher;

public class Option implements ApplicationListener{

	private String[] options = {
			"Synchro verticale",
			"Plein écran",
			"Résolution",
			"Valider",
			"Annuler"
	};
	private String[][] choix = {
			{"Off", "On"},
			{"Off", "On"}
	};
	private int[] resoWidth = {
		800, 1024, 1680	
	};
	private int[] resoHeight = {
		600, 768, 1050	
	};
	
	private int currentChoice = 0;
	private int[] currentMenuChoice = {
		0, 0, 0	
	};
	
	protected SpriteBatch batch;
	private BitmapFont activeFont;
	private BitmapFont defaultFont;
	
	
	@Override
	public void create() {
		batch = new SpriteBatch();	
		defaultFont = new BitmapFont();
		defaultFont.setColor(new com.badlogic.gdx.graphics.Color(255, 255, 255, 1));
		activeFont = new BitmapFont();
		activeFont.setColor(new com.badlogic.gdx.graphics.Color(255, 0, 0, 1));
	}

	@Override
	public void resize(int width, int height) {
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
						currentChoice = options.length-1;
				}
				if (keycode == Keys.DPAD_LEFT) {
					currentMenuChoice[currentChoice]--;
					if(currentChoice != 2) {
						if(currentMenuChoice[currentChoice] == -1)
							currentMenuChoice[currentChoice] = 1;
					}
					else {
						if(currentMenuChoice[currentChoice] == -1)
							currentMenuChoice[currentChoice] = resoWidth.length-1;						
					}
				}
				if (keycode == Keys.DPAD_RIGHT) {
					currentMenuChoice[currentChoice]++;
					if(currentChoice != 2) {
						if(currentMenuChoice[currentChoice] == 2)
							currentMenuChoice[currentChoice] = 0;
					}
					else {
						if(currentMenuChoice[currentChoice] == resoWidth.length)
							currentMenuChoice[currentChoice] = 0;						
					}
				}
				if (keycode == Keys.ENTER) {
					if(currentChoice == options.length-1) {
						GameLauncher.nextGame = new Menu();
					}
					if(currentChoice == options.length-2) {
						boolean vsync = false;
						boolean fullScreen = false;
						if(currentMenuChoice[0] == 1)
							vsync = true;
						if(currentMenuChoice[1] == 1)
							fullScreen = true;
						Gdx.graphics.setVSync(vsync);
						Gdx.graphics.setDisplayMode(resoWidth[currentMenuChoice[2]], resoHeight[currentMenuChoice[2]], fullScreen);
					}
				}
				return false;
			}
		});
		batch.begin();
		for(int i = 0; i < options.length; i++) {
			if(currentChoice == i)
				activeFont.draw(batch, options[i], (Gdx.graphics.getWidth() / 2) - 250, (Gdx.graphics.getHeight() / 2) - (50 * i));
			else 
				defaultFont.draw(batch, options[i], (Gdx.graphics.getWidth() / 2) - 250, (Gdx.graphics.getHeight() / 2) - (50 * i));
		}
		for(int j = 0; j < currentMenuChoice.length; j++) {
			if(j < 2) {
				if(currentChoice == j)
					activeFont.draw(batch, choix[j][currentMenuChoice[j]], (Gdx.graphics.getWidth() / 2) - 50, 
							(Gdx.graphics.getHeight() / 2) - (50 * j));
				else 
					defaultFont.draw(batch, choix[j][currentMenuChoice[j]], (Gdx.graphics.getWidth() / 2) - 50, 
							(Gdx.graphics.getHeight() / 2) - (50 * j));
			}
			else {
				if(currentChoice == j)
					activeFont.draw(batch, "" + resoWidth[currentMenuChoice[j]] + " x " + + resoHeight[currentMenuChoice[j]], 
							(Gdx.graphics.getWidth() / 2) - 50,  (Gdx.graphics.getHeight() / 2) - (50 * j));
				else 
					defaultFont.draw(batch, "" + resoWidth[currentMenuChoice[j]] + " x " + + resoHeight[currentMenuChoice[j]], 
							(Gdx.graphics.getWidth() / 2) - 50, (Gdx.graphics.getHeight() / 2) - (50 * j));
			}
				
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
	}

}