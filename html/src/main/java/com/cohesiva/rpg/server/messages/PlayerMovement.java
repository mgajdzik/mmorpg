package com.cohesiva.rpg.server.messages;

import org.json.simple.JSONObject;

import pythagoras.i.Point;

import com.cohesiva.rpg.game.core.Turn;
import com.cohesiva.rpg.server.ServerPlayer;

public class PlayerMovement implements ServerMessage {

	private final ServerPlayer serverPlayer;
	private long endTurnNumber;
	private Point destination;
	private Long startTurnNumber;
	private Point startingPoint;
	private JSONObject jsonObject;
	private boolean finished;

	public PlayerMovement(ServerPlayer serverPlayer) {
		this.serverPlayer = serverPlayer;
	}

	public void fromJson(JSONObject jsonObject) {
		this.jsonObject = jsonObject;
		JSONObject start = (JSONObject) jsonObject.get("start");
		JSONObject end = (JSONObject) jsonObject.get("end");
		parseStart(start);
		parseEnd(end);
	}

	private void parseEnd(JSONObject end) {
		endTurnNumber = (Long) end.get("turn");
		destination = new Point();
		destination.x = ((Long) end.get("x")).intValue();
		destination.y = ((Long) end.get("y")).intValue();
	}

	private void parseStart(JSONObject start) {
		startTurnNumber = (Long) start.get("turn");
		startingPoint = new Point();
		startingPoint.x = ((Long) start.get("x")).intValue();
		startingPoint.y = ((Long) start.get("y")).intValue();
	}

	@Override
	public JSONObject getMessage() {
		JSONObject obj = jsonObject;
		obj.put("id", new Integer(serverPlayer.getId()));
		return obj;
	}

	@Override
	public void update(Turn turn) {
		if (turn.getTurnNumber() >= endTurnNumber) {
			serverPlayer.setX(destination.x);
			serverPlayer.setY(destination.y);
			finished = true;
		}
	}

	@Override
	public boolean isFinished() {
		return finished;
	}

}
