package com.kamigaku.towerofgods.spell;

import com.badlogic.gdx.math.Vector2;
import com.kamigaku.towerofgods.entitiesModel.Player;
import com.kamigaku.towerofgods.spellcqc.Attack;
import com.kamigaku.towerofgods.spellrange.Explosion;
import com.kamigaku.towerofgods.spellrange.FireBall;

public class SpellFactory {

	public static Spell buildSpell(String spellName, Player p, Vector2 mouseCursor) {
		switch(spellName) {
			case "FireBall":
				if(p.stats.canLaunchSpell(FireBall.MANACOST)) {
					return new FireBall(p.getX() + (p.collisionWidth / 2), p.getY() + (p.collisionHeight / 2), 22, 22, 800, 5, mouseCursor);
				}
				else return null;
			case "Explosion":
				if(p.stats.canLaunchSpell(Explosion.MANACOST)) {
					if(SpellAOE.isInRange(p.getX(), p.getY(), mouseCursor, Explosion.RANGE)) {
						return new Explosion(p.getX() + (p.collisionWidth / 2) + mouseCursor.x, p.getY() + (p.collisionHeight / 2) + mouseCursor.y, p.getX(), p.getY());
					}
					else
						return null;
				}
				else return null;
			case "Attack":
				return new Attack(p.getX(), p.getY(), p.getStats().rangeAttack, p.getStats().sizeWeapon);
			default: return null;	
		}
	}
}
