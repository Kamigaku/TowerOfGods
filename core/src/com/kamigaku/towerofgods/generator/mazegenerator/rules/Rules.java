package com.kamigaku.towerofgods.generator.mazegenerator.rules;

import java.util.Arrays;


public class Rules {
	
	private String name;
	private String main;
	
	private String[] toLeft;
	private String[] toRight;
	private String[] toTop;
	private String[] toBot;
	
	public static final int TOLEFT = 0;
	public static final int TORIGHT = 1;
	public static final int TOTOP = 2;
	public static final int TOBOT = 3;
	
	public Rules(String main, String name, String[] toLeft, String[] toRight, String[] toTop, String[] toBot) {
		this.main = main;
		this.name = name;
		this.toLeft = toLeft;
		this.toRight = toRight;
		this.toTop = toTop;
		this.toBot = toBot;
	}
		
	public boolean isJoined(String join, int position) {
		switch(position) {
			case 0: //left
				if(Arrays.asList(this.toLeft).contains(join))
					return true;
				return false;
				
			case 1: //right
				if(Arrays.asList(this.toRight).contains(join))
					return true;				
				return false;
				
			case 2: //top
				if(Arrays.asList(this.toTop).contains(join))
					return true;
				return false;
			
			case 3: //bot 
				if(Arrays.asList(this.toBot).contains(join))
					return true;
				return false;
				
			default:
				return false;
		}
	}
	
	public String[] getToSomething(int element) {
		switch(element) {
			case TOLEFT:
				return this.toLeft;
			case TORIGHT:
				return this.toRight;
			case TOTOP:
				return this.toTop;
			case TOBOT:
				return this.toBot;
			default :
				return null;
		}
	}
	
	public boolean isTheGood(String good) {
		return (this.main == good);
	}
	
	public String getMain() {
		return this.main;
	}
	
	public String getName() {
		return this.name;
	}
	
	@Override
	public String toString() {
		String message = "";
		message = "L'élèment princiale est le " + this.main + ".\r\n" +
				"Il est lié au(x) élèment(s) suivant(s) : ";
		message += "\n Sur la gauche : ";
		for(String element : this.toLeft) {
			message += element + ", ";
		}
		message += "\n Sur la droite : ";
		for(String element : this.toRight) {
			message += element + ", ";
		}
		message += "\n En haut : ";
		for(String element : this.toTop) {
			message += element + ", ";
		}
		message += "\n En bas : ";
		for(String element : this.toBot) {
			message += element + ", ";
		}
		
		return message;
	}
	

}
