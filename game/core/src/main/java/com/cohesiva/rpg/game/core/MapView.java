package com.cohesiva.rpg.game.core;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import playn.core.Image;
import playn.core.Surface;
import pythagoras.i.Dimension;
import pythagoras.i.Point;

public class MapView {

	private static final int ADDITIONAL_TILES_TO_DRAW = 12;
	private Player player;

	private TileMap map;
	private Dimension clientViewSize;

	private int dimensionOfTilesToDraw;

	private GuiMapCoordinates[] guiMapCoordinatesArray;

	private Map<Point, Integer> coordinatesToIndex = new HashMap<Point, Integer>();

	private List<IsoObject> objectsToDraw = new LinkedList<IsoObject>();

	public MapView(Player player, TileMap map, Dimension clientViewSize) {
		this.player = player;
		this.map = map;
		this.clientViewSize = clientViewSize;
		int clientViewDiagonal = (int) Math.sqrt(clientViewSize.height() * clientViewSize.height() + clientViewSize.width()
				* clientViewSize.width());
		int tileDiagonal = (int) Math.sqrt(GameClient.TILE_HEIGHT * GameClient.TILE_HEIGHT + GameClient.TILE_WIDTH * GameClient.TILE_WIDTH) / 2;
		dimensionOfTilesToDraw = clientViewDiagonal / tileDiagonal + ADDITIONAL_TILES_TO_DRAW;

		int numberOfTiles = (dimensionOfTilesToDraw) * (dimensionOfTilesToDraw);

		int x = 0, y = 0;
		guiMapCoordinatesArray = new GuiMapCoordinates[numberOfTiles];

		GuiMapCoordinates playerMapCoordinates = new GuiMapCoordinates();
		playerMapCoordinates.setMapCoordinates(new MapCoordinates(dimensionOfTilesToDraw / 2, dimensionOfTilesToDraw / 2));
		playerMapCoordinates.setGuiCoordinatesCentered(clientViewSize.width / 2, clientViewSize.height / 2);
		int maxX = dimensionOfTilesToDraw;
		int maxY = dimensionOfTilesToDraw;
		int startX = 0;
		int startY = 0;
		for (int i = 0; i < numberOfTiles; i++) {
			MapCoordinates mapCoordinates = new MapCoordinates(x, y);
			GuiMapCoordinates tileCoordinates = GuiMapCoordinates.createRelativeToGuiMapCoordinates(mapCoordinates, playerMapCoordinates);
			guiMapCoordinatesArray[i] = tileCoordinates;
			coordinatesToIndex.put(mapCoordinates, i);
			if (x == maxX) {
				x = startX + y - startY + 1;
				y = maxY;
			} else if (y == startY) {
				y = startY + x - startX + 1;
				x = startX;
			} else {
				x += 1;
				y -= 1;
			}
		}

	}

	public Player getPlayer() {
		return player;
	}

	public void paint(Surface surface, Integer layerNumber) {
		surface.clear();
		MapCoordinates playerCoordinates = player.getCoordinates();
		Point shiftAgainstCenterOfCurrentTile = player.getShiftAgainstCenterOfCurrentTile();
		drawGroundTiles(surface, playerCoordinates, shiftAgainstCenterOfCurrentTile);
		drawObjects(surface, playerCoordinates, shiftAgainstCenterOfCurrentTile);
		// surface.drawLine(clientViewSize.width / 2, 0, clientViewSize.width / 2, clientViewSize.height, 1);
		// surface.drawLine(0, clientViewSize.height / 2, clientViewSize.width, clientViewSize.height / 2, 1);
	}

