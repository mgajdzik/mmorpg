package com.cohesiva.rpg.game.core.command;

import java.util.Comparator;

public class CommandComparator implements Comparator<Command> {

	@Override
	public int compare(Command o1, Command o2) {
		return (int) Math.signum(o1.getCreatedTime().getTurnNumber() - o2.getCreatedTime().getTurnNumber());
	}

}
