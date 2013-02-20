package com.cohesiva.rpg.game.core;

import pythagoras.i.Point;

public class MapPoint {

	private Point coordinates;
	private Point coordinatesWithinTile;

	public Point getCoordinates() {
		return coordinates;
	}

	public void setCoordinates(Point coordinates) {
		this.coordinates = coordinates;
	}

	public Point getCoordinatesWithinTile() {
		return coordinatesWithinTile;
	}

	public void setCoordinatesWithinTile(Point coordinatesWithinTile) {
		this.coordinatesWithinTile = coordinatesWithinTile;
	}

}
