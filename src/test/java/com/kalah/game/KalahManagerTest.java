package com.kalah.game;

import com.kalah.util.CreationUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class KalahManagerTest {
	private KalahManager manager;
	private int gameId;

	@BeforeEach
	void setup() {
		manager = new KalahManager();
		gameId = manager.createGame();
	}

	@Test
	void shouldCreateGame() {
		var game = manager.getGame(gameId);
		assertNotNull(game);
	}

	@Test
	void shouldMakeMove() {
		var game = manager.getGame(gameId);
		game.makeMove(1);
		Map<String, String> pits = CreationUtil.createSingleMoveResult();
		assertEquals(pits, game.getPits());
	}

	@Test
	void shouldNotAbleToFineGameWithInvalidId() {
		assertNull(manager.getGame(-1));
		assertNull(manager.getGame(2));
	}

}