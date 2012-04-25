package com.cohesiva.rpg.game.core;

import java.util.LinkedList;
import java.util.List;

public class Player extends IsoObject {

	private final static int STAND_FRAMES = 4;
	private final static int MOVE_FRAMES = 8;
	private final static int ALL_FRAMES = 32;

	private final static int MS_PER_FRAME = 150;

	private List<Tile> clothes;
	private List<Tile> head;
	private List<Tile> shield;
	private List<Tile> sword;

	public void setClothes(List<Tile> clothes) {
		this.clothes = clothes;
	}

	public void setHead(List<Tile> head) {
		this.head = head;
	}
	
	public void setShield(List<Tile> shield) {
		this.shield = shield;
	}

	private static int[] stopFrames = {0, 1, 2, 3, 2, 1};
	@Override
	public List<Tile> getTiles() {
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
		List<Tile> result = new LinkedList<Tile>();
		result.add(clothes.get(tileNumber));
		result.add(head.get(tileNumber));
		result.add(shield.get(tileNumber));
		result.add(sword.get(tileNumber));
		return result;
	}

	public void setSword(List<Tile> sword) {
		this.sword = sword;
	}

	

}
