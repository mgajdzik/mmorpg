package com.cohesiva.rpg.server.messages;

import org.json.simple.JSONObject;

import com.cohesiva.rpg.game.core.Turn;
import com.cohesiva.rpg.server.GameServer;
import com.cohesiva.rpg.server.ServerPlayer;

public class PlayerRegisteredResponse implements ServerMessage {

	private final ServerPlayer serverPlayer;

	public PlayerRegisteredResponse(ServerPlayer serverPlayer) {
		this.serverPlayer = serverPlayer;
	}

	@Override
	public JSONObject getMessage() {
		JSONObject obj = new JSONObject();
		obj.put("message", "register");
		obj.put("turn", GameServer.getTurn(System.currentTimeMillis()).getTurnNumber());
		obj.put("id", new Integer(serverPlayer.getId()));
		return obj;
	}

	@Override
	public void update(Turn turn) {
	}

	@Override
	public boolean isFinished() {
		return true;
	}

}
