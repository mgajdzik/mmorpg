package com.cohesiva.rpg.game.core;

import java.util.HashMap;
import java.util.Map;

import pythagoras.i.IPoint;
import pythagoras.i.Point;

public enum Direction {
	LEFT_DOWN(-1, 1), LEFT(-1, 0), LEFT_UP(-1, -1), UP(0, -1), RIGHT_UP(1, -1), RIGHT(1, 0), RIGHT_DOWN(1, 1), DOWN(0, 1);

	private Point point;
	private static Map<Point, Direction> pointToDirection;

	private Direction(int dx, int dy) {
		point = new Point(dx, dy);
	}

	public int getInt() {
		return this.ordinal();
	}

	public static Direction getDirection(int dx, int dy) {
		if (pointToDirection == null) {
			pointToDirection = new HashMap<Point, Direction>();
			for (Direction direction : values()) {
				pointToDirection.put(direction.point, direction);
			}
		}
		return pointToDirection.get(new Point(dx, dy));
	}
	
	public IPoint getPoint() {
		return point;
	}
}
