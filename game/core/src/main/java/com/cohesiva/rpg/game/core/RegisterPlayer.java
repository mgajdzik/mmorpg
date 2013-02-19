package com.cohesiva.rpg.game.core;

import playn.core.PlayN;

public class RegisterPlayer {

	public RegisterPlayer() {
	}

	@Override
	public String toString() {
		String jsonString = PlayN.json().newWriter().object().value("message", "register").end().write();
		return jsonString;
	}

}
