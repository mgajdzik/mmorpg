package com.cohesiva.rpg.server;

import org.apache.catalina.websocket.StreamInbound;

public class WebSocketServlet extends org.apache.catalina.websocket.WebSocketServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected StreamInbound createWebSocketInbound(String subProtocol) {
		return new SimpleStreamInbound();
	}

}
