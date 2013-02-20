package com.cohesiva.rpg.game.core.command;

import com.cohesiva.rpg.game.core.Turn;

public interface Command {
	CommandHandle perform();

	int getDurationInTurns();

	Turn getCreatedTime();

	Turn getEndTime();

	boolean queueMustBeEmptyToInsert();

	Turn getScheduledTime();
}
