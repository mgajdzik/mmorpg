package com.cohesiva.rpg.game.core;

import static playn.core.PlayN.graphics;
import static playn.core.PlayN.keyboard;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import playn.core.CanvasImage;
import playn.core.Game;
import playn.core.ImmediateLayer;
import playn.core.Json.Object;
import playn.core.Key;
import playn.core.Keyboard;
import playn.core.Keyboard.Event;
import playn.core.Keyboard.Listener;
import playn.core.Keyboard.TypedEvent;
import playn.core.Net.WebSocket;
import playn.core.PlayN;
import playn.core.ResourceCallback;
import playn.core.Surface;
import pythagoras.i.Dimension;

import com.cohesiva.rpg.game.core.command.CommandQueue;
import com.cohesiva.rpg.game.core.command.MoveCommand;
import com.cohesiva.rpg.game.core.command.MovetoCommand;
import com.cohesiva.rpg.game.core.objects.Player;

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
	private static Map<Integer, Player> players = new HashMap<Integer, Player>();

	private int moveRight;
	private int moveDown;
	private MapView mapView;

	private static WebSocket webSocket;

	private CommandQueue commandQueue;
	private ImmediateLayer gameLayer;

	private MainMessageHandler mainMessageHandler;

	private boolean initialized = false;

	@Override
	public void init() {
		final Dimension sizeInPixels = new Dimension(800, 600);
		commandQueue = new CommandQueue();

		TileMap map = TileMap.getInstance();
		map.initMapWithRandomElements(new ResourceCallback<TileMap>() {

			@Override
			public void error(Throwable err) {
			}

			@Override
			public void done(TileMap map) {
				initPlayer();
				mapView = new MapView(player, map, sizeInPixels);
				gameLayer = createGameLayer(mapView, 0);
				registerInServer();
				registerHandlers();
			}

		});

	}

	protected void registerHandlers() {
		mainMessageHandler.register("move", new MessageHandler() {
			@Override
			public void onMessage(Object message) {
				int playerId = message.getInt("id");
				Player player = players.get(playerId);
				if (player == null) {
					player = createNewPlayer(playerId);
				}
				TemporalCoordinates from = new TemporalCoordinates();
				Object fromJson = message.getObject("start");
				from.setLocation(fromJson.getInt("x"), fromJson.getInt("y"));
				Turn turnStart = new Turn();
				turnStart.setTurnNumber(fromJson.getInt("turn"));
				from.setTurn(turnStart);
				TemporalCoordinates to = new TemporalCoordinates();
				Object toJson = message.getObject("end");
				to.setLocation(toJson.getInt("x"), toJson.getInt("y"));
				Turn turnEnd = new Turn();
				turnEnd.setTurnNumber(toJson.getInt("turn"));
				to.setTurn(turnEnd);
				commandQueue.addCommand(new MovetoCommand(from, to, player));
			}
		});
	}

	protected Player createNewPlayer(int playerId) {
		Player player = new Player();
		player.setCoordinates(new MapCoordinates(FIELD_WIDTH / 2, FIELD_HEIGHT / 2));
		player.setClothes(TileLibrary.getInstance().getTileLibraries().get("player").get("clothes"));
		player.setHead(TileLibrary.getInstance().getTileLibraries().get("player").get("male_head1"));
		player.setShield(TileLibrary.getInstance().getTileLibraries().get("player").get("shield"));
		player.setSword(TileLibrary.getInstance().getTileLibraries().get("player").get("longsword"));
		player.setId(playerId);
		players.put(playerId, player);
		return player;
	}

	private void registerInServer() {
		mainMessageHandler = new MainMessageHandler();
		mainMessageHandler.register("register", new MessageHandler() {
			@Override
			public void onMessage(Object message) {
				int playerId = message.getInt("id");
				int turnNumber = message.getInt("turn");
				player.setId(playerId);
				turn = new Turn();
				turn.setTurnTimestamp(PlayN.currentTime());
				turn.setTurnNumber(turnNumber);
				startGame();
			}
		});
		webSocket = PlayN.net().createWebSocket("ws://localhost:8080/websocket/ws", new WebSocket.Listener() {

			@Override
			public void onTextMessage(String msg) {
				mainMessageHandler.onMessage(msg);
			}

			@Override
			public void onOpen() {
				webSocket.send(new RegisterPlayer().toString());
			}

			@Override
			public void onError(String reason) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onDataMessage(ByteBuffer msg) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onClose() {
				// TODO Auto-generated method stub

			}
		});

	}

	private void startGame() {
		graphics().rootLayer().add(gameLayer);
		keyboard().setListener(this);
		initialized = true;
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
		if (initialized) {
			updateRenderingTime();
			updateTurn();
			commandQueue.update();
			updatePlayerMovement();
			commandQueue.performCommands(turn);
		}

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

	public static void sendMessage(String message) {
		webSocket.send(message);
	}

	public static Map<Integer, Player> getPlayers() {
		return players;
	}
}
