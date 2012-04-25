package com.cohesiva.rpg.game.core;

import pythagoras.i.Point;

public class GuiMapCoordinates extends Point {

	private MapCoordinates mapCoordinates;

	public void setMapCoordinates(MapCoordinates mapCoordinates) {
		this.mapCoordinates = mapCoordinates;
	}

	public MapCoordinates getMapCoordinates() {
		return mapCoordinates;
	}

	public void setGuiCoordinatesCentered(int xCentered, int yCentered) {
		x = xCentered - GameClient.TILE_WIDTH / 2;
		y = yCentered - GameClient.TILE_HEIGHT / 2;
	}

	public static GuiMapCoordinates createRelativeToGuiMapCoordinates(MapCoordinates mapCoordinates,
			GuiMapCoordinates relativeToMapCoordinates) {
		int mapXDiff = mapCoordinates.x - relativeToMapCoordinates.mapCoordinates.x;
		int mapYDiff = mapCoordinates.y - relativeToMapCoordinates.mapCoordinates.y;
		int xDiff = (mapXDiff - mapYDiff) * GameClient.TILE_WIDTH / 2;
		int yDiff = (mapYDiff + mapXDiff) * GameClient.TILE_HEIGHT / 2;

		GuiMapCoordinates guiMapCoordinates = new GuiMapCoordinates();
		guiMapCoordinates.mapCoordinates = mapCoordinates;
		guiMapCoordinates.x = relativeToMapCoordinates.x + xDiff;
		guiMapCoordinates.y = relativeToMapCoordinates.y + yDiff;
		return guiMapCoordinates;
	}

}
