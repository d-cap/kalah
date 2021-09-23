package com.kalah.game;

import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class KalahManager {
	private final AtomicInteger idGenerator;
	private final Map<Integer, Game> games;

	public KalahManager() {
		idGenerator = new AtomicInteger(0);
		games = Collections.synchronizedMap(new TreeMap<>());
	}

	public int createGame() {
		var id = idGenerator.getAndIncrement();
		var game = new Game();
		games.put(id, game);
		return id;
	}

	public Game getGame(int gameId) {
		return games.get(gameId);
	}
}
