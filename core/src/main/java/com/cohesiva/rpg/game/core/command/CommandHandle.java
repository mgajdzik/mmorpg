package com.cohesiva.rpg.game.core.command;

public interface CommandHandle {

	void update();
	
	boolean isFinished();
	
	Command getCommand();
}
