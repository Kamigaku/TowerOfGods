package com.kamigaku.towerofgods.screen;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.kamigaku.towerofgods.GameLauncher;
import com.kamigaku.towerofgods.multiplayer.Multiplayer;

public class MultiplayerInterface implements ApplicationListener {
	
	private Stage stage;
	private Skin uiSkin;
	private TextField ipServerField;
	private TextField portServerField;
	private TextField joinIpServerField;
	private TextField joinPortServerField;
	public static String messageErreur = "";

	@Override
	public void create() {
		this.stage = new Stage(new ScreenViewport());
		Gdx.input.setInputProcessor(stage);
		
		this.uiSkin = new Skin(Gdx.files.internal("hud/uiskin.json"));
		
		Table table = new Table(this.uiSkin);
		table.setFillParent(true);
		stage.addActor(table);
		
		Label createServLabel = new Label("Creer un serveur", this.uiSkin);
		Label servIpLabel = new Label("Adresse IP : ", this.uiSkin);
		Label portServ = new Label("Port du serveur : ", this.uiSkin);
		Label servIpLabel2 = new Label("Adresse IP : ", this.uiSkin);
		Label portServ2 = new Label("Port du serveur : ", this.uiSkin);
		Label joinServ = new Label("Rejoindre un serveur", this.uiSkin);
		
		Label erreurServ = new Label(MultiplayerInterface.messageErreur, this.uiSkin);
				
		this.ipServerField = new TextField("", this.uiSkin);
		this.portServerField = new TextField("", this.uiSkin);
		this.joinIpServerField = new TextField("", this.uiSkin);
		this.joinPortServerField = new TextField("", this.uiSkin);
				
		TextButton createServer = new TextButton("Creer serveur", this.uiSkin);
		createServer.addListener(new InputListener() {
			@Override
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				if(!ipServerField.getText().equals(""))
					Multiplayer.ipAdresse = ipServerField.getText();
				else 
					Multiplayer.ipAdresse = "127.0.0.1";
				if(!portServerField.getText().equals(""))
					Multiplayer.port = Integer.parseInt(portServerField.getText());
				else 
					Multiplayer.port = 2302;
                Multiplayer.isHost = true;
                GameLauncher.nextGame = new Multiplayer();
                return true;
			}
		});
		
		TextButton joinServer = new TextButton("Rejoindre le serveur", this.uiSkin);
		joinServer.addListener(new InputListener() {
			@Override
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				if(!ipServerField.getText().equals(""))
					Multiplayer.ipAdresse = joinIpServerField.getText();
				else 
					Multiplayer.ipAdresse = "127.0.0.1";
				if(!portServerField.getText().equals(""))
					Multiplayer.port = Integer.parseInt(joinPortServerField.getText());
				else 
					Multiplayer.port = 2302;
                Multiplayer.isHost = false;
                GameLauncher.nextGame = new Multiplayer();
                return true;
			}
		});
		
		table.add(createServLabel);
		table.row();
		table.add(servIpLabel);
		table.add(ipServerField);
		table.row();
		table.add(portServ);
		table.add(portServerField);
		table.row();
		table.add(createServer);
		table.row();
		table.add(joinServ);
		table.row();
		table.add(servIpLabel2);
		table.add(joinIpServerField);
		table.row();
		table.add(portServ2);
		table.add(joinPortServerField);
		table.row();
		table.add(joinServer);
		table.row();
		table.add(erreurServ);
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		this.stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
		this.stage.draw();
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
