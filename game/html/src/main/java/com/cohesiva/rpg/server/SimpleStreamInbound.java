package com.cohesiva.rpg.server;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;

import org.apache.catalina.websocket.MessageInbound;

public class SimpleStreamInbound extends MessageInbound {

	@Override
	protected void onBinaryMessage(ByteBuffer message) throws IOException {
	}

	@Override
	protected void onTextMessage(CharBuffer message) throws IOException {
		System.out.println(message.toString());
	}


}
