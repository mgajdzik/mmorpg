package com.cohesiva.rpg.game.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.cohesiva.rpg.game.core.objects.TileDefinition;
import com.cohesiva.rpg.game.core.objects.TileType;

import playn.core.Image;
import playn.core.Json;
import playn.core.Json.Array;
import playn.core.Json.Object;
import playn.core.PlayN;
import playn.core.ResourceCallback;

public class TileLibrary {

	protected static boolean success = true;

	private static TileLibrary instance;

	private static List<String> imageNames = new ArrayList<String>();
	
	private static List<Image> images = new ArrayList<Image>();

	private Map<String, Map<String, List<TileDefinition>>> tileLibraries = new HashMap<String, Map<String, List<TileDefinition>>>();

	public static void createLibrary(final ResourceCallback<TileLibrary> callback) {
		if (instance != null && success == true) {
			callback.done(instance);
			return;
		}
		success = true;
		PlayN.assets().getText("assets.json", new ResourceCallback<String>() {

			@Override
			public void error(Throwable err) {
				callback.error(err);
			}

			@Override
			public void done(String resource) {
				Json.Object mainAssetsConfig = PlayN.json().parse(resource);
				loadMainAssetConfiguration(mainAssetsConfig, callback);
			}
		});
	}

	private static void loadMainAssetConfiguration(Json.Object mainAssetsConfig, final ResourceCallback<TileLibrary> callback) {
		Json.Object assets = mainAssetsConfig.getObject("assets");
		final Array assetFiles = assets.getArray("assetFile");

		instance = new TileLibrary();

		final IntRef count = new IntRef();
		for (int i = 0; i < assetFiles.length(); i++) {
			final String filePath = assetFiles.getObject(i).getObject("@attributes").getString("name");
			PlayN.assets().getText(filePath, new ResourceCallback<String>() {

				@Override
				public void error(Throwable err) {
					callback.error(err);
					success = false;
				}

				@Override
				public void done(String resource) {
					if (!success) {
						// there was an error in one of the previous calls, so ignore any further successes
						return;
					}
					count.value++;
					Json.Object assetConfig = PlayN.json().parse(resource);
					loadAssetConfiguration(assetConfig, filePath, callback);
					if (count.value == assetFiles.length()) {
						preloadImages();
						callback.done(instance);
					}
				}
			});
		}
	}

	private static class IntRef {
		public int value;
	}

	private static void preloadImages() {
		for (String fileName : imageNames) {
			Image image = PlayN.assets().getImage(fileName);
			images.add(image);
		}
	}

	private static void loadAssetConfiguration(Json.Object assetConfig, String filePath, ResourceCallback<TileLibrary> callback) {
		Object assetLibraryJson = assetConfig.getObject("assetLibrary");
		String libraryName = assetLibraryJson.getObject("@attributes").getString("name");
		Array assetGroupsJson = assetLibraryJson.getArray("assetGroup");
		HashMap<String, List<TileDefinition>> library = createLibrary(libraryName, filePath, assetGroupsJson);
		instance.tileLibraries.put(libraryName, library);
	}

	private static HashMap<String, List<TileDefinition>> createLibrary(String libraryName, String filePath, Array assetGroupsJson) {
		HashMap<String, List<TileDefinition>> library = new HashMap<String, List<TileDefinition>>();
		for (int i = 0; i < assetGroupsJson.length(); i++) {
			Object assetGroupJson = assetGroupsJson.getObject(i);
			String assetGroupName = assetGroupJson.getObject("@attributes").getString("name");
			library.put(assetGroupName, createAssetGroup(libraryName, assetGroupName, filePath, assetGroupJson));
		}
		return library;
	}

	private static List<TileDefinition> createAssetGroup(String libraryName, String assetGroupName, String filePath, Object assetGroupJson) {
		ArrayList<TileDefinition> assetGroup = new ArrayList<TileDefinition>();
		TileType tileType = TileType.valueOf(assetGroupJson.getObject("@attributes").getString("type").toUpperCase());
		List<Object> imagesJson = new ArrayList<Json.Object>();
		String path = filePath.substring(0, filePath.lastIndexOf("/") + 1);

		if (assetGroupJson.isArray("image")) {
			for (int i = 0; i < assetGroupJson.getArray("image").length(); i++) {
				imagesJson.add(assetGroupJson.getArray("image").getObject(i));
			}
		} else {
			imagesJson.add(assetGroupJson.getObject("image"));
		}
		for (Object imageJson : imagesJson) {
			Object attributes = imageJson.getObject("@attributes");
			String imageFileName = path + attributes.getString("name");
			if (!imageNames.contains(imageFileName)) {
				imageNames.add(imageFileName);
			}
			int imageIndex = imageNames.indexOf(imageFileName);
			int tileWidth = Integer.parseInt(attributes.getString("tileWidth"));
			int tileHeight = Integer.parseInt(attributes.getString("tileHeight"));
			int tileHorizontalOffset = Integer.parseInt(attributes.getString("tileHorizontalOffset"));
			int tileVerticalOffset = Integer.parseInt(attributes.getString("tileVerticalOffset"));
			int numberOfTiles = Integer.parseInt(attributes.getString("numberOfTiles"));
			int width = Integer.parseInt(attributes.getString("width"));
			int height = Integer.parseInt(attributes.getString("height"));
			int renderOffsetX = Integer.parseInt(attributes.getString("renderOffsetX", "0"));
			int renderOffsetY = Integer.parseInt(attributes.getString("renderOffsetY", "0"));
			boolean moveable = !attributes.containsKey("moveable") || "true".equalsIgnoreCase(attributes.getString("moveable"));
			for (int i = 0; i < numberOfTiles; i++) {
				TileDefinition tile = new TileDefinition();
				tile.setAssetHorizontalOffset(tileHorizontalOffset);
				tile.setAssetVerticalOffset(tileVerticalOffset);
				tile.setHeight(tileHeight);
				tile.setWidth(tileWidth);
				tile.setImageIndex(imageIndex);
				tile.setRenderOffsetX(renderOffsetX);
				tile.setRenderOffsetY(renderOffsetY);
				tile.setBlocking(!moveable);

				tileHorizontalOffset += tileWidth;
				if (tileHorizontalOffset >= width) {
					tileVerticalOffset += tileHeight;
					tileHorizontalOffset = 0;
				}
				assetGroup.add(tile);
			}
		}
		return assetGroup;
	}

	public Map<String, Map<String, List<TileDefinition>>> getTileLibraries() {
		return tileLibraries;
	}

	public static List<Image> getImages() {
		return images;
	}

	public static TileLibrary getInstance() {
		return instance;
	}
}
