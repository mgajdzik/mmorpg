package com.cohesiva.rpg.game.core;

import java.util.ArrayList;
import java.util.List;

import playn.core.PlayN;
import pythagoras.i.Point;

public class IsoObject {

	private List<Tile> tiles = new ArrayList<Tile>();

	public final static double MOVING_TIME_MILIS = 500.0;
	public final static double HALF_OF_MOVING_TIME_MILIS = MOVING_TIME_MILIS / 2;

	protected MapCoordinates coordinates;
	protected MapCoordinates destination;
	protected MapCoordinates source;
	private double moveStartTimestamp;

	protected Direction direction = Direction.DOWN;

	private boolean blocking;

	public MapCoordinates getCoordinates() {
		double calculateMovementTime = calculateMovementTime();
		if (calculateMovementTime > MOVING_TIME_MILIS) {
			return coordinates;
		}
		if (coordinates.equals(destination)) {
			return coordinates;
		}
		if (calculateMovementTime > HALF_OF_MOVING_TIME_MILIS) {
			coordinates = destination;
			return coordinates;
		}
		return coordinates;
	}

	public void setCoordinates(MapCoordinates coordinates) {
		this.coordinates = coordinates;
	}

	public Point getShiftAgainstCenterOfCurrentTile() {
		double calculateMovementTime = calculateMovementTime();
		if (calculateMovementTime > MOVING_TIME_MILIS) {
			return new Point();
		}
		GuiMapCoordinates relativeToMapCoordinates = new GuiMapCoordinates();
		relativeToMapCoordinates.x = 0;
		relativeToMapCoordinates.y = 0;
		GuiMapCoordinates relativeToGuiMapCoordinates = null;
		if (calculateMovementTime > HALF_OF_MOVING_TIME_MILIS) {
			relativeToMapCoordinates.setMapCoordinates(source);
			relativeToGuiMapCoordinates = GuiMapCoordinates.createRelativeToGuiMapCoordinates(destination, relativeToMapCoordinates);
			relativeToGuiMapCoordinates.x *= (MOVING_TIME_MILIS - calculateMovementTime) / HALF_OF_MOVING_TIME_MILIS / 2;
			relativeToGuiMapCoordinates.y *= (MOVING_TIME_MILIS - calculateMovementTime) / HALF_OF_MOVING_TIME_MILIS / 2;
		} else {
			relativeToMapCoordinates.setMapCoordinates(destination);
			relativeToGuiMapCoordinates = GuiMapCoordinates.createRelativeToGuiMapCoordinates(source, relativeToMapCoordinates);
			relativeToGuiMapCoordinates.x *= calculateMovementTime / HALF_OF_MOVING_TIME_MILIS / 2;
			relativeToGuiMapCoordinates.y *= calculateMovementTime / HALF_OF_MOVING_TIME_MILIS / 2;
		}
		return relativeToGuiMapCoordinates;
	}

	public void moveTo(MapCoordinates newCoordinates) {
		if (isMoving()) {
			return;
		}
		if (newCoordinates.equals(coordinates)) {
			return;
		}
		if (!TileMap.getInstance().canMove(newCoordinates)) {
			return;
		}
		PlayN.log().info("moving");
		moveStartTimestamp = GameClient.getRenderingTime();
		this.destination = newCoordinates;
		this.source = coordinates;
		direction  = Direction.getDirection(destination.x - source.x, destination.y - source.y);
		if(direction == null) {
			System.out.println("asdasd");
		}
	}

	public boolean isMoving() {
		return calculateMovementTime() < MOVING_TIME_MILIS;
	}

	private double calculateMovementTime() {
		return GameClient.getRenderingTime() - moveStartTimestamp;
	}
	public List<Tile> getTiles() {
		return tiles;
	}

	public void addTile(Tile tile) {
		blocking = blocking || tile.isBlocking();
		this.tiles.add(tile);
	}

	public boolean isBlocking() {
		return blocking;
	}

}
