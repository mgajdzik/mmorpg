package com.cohesiva.rpg.game.core.command;

import com.cohesiva.rpg.game.core.Direction;
import com.cohesiva.rpg.game.core.MapCoordinates;
import com.cohesiva.rpg.game.core.TemporalCoordinates;
import com.cohesiva.rpg.game.core.Turn;
import com.cohesiva.rpg.game.core.objects.Player;

public class MovetoCommand extends MoveCommand {

	private final TemporalCoordinates start;
	private final TemporalCoordinates end;

	public MovetoCommand(TemporalCoordinates start, TemporalCoordinates end, Player objectToMove) {
		super(Direction.getDirection(end.x - start.x, end.y - start.y), objectToMove);
		this.start = start;
		this.end = end;
	}

	@Override
	protected Turn createStartTime() {
		return start.getTurn();
	}



	@Override
	protected Turn createEndTime() {
		return end.getTurn();
	}

	@Override
	protected MapCoordinates createDestination() {
		return end;
		
	}

	@Override
	protected void sendMessage() {
	}

	@Override
	public boolean queueMustBeEmptyToInsert() {
		return false;
	}

	@Override
	public int getDurationInTurns() {
		return (int) (end.getTurn().getTurnNumber() - start.getTurn().getTurnNumber());
	}

	@Override
	protected void updateObjectToMoveBeforeStart() {
		getObjectToMove().setCoordinates(start);
	}

	
}
