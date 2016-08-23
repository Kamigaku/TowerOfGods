package com.kamigaku.towerofgods.entitiesModel;

import com.badlogic.gdx.math.Circle;
import com.kamigaku.towerofgods.level.Level;

public class StupidEnemy extends Enemy {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6905023825927381190L;

	public StupidEnemy(float x, float y, int collisionHeight, int collisionWidth) {
		super(x, y, collisionHeight, collisionWidth);
	}
	
	@Override 
	public void update(Level map) {
		for(Player player : map.players) {
			float playerX = player.x;
			float playerY = player.y;
			int collisionWidth = player.collisionWidth;
			int collisionHeight = player.collisionHeight;
			this.tempX = 0;
			this.tempY = 0;
			Circle circle = new Circle(this.x + (this.collisionWidth / 2), this.y + (this.collisionHeight / 2), 
							(float)this.stats.rangeDetection);
			if(circle.contains(playerX + (collisionWidth / 2), playerY + (collisionHeight / 2))) {
				int xPlayer = (int)playerX + (collisionWidth / 2) + player.diffCollisionWidth;
				int yPlayer = (int)playerY + (collisionHeight / 2) + player.diffCollisionHeight;
				int xEnnemy = (int)this.x + (this.collisionWidth / 2) + diffCollisionWidth;
				int yEnnemy = (int)this.y + (this.collisionHeight / 2) + diffCollisionHeight;
				double norme = Math.sqrt((Math.pow((yPlayer - yEnnemy), 2) + Math.pow((xPlayer - xEnnemy), 2)));
				double newX = (xPlayer - xEnnemy) / norme;
				double newY = (yPlayer - yEnnemy) / norme;
				int result = checkNormeInWall(norme, newX, newY, map);
				if(result == 1 || result == 2) {
					if(result == 1) {
						detect = true;
					}
					if(!detect)
						return;
				}		
				this.tempX = (float) newX;
				this.tempY = (float) newY;
				if(!collision(x, ((tempY * this.stats.speed) + y), map) && !collisionInEntity(
						x + this.diffCollisionWidth,
						((tempY * this.stats.speed) + y) + this.diffCollisionHeight, 
						player.x + player.diffCollisionWidth, 
						player.y + player.diffCollisionHeight,
						player.collisionHeight, 
						player.collisionWidth)) {
					setY((tempY * this.stats.speed) + y);
					this.hasBeenUpdated = true;
				}
				if(!collision(((tempX * this.stats.speed) + x), y, map) && !collisionInEntity(
						((tempX * this.stats.speed) + x) + this.diffCollisionWidth,
						y + this.diffCollisionHeight,
						player.x + player.diffCollisionWidth,
						player.y + player.diffCollisionHeight,
						player.collisionHeight,
						player.collisionWidth)) {
					setX((tempX * this.stats.speed) + x);
					this.hasBeenUpdated = true;
				}
				setAnim(tempX, tempY);
			}
			else {
				this.detect = false;
				regenHp();
			}
			setAnim(tempX, tempY);
		}
	}
}