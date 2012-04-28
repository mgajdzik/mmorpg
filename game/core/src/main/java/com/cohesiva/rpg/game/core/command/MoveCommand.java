package com.cohesiva.rpg.game.core.command;

import playn.core.PlayN;
import pythagoras.i.IPoint;
import pythagoras.i.Point;

import com.cohesiva.rpg.game.core.Direction;
import com.cohesiva.rpg.game.core.GameClient;
import com.cohesiva.rpg.game.core.GuiMapCoordinates;
import com.cohesiva.rpg.game.core.MapCoordinates;
import com.cohesiva.rpg.game.core.TileMap;
import com.cohesiva.rpg.game.core.Turn;
import com.cohesiva.rpg.game.core.objects.Player;

public class MoveCommand implements Command {

	private static final IPoint POINT_0_0 = new Point();
	private final Player objectToMove;
	private final Direction direction;
	private Turn createdTime;
	private Turn startTime;
	private Turn endTime;
	private MapCoordinates destination;
	private long movementTimeInMilis;
	private Integer movementTimeInTurns;
	private boolean finished;

	public MoveCommand(Direction direction, Player objectToMove) {
		this.direction = direction;
		this.objectToMove = objectToMove;
		createdTime = GameClient.getTurn();
		finished = false;
	}

	@Override
	public CommandHandle perform() {
		startTime = GameClient.getTurn();
		objectToMove.setDirection(direction);
		MapCoordinates coordinates = objectToMove.getCoordinates();
		destination = new MapCoordinates(coordinates);
		IPoint point = direction.getPoint();
		destination.x += point.x();
		destination.y += point.y();
		if (!TileMap.getInstance().canMove(destination)) {
			return null;
		}
		PlayN.log().info("moving");
		endTime = new Turn();
		int turnsPerMovement = getDurationInTurns();
		endTime.setTurnNumber(startTime.getTurnNumber() + turnsPerMovement);
		objectToMove.setMoving(true);
		movementTimeInMilis = turnsPerMovement * Turn.getTimeDurationMilis();
		return new CommandHandle() {

			@Override
			public void update() {
				updateObjectToMove();
			}

			@Override
			public boolean isFinished() {
				return finished;
			}

			@Override
			public Command getCommand() {
				return MoveCommand.this;
			}
			

		};
	}

	protected void updateObjectToMove() {
		if (GameClient.getTurn().getTurnNumber() >= endTime.getTurnNumber()) {
			objectToMove.setCoordinates(destination);
			objectToMove.setMoving(false);
			finished = true;
			objectToMove.setShiftAgainstCenterOfCurrentLocation(POINT_0_0.clone());
			return;
		}
		objectToMove.setShiftAgainstCenterOfCurrentLocation(getShiftAgainstCenterOfCurrentTile());
	}

	@Override
	public int getDurationInTurns() {
		if (movementTimeInTurns == null) {
			movementTimeInTurns = objectToMove.getTurnsPerMovement() * POINT_0_0.distance(direction.getPoint());
		}
		return movementTimeInTurns;
	}

	private double calculateMovementTime() {
		return GameClient.getRenderingTime() - startTime.getTurnTimestamp();
	}

	public Point getShiftAgainstCenterOfCurrentTile() {

		double calculateMovementTime = calculateMovementTime();
		GuiMapCoordinates relativeToMapCoordinates = new GuiMapCoordinates();
		relativeToMapCoordinates.x = 0;
		relativeToMapCoordinates.y = 0;
		GuiMapCoordinates relativeToGuiMapCoordinates = null;

		relativeToMapCoordinates.setMapCoordinates(destination);
		relativeToGuiMapCoordinates = GuiMapCoordinates.createRelativeToGuiMapCoordinates(objectToMove.getCoordinates(),
				relativeToMapCoordinates);
		relativeToGuiMapCoordinates.x *= calculateMovementTime / movementTimeInMilis;
		relativeToGuiMapCoordinates.y *= calculateMovementTime / movementTimeInMilis;
		return relativeToGuiMapCoordinates;
	}

	public Turn getCreatedTime() {
		return createdTime;
	}

	public Turn getEndTime() {
		return endTime;
	}

	@Override
	public boolean queueMustBeEmptyToInsert() {
		return true;
	}

	@Override
	public Turn getScheduledTime() {
		return createdTime;
	}

}
