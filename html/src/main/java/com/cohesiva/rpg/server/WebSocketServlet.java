package com.cohesiva.rpg.server;

import javax.servlet.ServletException;

import org.apache.catalina.websocket.MessageInbound;
import org.apache.catalina.websocket.StreamInbound;
import org.json.simple.JSONObject;

import com.cohesiva.rpg.server.messages.PlayerMovement;
import com.cohesiva.rpg.server.messages.PlayerRegisteredResponse;
import com.cohesiva.rpg.server.messages.ServerMessage;

public class WebSocketServlet extends org.apache.catalina.websocket.WebSocketServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected StreamInbound createWebSocketInbound(String subProtocol) {
		return new MainStreamInbound();
	}

	@Override
	public void init() throws ServletException {
		super.init();
		


		MainStreamInbound.register("register", new MessageHandler() {

			@Override
			public void onMessage(int id, JSONObject message, MainStreamInbound messageInbound) {
				ServerPlayer serverPlayer = ServerPlayer.createPlayer(id);
				serverPlayer.setMessageInbound(messageInbound);
				serverPlayer.sendMessage(new PlayerRegisteredResponse(serverPlayer));
			}
		});
		
		MainStreamInbound.register("move", new MessageHandler() {

			@Override
			public void onMessage(int id, JSONObject message, MainStreamInbound messageInbound) {
				ServerPlayer serverPlayer = ServerPlayer.getPlayer(id);
				PlayerMovement playerMovementMessage = new PlayerMovement(serverPlayer);
				playerMovementMessage.fromJson(message);
				ServerPlayer.sendMessageToAll(playerMovementMessage);
			}
		});
	}

}
