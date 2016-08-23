package com.kamigaku.towerofgods.entitiesModel;

public class StatsEnemy extends Stats {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1487062315536586408L;

	public StatsEnemy(int hp, int rangeAttack, int sizeWeapon, int damage, int mana,
			float regenHp, float regenMana, float speed, int rangeDetection, int exp) {
		// Attention � chaque ajout il faut rajouter les �l�ments dans le fichier statsMob.xml et XMLReader !
		super(hp, rangeAttack, sizeWeapon, damage, mana, regenHp, regenMana, speed, exp);
		this.rangeDetection = rangeDetection;
	}

}
