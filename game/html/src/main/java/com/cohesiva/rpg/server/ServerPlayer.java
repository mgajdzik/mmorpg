package com.cohesiva.rpg.server;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.catalina.websocket.MessageInbound;
import org.json.simple.JSONObject;

import com.cohesiva.rpg.game.core.Turn;
import com.cohesiva.rpg.server.messages.PlayerRegisteredResponse;
import com.cohesiva.rpg.server.messages.ServerMessage;

public class ServerPlayer {

	private static Map<Integer, ServerPlayer> players = new HashMap<Integer, ServerPlayer>();
	private int id;
	private int x, y;
	private MainStreamInbound messageInbound;
	private List<ServerMessage> serverMessages = new ArrayList<ServerMessage>();

	public static ServerPlayer createPlayer(int id) {
		ServerPlayer serverPlayer = new ServerPlayer();
		serverPlayer.setId(id);
		players.put(id, serverPlayer);
		serverPlayer.x = 50;
		serverPlayer.y = 50;
		return serverPlayer;
	}

	private void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public void setMessageInbound(MainStreamInbound messageInbound) {
		this.messageInbound = messageInbound;
	}

	public MainStreamInbound getMessageInbound() {
		return messageInbound;
	}

	public void sendMessage(String message) {
		messageInbound.sendMessage(message);
	}

	public void sendMessage(JSONObject message) {
		sendMessage(serializeJson(message));
	}

	public static void sendMessageToAll(String message) {
		for (ServerPlayer player : players.values()) {
			try {
				player.sendMessage(message);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void sendMessageToAll(JSONObject message) {
		sendMessageToAll(serializeJson(message));
	}

	public static void sendMessageToAll(ServerMessage message) {
		sendMessageToAll(message.getMessage());
	}

	private static String serializeJson(JSONObject object) {
		StringWriter out = new StringWriter();
		try {
			object.writeJSONString(out);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		String jsonText = out.toString();
		return jsonText;
	}

	public void sendMessage(ServerMessage message) {
		sendMessage(message.getMessage());
	}

	public static ServerPlayer getPlayer(int id) {
		return players.get(id);
	}

	public static void updatePlayers(Turn turn) {
		for (ServerPlayer player : players.values()) {
			Iterator<ServerMessage> iterator = player.serverMessages.iterator();
			while (iterator.hasNext()) {
				ServerMessage message = iterator.next();
				message.update(turn);
				if (message.isFinished()) {
					iterator.remove();
				}
			}
		}
	}

	public static void removePlayer(int id2) {
		players.remove(id2);
	}
}
