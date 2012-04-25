package com.cohesiva.rpg.game.java;

import playn.core.PlayN;
import playn.java.JavaPlatform;

import com.cohesiva.rpg.game.core.GameClient;

public class RpgClientJava {

	public static void main(String[] args) {
		JavaPlatform platform = JavaPlatform.register();
		platform.assets().setPathPrefix("com/cohesiva/rpg/game/resources");
		PlayN.run(new GameClient());
	}
}
