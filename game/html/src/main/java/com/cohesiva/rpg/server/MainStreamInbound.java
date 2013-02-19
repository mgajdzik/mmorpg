package com.cohesiva.rpg.server;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.util.HashMap;
import java.util.Map;

import org.apache.catalina.websocket.MessageInbound;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.cohesiva.rpg.server.messages.ServerMessage;

public class MainStreamInbound extends MessageInbound {
	private static Map<String, MessageHandler> registry = new HashMap<String, MessageHandler>();
	private static Id lastId = new Id();
	private int id;

	public MainStreamInbound() {
		id = lastId.nextInt();
	}

	private static class Id {
		private int i = 0;

		public synchronized int nextInt() {
			return i++;
		}
	}

	@Override
	protected void onBinaryMessage(ByteBuffer message) throws IOException {
	}

	@Override
	protected void onTextMessage(CharBuffer message) throws IOException {
		JSONParser parser=new JSONParser();
		JSONObject object;
		try {
			object = (JSONObject)parser.parse(message.toString().trim());
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
		MessageHandler messageHandler = registry.get(object.get("message"));
		if (messageHandler != null) {
			messageHandler.onMessage(id, object, this);
		}
	}

	public void sendMessage(String serialized) {
		try {
			getWsOutbound().writeTextMessage(CharBuffer.wrap(serialized.toCharArray()));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}


	public static void register(String messageType, MessageHandler messageHandler) {
		registry.put(messageType, messageHandler);
	}

	@Override
	protected void onClose(int status) {
		ServerPlayer.removePlayer(id);
		super.onClose(status);
	}

}
