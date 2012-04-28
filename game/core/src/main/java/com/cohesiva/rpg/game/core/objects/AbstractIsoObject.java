package com.cohesiva.rpg.game.core.objects;

import java.util.List;

import com.cohesiva.rpg.game.core.MapCoordinates;

import pythagoras.i.Point;

public abstract class AbstractIsoObject {

	protected MapCoordinates coordinates;

	private boolean blocking;
	
	private static final Point POINT_ZERO = new Point();

	public MapCoordinates getCoordinates() {
		return coordinates;
	}

	public void setCoordinates(MapCoordinates coordinates) {
		this.coordinates = coordinates;
	}

	public abstract List<TileDefinition> getTileDefinitions();

	public boolean isBlocking() {
		return blocking;
	}

	protected void setBlocking(boolean blocking) {
		this.blocking = blocking;
	}
	
	public Point getShiftAgainstCenterOfCurrentLocation()  {
		return POINT_ZERO;
	}

}
