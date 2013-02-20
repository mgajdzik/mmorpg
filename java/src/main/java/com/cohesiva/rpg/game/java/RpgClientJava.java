package com.cohesiva.rpg.game.java;

import static playn.core.PlayN.graphics;
import playn.core.PlayN;
import playn.java.JavaPlatform;
import pythagoras.i.Dimension;

import com.cohesiva.rpg.game.core.GameClient;

public class RpgClientJava {

	public static void main(String[] args) {
		JavaPlatform platform = JavaPlatform.register();
		
		final Dimension sizeInPixels = new Dimension(800, 600);
		PlayN.graphics().ctx().setSize(sizeInPixels.width(), sizeInPixels.height());
		platform.assets().setPathPrefix("com/cohesiva/rpg/game/resources");
		PlayN.run(new GameClient());
	}
}
