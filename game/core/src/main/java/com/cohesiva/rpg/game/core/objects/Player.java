package com.cohesiva.rpg.game.core.objects;

import java.util.LinkedList;
import java.util.List;

import pythagoras.i.Point;

import com.cohesiva.rpg.game.core.Direction;
import com.cohesiva.rpg.game.core.GameClient;
import com.cohesiva.rpg.game.core.MapCoordinates;

public class Player extends MultiLayerIsoObject {

	public final static double MOVING_TIME_MILIS = 500.0;
	public final static double HALF_OF_MOVING_TIME_MILIS = MOVING_TIME_MILIS / 2;

	private final static int STAND_FRAMES = 4;
	private final static int MOVE_FRAMES = 8;
	private final static int ALL_FRAMES = 32;

	private final static int MS_PER_FRAME = 150;

	private List<TileDefinition> clothes;
	private List<TileDefinition> head;
	private List<TileDefinition> shield;
	private List<TileDefinition> sword;

	protected Direction direction = Direction.DOWN;

	protected MapCoordinates destination;

	private double moveStartTimestamp;

	private int turnsPerMovement = 20;

	public int getTurnsPerMovement() {
		return turnsPerMovement;
	}

	public void setClothes(List<TileDefinition> clothes) {
		this.clothes = clothes;
	}

	public void setHead(List<TileDefinition> head) {
		this.head = head;
	}

	public void setShield(List<TileDefinition> shield) {
		this.shield = shield;
	}

	private static int[] stopFrames = { 0, 1, 2, 3, 2, 1 };
	private Point shiftAgainstCenterOfCurrentLocation = new Point();
	private boolean moving;

	@Override
	public List<TileDefinition> getTileDefinitions() {
		double renderingTime = GameClient.getRenderingTime();
		int frameOffset = 0;
		int numberOfFrames = STAND_FRAMES;
		int frameNumber;
		if (isMoving()) {
			frameOffset = STAND_FRAMES;
			numberOfFrames = MOVE_FRAMES;
			frameNumber = (int) ((renderingTime / MS_PER_FRAME) % numberOfFrames) + frameOffset;
		} else {
			frameNumber = stopFrames[(int) ((renderingTime / MS_PER_FRAME) % stopFrames.length)] + frameOffset;
		}
		int rowNumber = direction.getInt();
		int tileNumber = rowNumber * ALL_FRAMES + frameNumber;
		List<TileDefinition> result = new LinkedList<TileDefinition>();
		result.add(clothes.get(tileNumber));
		result.add(head.get(tileNumber));
		result.add(shield.get(tileNumber));
		result.add(sword.get(tileNumber));
		return result;
	}

	public void setSword(List<TileDefinition> sword) {
		this.sword = sword;
	}

	public MapCoordinates getCoordinates() {
		return coordinates;
	}

	public boolean isMoving() {
		return moving;
	}

	public void setMoving(boolean moving) {
		this.moving = moving;
	}

	public void setShiftAgainstCenterOfCurrentLocation(Point shiftAgainstCenterOfCurrentLocation) {
		this.shiftAgainstCenterOfCurrentLocation = shiftAgainstCenterOfCurrentLocation;

	}

	public Point getShiftAgainstCenterOfCurrentLocation() {
		return shiftAgainstCenterOfCurrentLocation;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}

}
