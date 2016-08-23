package com.kamigaku.towerofgods.entitiesModel;


public class StatsPlayer extends Stats {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8667883869729962653L;

	public StatsPlayer(int hp, int rangeAttack, int sizeWeapon, int damage, int mana,
			float regenHp, float regenMana, float speed) {
		super(hp, rangeAttack, sizeWeapon, damage, mana, regenHp, regenMana, speed, 0);
	}
}
