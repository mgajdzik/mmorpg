package com.cohesiva.rpg.server.messages;

import org.json.simple.JSONObject;

import com.cohesiva.rpg.game.core.Turn;

public interface ServerMessage {

	JSONObject getMessage();

	void update(Turn turn);

	boolean isFinished();
}
