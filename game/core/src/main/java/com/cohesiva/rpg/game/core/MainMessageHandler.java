package com.cohesiva.rpg.game.core;

import java.util.HashMap;
import java.util.Map;

import playn.core.Json.Object;
import playn.core.PlayN;

public class MainMessageHandler {

	private Map<String, MessageHandler> registry = new HashMap<String, MessageHandler>();

	public void onMessage(String message) {
		Object parsedObject = PlayN.json().parse(message);
		MessageHandler messageHandler = registry.get(parsedObject.getString("message"));
		if (messageHandler != null) {
			messageHandler.onMessage(parsedObject);
		}

	}

	public void register(String messageType, MessageHandler messageHandler) {
		registry.put(messageType, messageHandler);
	}

}
