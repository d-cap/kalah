package com.kalah.game;

import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.stream.IntStream;

import static com.kalah.util.CreationUtil.createEmptyStonePickup;
import static com.kalah.util.CreationUtil.createGameDefaultStatus;
import static org.junit.jupiter.api.Assertions.*;

class GameTest {
	@Test
	void shouldCreateDefaultGameStatus() {
		var game = new Game();
		assertFalse(game.isFinished());
		assertEquals(createGameDefaultStatus(), game.getPits());
	}

	@Test
	void shouldNotBeAbleToMoveOpponentPits() {
		var game = new Game();
		assertThrows(IllegalArgumentException.class,
				() -> game.makeMove(8),
				"Player 1 should not be able to move player 2 pits");
		game.makeMove(2);
		assertThrows(IllegalArgumentException.class,
				() -> game.makeMove(2),
				"Player 2 should not be able to move player 1 pits");
		game.makeMove(8);
	}


	@Test
	void shouldNotBeAbleToMoveHouses() {
		var game = new Game();
		assertThrows(IllegalArgumentException.class,
				() -> game.makeMove(7),
				"Should not be possible to move house stones");
		assertThrows(IllegalArgumentException.class,
				() -> game.makeMove(14),
				"Should not be possible to move house stones");
	}

	@Test
	void shouldAllowDoubleMove() {
		var game = new Game();
		game.makeMove(1);
		assertDoesNotThrow(
				() -> game.makeMove(6),
				"Should allow a double move for player 1");
	}

	@Test
	void shouldCaptureOpponentPlayerStones() {
		var game = new Game();
		var moves = new int[]{1, 3, 8, 1, 9, 2, 10, 1, 9};
		for (int move : moves) {
			game.makeMove(move);
		}
		assertEquals(createEmptyStonePickup(), game.getPits());
	}

	@Test
	void shouldNotCapturePlayerStoneWhenNotOpponentPit() {
		var game = new Game();
		var moves = new int[]{1, 3, 12, 5, 11, 2};
		for (int move : moves) {
			game.makeMove(move);
			System.out.println("move: " + move + ", " + game.getPits());
		}
		assertEquals("3", game.getPits().get("3"));
	}

	@Test
	void shouldNotBeAbleToSelectPitWithoutStones() {
		var game = new Game();
		game.makeMove(2);
		game.makeMove(9);
		var exception = assertThrows(IllegalArgumentException.class,
				() -> game.makeMove(2),
				"Should not be possible to move an empty pit");
		assertEquals("2 is an empty pit", exception.getMessage());
	}

	@Test
	void shouldNotAllowOutOfRangePits() {
		var game = new Game();
		assertThrows(IllegalArgumentException.class,
				() -> game.makeMove(0),
				"Should not be possible to move a pit with wrong id");
		assertThrows(IllegalArgumentException.class,
				() -> game.makeMove(7),
				"Should not be possible to move a pit with wrong id");
		game.makeMove(2);
		assertThrows(IllegalArgumentException.class,
				() -> game.makeMove(7),
				"Should not be possible to move a pit with wrong id");
		assertThrows(IllegalArgumentException.class,
				() -> game.makeMove(15),
				"Should not be possible to move a pit with wrong id");
	}

	public static final int[] FINISHED_GAME_MOVES = {1, 3, 8, 1, 9, 2, 10, 1, 9, 2, 8, 6, 13, 1, 11, 3, 9, 1, 10, 11, 13, 4, 5, 9, 3, 8, 2, 12, 6, 12, 11, 9, 3, 8, 1, 10, 4, 5, 9, 2, 8, 1, 11, 3, 12, 6};

	@Test
	void shouldFinishGame() {
		var game = new Game();
		for (int move : FINISHED_GAME_MOVES) {
			game.makeMove(move);
		}
		assertTrue(game.isFinished());
	}


	@Test
	void shouldNotAllowToMakeAMoveWhenGameIsFinished() {
		var game = new Game();
		for (int move : FINISHED_GAME_MOVES) {
			game.makeMove(move);
		}
		var exception = assertThrows(IllegalStateException.class,
				() -> game.makeMove(1),
				"Should not be able to make a move when the game is finished"
		);
		assertEquals("Move is not possible for a finished game", exception.getMessage());
	}

	@Test
	void shouldHasAllEmptyPit() {
		var game = new Game();
		for (int move : FINISHED_GAME_MOVES) {
			game.makeMove(move);
		}

		Map<String, String> pits = game.getPits();
		IntStream.range(1, 6).forEach(i -> assertEquals("0", pits.get(String.valueOf(i))));
		IntStream.range(8, 13).forEach(i -> assertEquals("0", pits.get(String.valueOf(i))));
	}
}