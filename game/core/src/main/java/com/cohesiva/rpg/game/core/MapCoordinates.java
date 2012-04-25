package com.cohesiva.rpg.game.core;

import pythagoras.i.AbstractPoint;
import pythagoras.i.IPoint;
import pythagoras.i.Point;

public class MapCoordinates extends Point {

	public MapCoordinates() {
		super();
		// TODO Auto-generated constructor stub
	}

	public MapCoordinates(int x, int y) {
		super(x, y);
		// TODO Auto-generated constructor stub
	}

	public MapCoordinates(IPoint p) {
		super(p);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		AbstractPoint p = (AbstractPoint) obj;
		return x() == p.x() && y() == p.y();
	}

	@Override
	public int hashCode() {
		return x() ^ y();
	}

}
