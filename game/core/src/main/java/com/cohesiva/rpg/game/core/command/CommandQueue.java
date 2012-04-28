package com.cohesiva.rpg.game.core.command;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.cohesiva.rpg.game.core.Turn;

public class CommandQueue {

	private List<Command> playerCommands;
	
	private Set<CommandHandle> runningCommandHandles;
	
	private Set<Command> runningCommands = new HashSet<Command>();

	public CommandQueue() {
		playerCommands = new ArrayList<Command>();
		runningCommandHandles = new HashSet<CommandHandle>();
	}

	public void addCommand(Command command) {
		if (command.queueMustBeEmptyToInsert()) {
			if (playerCommands.isEmpty() && runningCommands.isEmpty()) {
				playerCommands.add(command);
			}
		} else {

		}
	}
	
	public void update() {
		Iterator<CommandHandle> i = runningCommandHandles.iterator();
		while(i.hasNext()) {
			CommandHandle commandHandle = i.next();
			commandHandle.update();
			if (commandHandle.isFinished()) {
				runningCommands.remove(commandHandle.getCommand());
				i.remove();
			}
		}
	}
	
	public boolean isPerformingCommands() {
		return !runningCommandHandles.isEmpty();
	}

	public void performCommands(Turn turn) {
		Iterator<Command> i = playerCommands.iterator();
		while(i.hasNext()) {
			Command command = i.next();
			if (command.getScheduledTime().getTurnNumber() <= turn.getTurnNumber()) {
				i.remove();
				CommandHandle commandHandle = command.perform();
				if (commandHandle != null) {
					runningCommands.add(command);
					runningCommandHandles.add(commandHandle);
					commandHandle.update();
				}
			}
		}
	}

}
