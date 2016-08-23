package com.kamigaku.towerofgods.entitiesModel;

import java.io.Serializable;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Sprite implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5970543743475961826L;
    private Animation animation[];
	private float temps;
	private int nombre_image;
	private int nombre_ligne;
    private int largeur_image;
    private int hauteur_image;
    private boolean animationEnd;
	
	public Sprite(String linkImage, int largeur_image, int hauteur_image, int nombre_image, int nombre_ligne, float vitesseExecution) {
		Texture textureAnimation = new Texture(Gdx.files.internal(linkImage));
		this.largeur_image = largeur_image;
		this.hauteur_image = hauteur_image;
		this.nombre_image = nombre_image;
		this.nombre_ligne = nombre_ligne;
		this.animationEnd = false;
		this.animation = new Animation[nombre_ligne];
		TextureRegion region = new TextureRegion(textureAnimation);
		TextureRegion tab[][] = region.split(this.largeur_image, this.hauteur_image);
		TextureRegion[][] tabAnim = new TextureRegion[nombre_ligne][nombre_image];
		for(int j = 0; j < this.nombre_ligne; j++) {
			for(int i = 0; i < this.nombre_image; i++) {
				tabAnim[j][i] = tab[j][i];
			}
		}
		for(int i = 0; i < this.nombre_ligne; i++) {
			animation[i] = new Animation(vitesseExecution, tabAnim[i]);
		}
		temps = 0.0f;
	}
	
	public TextureRegion getTextureRegion(int current_frame) {
		TextureRegion regionCourante = new TextureRegion();
		temps += Gdx.graphics.getDeltaTime(); 
        regionCourante = animation[current_frame].getKeyFrame(temps, true);  
        regionCourante.setRegion(regionCourante, 0, 0, this.largeur_image, this.hauteur_image);
        if(animation[current_frame].isAnimationFinished(temps))
        	this.animationEnd = true;
        return regionCourante;
	}
	
	public boolean animationFinish() {
		return this.animationEnd;
	}
	
	public int getLargeurImage() {
		return this.largeur_image;
	}
	
	public int getHauteurImage() {
		return this.hauteur_image;
	}
	
}