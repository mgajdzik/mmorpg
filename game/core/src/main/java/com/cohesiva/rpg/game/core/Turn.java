package com.cohesiva.rpg.game.core;

public class Turn {

	private long turnNumber;

	private double turnTimestamp;

	public long getTurnNumber() {
		return turnNumber;
	}

	public void setTurnNumber(long turnNumber) {
		this.turnNumber = turnNumber;
	}

	public double getTurnTimestamp() {
		return turnTimestamp;
	}

	public void setTurnTimestamp(double turnTimestamp) {
		this.turnTimestamp = turnTimestamp;
	}
	
	public static long getTimeDurationMilis() {
		return 20;
	}

}
