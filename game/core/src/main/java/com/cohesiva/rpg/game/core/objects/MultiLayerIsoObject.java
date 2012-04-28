package com.cohesiva.rpg.game.core.objects;

public class MultiLayerIsoObject extends SimpleIsoObject {

	public void addTile(TileDefinition tile) {
		setBlocking(isBlocking() || tile.isBlocking());
		this.tileDefinition.add(tile);
	}
}
