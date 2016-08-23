package com.kamigaku.towerofgods.hud;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.kamigaku.towerofgods.entitiesModel.Player;

public class HUD {
	
	private Stage stage;
	private Skin uiSkin;
	private TextureAtlas atlas;
	private BitmapFont font;
	private NinePatch empty, full, fullMana, emptyExp, fullExp;
	private Table table;
	
	private static int scaleXHp = 10;
	private static int scaleXExp = 30;
	
	public HUD(SpriteBatch batch) {
		this.stage = new Stage();
		this.uiSkin = new Skin(Gdx.files.internal("hud/uiskin.json"));
		this.atlas = new TextureAtlas(Gdx.files.internal("hud/uiskin.atlas"));
		this.font = new BitmapFont(Gdx.files.internal("hud/futura_white.fnt"));
		
		this.table = new Table(this.uiSkin);
		table.setFillParent(true);
		stage.addActor(table);
		
		Texture emptyT = new Texture(Gdx.files.internal("hud/empty2.png"));
		Texture emptyE = new Texture(Gdx.files.internal("hud/exp_bar.png"));
		
		Texture fullT = new Texture(Gdx.files.internal("hud/full2.png"));
		Texture fullM = new Texture(Gdx.files.internal("hud/full_mana.png"));
		Texture fullE = new Texture(Gdx.files.internal("hud/exp_full.png"));
		
		this.empty = new NinePatch(new TextureRegion(emptyT,24,24),7,7,7,7);
		this.emptyExp = new NinePatch(new TextureRegion(emptyE, 24, 16), 4, 4, 4, 4);
		this.full = new NinePatch(new TextureRegion(fullT,24,24),7,7,7,7);
		this.fullMana = new NinePatch(new TextureRegion(fullM,24,24),7,7,7,7);
		this.fullExp = new NinePatch(new TextureRegion(fullE, 24, 16), 4, 4, 4, 4);	
		
		this.table.bottom().left();
		
		//Hp bar
		Group hpGroup = new Group();
		Image img1 = new Image(this.empty);
		Image img2 = new Image(this.full);
		img1.setScale(scaleXHp, 1.5f);
		img2.setScale(scaleXHp, 1.48f);
		hpGroup.addActor(img1);
		hpGroup.addActor(img2);
		
		//Mana bar
		Group manaGroup = new Group();
		img1 = new Image(this.empty);
		img2 = new Image(this.fullMana);
		img1.setScale(scaleXHp, 1.5f);
		img2.setScale(scaleXHp, 1.48f);
		manaGroup.addActor(img1);
		manaGroup.addActor(img2);
		
		//Exp bar
		Group expGroup = new Group();
		img1 = new Image(this.emptyExp);
		img2 = new Image(this.fullExp);
		img1.setScale(scaleXExp, 0.7f);
		img2.setScale(scaleXExp, 0.7f);
		expGroup.addActor(img1);
		expGroup.addActor(img2);
		
		//Assemblage
		this.table.add(hpGroup).padLeft(20).padBottom(55);
		this.table.row();
		this.table.add(manaGroup).padLeft(20).padBottom(25);
		this.table.row();
		this.table.add(expGroup).padLeft(20).padBottom(5);
		
	}
	
	public void draw(Player p) {
	
		float hp = ((float)p.getStats().hp/(float)p.getStats().maxHp) * scaleXHp;
		float mana = ((float)p.getStats().mana/(float)p.getStats().maxMana) * scaleXHp;
		float exp = (((float)p.getStats().exp) / 60f) * scaleXExp;
		
		setValue(((Group)(this.table.getChildren().get(0))).getChildren().get(1), hp);
		setValue(((Group)(this.table.getChildren().get(1))).getChildren().get(1), mana);
		setValue(((Group)(this.table.getChildren().get(2))).getChildren().get(1), exp);

		this.stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
		this.stage.draw();
	}
	
	public void setValue(Actor actor, float value) {
		if(value > 0) {
			if(!actor.isVisible())
				actor.setVisible(true);
			actor.setScaleX(value);
		}
		else {
			actor.setVisible(false);
		}
	}
	
	public void dispose() {
		this.atlas.dispose();
		this.empty.getTexture().dispose();
		this.emptyExp.getTexture().dispose();
		this.font.dispose();
		this.full.getTexture().dispose();
		this.fullExp.getTexture().dispose();
		this.fullMana.getTexture().dispose();
		this.stage.dispose();
		this.uiSkin.dispose();
	}

}