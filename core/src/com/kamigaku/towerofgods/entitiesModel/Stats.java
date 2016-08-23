package com.kamigaku.towerofgods.entitiesModel;

import java.io.Serializable;

public class Stats implements Serializable {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = -2384203790428071144L;
	public int hp, maxHp;
	public int rangeAttack;
	public int sizeWeapon;
	public int damage;
	public int mana, maxMana;
	public float regenHp;
	public float regenMana;
	public float speed;
	public int rangeDetection;
	public int exp;
	

	// Attention à chaque ajout il faut rajouter les élèments dans le fichier statsMob.xml et XMLReader !
	
	public Stats(int hp, int rangeAttack, int sizeWeapon, int damage, int mana, float regenHp, float regenMana, float speed, int exp) {
		this.hp = this.maxHp = hp;
		this.rangeAttack = rangeAttack;
		this.sizeWeapon = sizeWeapon;
		this.damage = damage;
		this.mana = this.maxMana = mana;
		this.regenHp = regenHp;
		this.regenMana = regenMana;
		this.speed = speed;
		this.exp = exp;
	}
	
	public void update(boolean inCombat) {
		if(!inCombat) {
			this.hp += regenHp;
			if(this.hp > maxHp)
				this.hp = maxHp;
			this.mana += regenMana;
			if(this.mana > maxMana)
				this.mana = maxMana;
		}
	}
	
	public boolean canLaunchSpell(int manaCost) {
		if(this.mana - manaCost >= 0) {
			this.mana -= manaCost;
			if(mana < 0) {
				mana = 0;
			}
			return true;	
		}
		return false;
	}

}