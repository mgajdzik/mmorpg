package com.cohesiva.rpg.game.core;

import static playn.core.PlayN.graphics;
import static playn.core.PlayN.keyboard;

import com.cohesiva.rpg.game.core.command.CommandQueue;
import com.cohesiva.rpg.game.core.command.MoveCommand;
import com.cohesiva.rpg.game.core.objects.Player;

import playn.core.CanvasImage;
import playn.core.Game;
import playn.core.ImmediateLayer;
import playn.core.Key;
import playn.core.Keyboard;
import playn.core.Keyboard.Event;
import playn.core.Keyboard.Listener;
import playn.core.Keyboard.TypedEvent;
import playn.core.PlayN;
import playn.core.ResourceCallback;
import playn.core.Surface;
import playn.core.WebSocket;
import pythagoras.i.Dimension;

public class GameClient implements Game, Listener {
	private static double renderingTime;

	private static Turn turn;

	public static final int FIELD_WIDTH = 1000;
	public static final int FIELD_HEIGHT = 1000;
	public static final int TILE_WIDTH = 64;
	public static final int TILE_HEIGHT = 32;

	private int[] frameRates = new int[10];
	private int index;
	private Player player;

	private int moveRight;
	private int moveDown;
	private MapView mapView;

	private WebSocket webSocket;

	private CommandQueue commandQueue;

	@Override
	public void init() {
		final Dimension sizeInPixels = new Dimension(800, 600);

		graphics().setSize(sizeInPixels.width(), sizeInPixels.height());

		keyboard().setListener(this);

		commandQueue = new CommandQueue();

		// webSocket = PlayN.net().createWebSocket(
		// "ws://localhost:8080/websocket/ws");

		TileMap map = TileMap.getInstance();
		map.initMapWithRandomElements(new ResourceCallback<TileMap>() {

			@Override
			public void error(Throwable err) {
			}

			@Override
			public void done(TileMap map) {
				initPlayer();
				mapView = new MapView(player, map, sizeInPixels);
				turn = new Turn();
				turn.setTurnTimestamp(PlayN.currentTime());
				graphics().rootLayer().add(createGameLayer(mapView, 0));
			}

		});

	}

	private void initPlayer() {
		player = new Player();
		// player.setCoordinates(new MapCoordinates(10,10));
		player.setCoordinates(new MapCoordinates(FIELD_WIDTH / 2, FIELD_HEIGHT / 2));
		player.setClothes(TileLibrary.getInstance().getTileLibraries().get("player").get("clothes"));
		player.setHead(TileLibrary.getInstance().getTileLibraries().get("player").get("male_head1"));
		player.setShield(TileLibrary.getInstance().getTileLibraries().get("player").get("shield"));
		player.setSword(TileLibrary.getInstance().getTileLibraries().get("player").get("longsword"));
	}

	private ImmediateLayer createGameLayer(final MapView mapView, final Integer layerNumber) {
		return graphics().createImmediateLayer(new ImmediateLayer.Renderer() {
			public void render(Surface surface) {
				mapView.paint(surface, layerNumber);
				drawFPS(surface);
			}
		});
	}

	@Override
	public void paint(float alpha) {
		
	}

	private int avarageFrameRate() {
		int av = 0;
		for (int i = 0; i < 10; i++) {
			av += frameRates[i];
		}
		return av / 10;
	}

	@Override
	public void update(float delta) {
		updateRenderingTime();
		updateTurn();
		commandQueue.update();
		updatePlayerMovement();
		commandQueue.performCommands(turn);
	}

	private void updateRenderingTime() {
		double oldRenderingTime = renderingTime;
		renderingTime = PlayN.currentTime();
		frameRates[index] = (int) (1000 / (renderingTime - oldRenderingTime));
		index = (++index) % 10;		
	}

	private void updateTurn() {
		double diff = renderingTime - turn.getTurnTimestamp();
		if (diff >= Turn.getTimeDurationMilis()) {
			Turn newTurn = new Turn();
			long turnsPassed = (long) Math.floor(diff / Turn.getTimeDurationMilis());
			newTurn.setTurnNumber(turn.getTurnNumber() + turnsPassed);
			newTurn.setTurnTimestamp(turn.getTurnTimestamp() + turnsPassed * Turn.getTimeDurationMilis());
			turn = newTurn;
		}
	}

	private void updatePlayerMovement() {
		if (moveDown != 0 || moveRight != 0) {
			MapCoordinates coordinates = player.getCoordinates();
			MapCoordinates newCoordinates = new MapCoordinates(coordinates);
			int x = 0, y = 0;
			y += moveDown;
			x += moveDown;
			y -= moveRight;
			x += moveRight;
			newCoordinates.x += Math.signum(x);
			newCoordinates.y += Math.signum(y);
			Direction direction = Direction.getDirection(newCoordinates.x - coordinates.x, newCoordinates.y - coordinates.y);
			commandQueue.addCommand(new MoveCommand(direction, player));
		}
	}

	@Override
	public int updateRate() {
		return 0;
	}

	@Override
	public void onKeyDown(Keyboard.Event event) {
		Key key = event.key();
		switch (key) {
		case UP:
			moveDown = -1;
			break;
		case DOWN:
			moveDown = 1;
			break;
		case LEFT:
			moveRight = -1;
			break;
		case RIGHT:
			moveRight = 1;
			break;
		}

	}

	@Override
	public void onKeyTyped(TypedEvent event) {
	}

	@Override
	public void onKeyUp(Event event) {
		Key key = event.key();
		switch (key) {
		case UP:
			moveDown = 0;
			break;
		case DOWN:
			moveDown = 0;
			break;
		case LEFT:
			moveRight = 0;
			break;
		case RIGHT:
			moveRight = 0;
			break;
		}
	}

	private void drawFPS(Surface surface) {
		CanvasImage image = PlayN.graphics().createImage(100, 100);
		image.canvas().drawText(avarageFrameRate() + " FPS", 5, 20);
		image.canvas().drawText(PlayN.platformType().name(), 5, 40);
		surface.drawImage(image, 0, 0);
	}

	public static double getRenderingTime() {
		return renderingTime;
	}

	public static Turn getTurn() {
		return turn;
	}
}
