package com.cohesiva.rpg.game.core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import playn.core.PlayN;
import playn.core.ResourceCallback;

public class TileMap {

	private static TileMap instance;

	private Tile[][] tiles;

	private TileLibrary tileLibrary;

	private Map<MapCoordinates, IsoObject> objects = new HashMap<MapCoordinates, IsoObject>();

	public Tile[][] getTileMap() {
		return tiles;
	}

	private TileMap() {
		tiles = new Tile[GameClient.FIELD_WIDTH][GameClient.FIELD_HEIGHT];
	}

	public static TileMap getInstance() {
		if (instance == null) {
			instance = new TileMap();
		}
		return instance;
	}

	public void initMapWithRandomElements(final ResourceCallback<TileMap> callback) {
		TileLibrary.createLibrary(new ResourceCallback<TileLibrary>() {
			@Override
			public void done(TileLibrary resource) {
				tileLibrary = resource;
				fillMap(callback);
			}

			@Override
			public void error(Throwable err) {
				PlayN.log().error("Error while creating tile library: ", err);
				callback.error(err);
			}
		});
	}

	private void fillMap(ResourceCallback<TileMap> callback) {
		Map<String, List<Tile>> grassland = tileLibrary.getTileLibraries().get("grassland");
		Random random = new Random();
		for (int x = 0; x < GameClient.FIELD_WIDTH; x++) {
			for (int y = 0; y < GameClient.FIELD_HEIGHT; y++) {
				if (random.nextInt(1000) < 995) {
					tiles[x][y] = grassland.get("grass").get(random.nextInt(16));
				} else {
					tiles[x][y] = grassland.get("road").get(random.nextInt(16));
				}
			}
		}
		for (int i = 0; i < GameClient.FIELD_WIDTH * GameClient.FIELD_HEIGHT / 20; i++) {
			int x = random.nextInt(GameClient.FIELD_WIDTH);
			int y = random.nextInt(GameClient.FIELD_HEIGHT);
			MapCoordinates mapCoordinates = new MapCoordinates(x, y);
			if (objects.containsKey(mapCoordinates)) {
				continue;
			}
			IsoObject isoObject = new IsoObject();
			isoObject.setCoordinates(mapCoordinates);
			isoObject.addTile(grassland.get("trees").get(random.nextInt(16)));
			objects.put(mapCoordinates, isoObject);
		}

		for (int i = 0; i < GameClient.FIELD_WIDTH * GameClient.FIELD_HEIGHT / 10; i++) {
			int x = random.nextInt(GameClient.FIELD_WIDTH);
			int y = random.nextInt(GameClient.FIELD_HEIGHT);
			MapCoordinates mapCoordinates = new MapCoordinates(x, y);
			if (objects.containsKey(mapCoordinates)) {
				continue;
			}
			IsoObject isoObject = new IsoObject();
			isoObject.setCoordinates(mapCoordinates);
			isoObject.addTile(grassland.get("bushes").get(random.nextInt(16)));
			objects.put(mapCoordinates, isoObject);
		}

		for (int i = 0; i < GameClient.FIELD_WIDTH * GameClient.FIELD_HEIGHT / 240; i++) {
			int x = random.nextInt(GameClient.FIELD_WIDTH);
			int y = random.nextInt(GameClient.FIELD_HEIGHT);
			MapCoordinates mapCoordinates = new MapCoordinates(x, y);
			if (objects.containsKey(mapCoordinates)) {
				continue;
			}
			IsoObject isoObject = new IsoObject();
			isoObject.setCoordinates(mapCoordinates);
			isoObject.addTile(grassland.get("objects").get(random.nextInt(16)));
			objects.put(mapCoordinates, isoObject);
		}
		callback.done(this);
	}

	public Map<MapCoordinates, IsoObject> getObjects() {
		return objects;
	}

	public boolean canMove(MapCoordinates newCoordinates) {
		if (tiles[newCoordinates.x][newCoordinates.y].isBlocking()) {
			return false;
		}
		IsoObject isoObject = objects.get(newCoordinates);
		if (isoObject != null) {
			return !isoObject.isBlocking();
		}
		return true;
	}

}
