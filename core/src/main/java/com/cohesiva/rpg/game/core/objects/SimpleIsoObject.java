package com.cohesiva.rpg.game.core.objects;

import java.util.ArrayList;
import java.util.List;

public class SimpleIsoObject extends AbstractIsoObject {
	protected List<TileDefinition> tileDefinition = new ArrayList<TileDefinition>();

	public void setTileDefinition(TileDefinition tileDefinition) {
		this.tileDefinition.clear();
		this.tileDefinition.add(tileDefinition);
		setBlocking(tileDefinition.isBlocking());
	}
	
	@Override
	public List<TileDefinition> getTileDefinitions() {
		return tileDefinition;
	}

}
