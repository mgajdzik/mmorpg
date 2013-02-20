package com.cohesiva.rpg.game.core;

public class TemporalCoordinates extends MapCoordinates {

	private Turn turn;

	public Turn getTurn() {
		return turn;
	}

	public void setTurn(Turn turn) {
		this.turn = turn;
	}
}
