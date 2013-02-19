package com.cohesiva.rpg.server;

import org.apache.catalina.websocket.MessageInbound;
import org.json.simple.JSONObject;

import com.cohesiva.rpg.server.messages.ServerMessage;

public interface MessageHandler {

	void onMessage(int id, JSONObject message, MainStreamInbound messageInbound);
}