	private void drawGroundTiles(Surface surface, MapCoordinates centerOnCoordinates, Point shiftAgainstCenterOfCurrentTile) {
		int x = centerOnCoordinates.x() - dimensionOfTilesToDraw / 2;
		int y = centerOnCoordinates.y() - dimensionOfTilesToDraw / 2;
		int maxX = centerOnCoordinates.x() + dimensionOfTilesToDraw / 2;
		int maxY = centerOnCoordinates.y() + dimensionOfTilesToDraw / 2;
		int startX = x;
		int startY = y;
		objectsToDraw.clear();
		List<Image> images = TileLibrary.getImages();
		Map<MapCoordinates, IsoObject> objects = map.getObjects();
		for (int i = 0; i < guiMapCoordinatesArray.length; i++) {
			if (x > 0 && x < GameClient.FIELD_WIDTH && y > 0 && y < GameClient.FIELD_HEIGHT) {
				GuiMapCoordinates tileCoordinates = guiMapCoordinatesArray[i];
				Tile tile = map.getTileMap()[x][y];
				IsoObject isoObject = objects.get(new MapCoordinates(x, y));
				if (isoObject != null) {
					objectsToDraw.add(isoObject);
				}
				if (centerOnCoordinates.x == x && centerOnCoordinates.y == y) {
					objectsToDraw.add(player);
				}
				int dx = tileCoordinates.x() + shiftAgainstCenterOfCurrentTile.x;
				int dy = tileCoordinates.y() - tile.getHeight() + GameClient.TILE_HEIGHT + shiftAgainstCenterOfCurrentTile.y;
				int tileWidth = GameClient.TILE_WIDTH;
				int height = tile.getHeight();
//				if (isoObject != null) {
				if (dx + tileWidth >= 0 && dy + height > 0 && dx < clientViewSize.width & dy < clientViewSize.height) {
					int imageIndex = tile.getImageIndex();
					Image image = images.get(imageIndex);
					surface.drawImage(image, dx, dy, tileWidth, height, tile.getAssetHorizontalOffset(), tile.getAssetVerticalOffset(),
							tile.getWidth(), height);
				}
//			}

			}
			if (x == maxX) {
				x = startX + y - startY + 1;
				y = maxY;
			} else if (y == startY) {
				y = startY + x - startX + 1;
				x = startX;
			} else {
				x += 1;
				y -= 1;
			}
		}
	}

	private void drawObjects(Surface surface, Point centerOnCoordinates, Point shiftAgainstCenterOfCurrentTile) {
		int startMapX = centerOnCoordinates.x() - dimensionOfTilesToDraw / 2;
		int startMapY = centerOnCoordinates.y() - dimensionOfTilesToDraw / 2;
		List<Image> images = TileLibrary.getImages();
		for (IsoObject isoObject : objectsToDraw) {
			Point coordinates = isoObject.getCoordinates();
			int x = coordinates.x;
			int y = coordinates.y;
			if (x > 0 && x < GameClient.FIELD_WIDTH && y > 0 && y < GameClient.FIELD_HEIGHT) {
				MapCoordinates mapCoordinates = new MapCoordinates(coordinates);
				mapCoordinates.x -= startMapX;
				mapCoordinates.y -= startMapY;
				Integer i = coordinatesToIndex.get(mapCoordinates);
				if (i == null) {
					continue;
				}
				GuiMapCoordinates tileCoordinates = guiMapCoordinatesArray[i];
				for (Tile tile : isoObject.getTiles()) {
					Image image = images.get(tile.getImageIndex());
					Point shiftOfTileAgainstCenterOfCurrentTile = isoObject.getShiftAgainstCenterOfCurrentTile();
					int xOnScreen = tileCoordinates.x() + shiftAgainstCenterOfCurrentTile.x + tile.getRenderOffsetX()
							- shiftOfTileAgainstCenterOfCurrentTile.x;
					int yOnScreen = tileCoordinates.y() - tile.getHeight() + GameClient.TILE_HEIGHT + shiftAgainstCenterOfCurrentTile.y
							+ tile.getRenderOffsetY() - shiftOfTileAgainstCenterOfCurrentTile.y;
					int widthOnScreen = tile.getWidth();
					int heightOnScreen = tile.getHeight();
					int xOnImage = tile.getAssetHorizontalOffset();
					int yOnImage = tile.getAssetVerticalOffset();
					int widthOnImage = tile.getWidth();
					int heightOnImage = tile.getHeight();
					surface.drawImage(image, xOnScreen, yOnScreen, widthOnScreen, heightOnScreen, xOnImage, yOnImage, widthOnImage,
							heightOnImage);
				}
			}
		}
	}
}
