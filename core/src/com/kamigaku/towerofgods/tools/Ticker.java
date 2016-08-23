package com.kamigaku.towerofgods.tools;

public class Ticker extends Thread {
	
	private boolean hasTicked;
	private long timerTick;
	
	public Ticker(long timerTick) {
		this.hasTicked = false;
		this.timerTick = timerTick;
		this.start();
	}

	@Override
	public void run() {
		while(true) {
			this.hasTicked = true;
			try {
				Thread.sleep(timerTick);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public boolean hasTicked() {
		return this.hasTicked;
	}

	public void setTicked(boolean hasTicked) {
		this.hasTicked = hasTicked;
	}

}
