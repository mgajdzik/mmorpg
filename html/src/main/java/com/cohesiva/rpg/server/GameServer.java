package com.cohesiva.rpg.server;

import com.cohesiva.rpg.game.core.Turn;
import com.cohesiva.rpg.game.core.objects.Player;

public class GameServer {

	private static Turn turn;

	public static Turn getTurn(long timestamp) {
		if (turn == null) {
			turn = new Turn();
			turn.setTurnNumber(0);
			turn.setTurnTimestamp(timestamp);
		} else {
			long diff = (long) (timestamp - turn.getTurnTimestamp());
			if (diff >= Turn.getTimeDurationMilis()) {
				Turn newTurn = new Turn();
				long turnsPassed = (long) Math.floor(diff / Turn.getTimeDurationMilis());
				newTurn.setTurnNumber(turn.getTurnNumber() + turnsPassed);
				newTurn.setTurnTimestamp(newTurn.getTurnNumber() + turnsPassed * Turn.getTimeDurationMilis());
				turn = newTurn;
				updatePlayers();
			}
		}
		return turn;
	}

	private static void updatePlayers() {
		ServerPlayer.updatePlayers(turn);
	}
}
