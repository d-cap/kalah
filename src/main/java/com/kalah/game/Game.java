package com.kalah.game;

import lombok.Getter;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Game {
	private static final int LAST_PIT_INDEX = 12;
	private static final int FIRST_PLAYER_FIRST_PIT_INDEX = 0;
	private static final int FIRST_PLAYER_HOUSE_INDEX = 6;
	private static final int SECOND_PLAYER_FIRST_PIT_INDEX = 7;
	private static final int SECOND_PLAYER_HOUSE_INDEX = 13;

	private final int[] pits;
	private int player;
	@Getter
	private boolean finished;

	Game() {
		this.pits = new int[]{
				6, 6, 6, 6, 6, 6, 0,
				6, 6, 6, 6, 6, 6, 0
		};
		this.player = 1;
		this.finished = false;
	}

	public void makeMove(int pitId) {
		validateNotFinishedGame();

		// Pit id is 1 based, pit index is 0 based
		var pitIndex = pitId - 1;

		validatePitIndex(pitIndex);

		var selectedPitStonesAmount = this.pits[pitIndex];
		this.pits[pitIndex] = 0;

		distributeStones(pitIndex, selectedPitStonesAmount);

		setPlayer(pitIndex, selectedPitStonesAmount);

		this.finished = this.checkFinishedAndCollectStones();
	}

	public Map<String, String> getPits() {
		return IntStream
				.range(0, this.pits.length)
				.boxed()
				.collect(
						Collectors.toMap(
								index -> String.valueOf(index + 1),
								index -> String.valueOf(this.pits[index]),
								(String k1, String k2) -> k2, // Merge function
								() -> new TreeMap<>(Comparator.comparing(Integer::valueOf))));
	}

	private void validateNotFinishedGame() {
		if (this.finished) {
			throw new IllegalStateException("Move is not possible for a finished game");
		}
	}

	private void validatePitIndex(int pitIndex) {
		if (pitIndex == FIRST_PLAYER_HOUSE_INDEX || pitIndex == SECOND_PLAYER_HOUSE_INDEX) {
			throw new IllegalArgumentException("House cannot be moved");
		} else if (this.player == 1 && (pitIndex < FIRST_PLAYER_FIRST_PIT_INDEX || pitIndex >= FIRST_PLAYER_HOUSE_INDEX)) {
			throw new IllegalArgumentException("Pit it not valid for player 1 (valid are: 1-6)");
		} else if (this.player == 2 && (pitIndex < SECOND_PLAYER_FIRST_PIT_INDEX || pitIndex >= SECOND_PLAYER_HOUSE_INDEX)) {
			throw new IllegalArgumentException("Pit it not valid for player 2 (valid are: 8-13)");
		} else if (this.pits[pitIndex] == 0) {
			throw new IllegalArgumentException(String.format("%s is an empty pit", pitIndex + 1));
		}
	}

	private void distributeStones(int pitIndex, int selectedPitStonesAmount) {
		var index = pitIndex + 1;
		var stoneToDistribute = selectedPitStonesAmount;
		while (stoneToDistribute > 0) {
			if (isNotOpponentHouse(index)) {
				if (shouldCaptureOpponentStones(index, stoneToDistribute)) {
					captureOpponentStones(index);
				} else {
					this.pits[index] += 1;
				}
				stoneToDistribute--;
			}
			index = setNextIndex(index);
		}
	}

	private void setPlayer(int pitIndex, int selectedPitStonesAmount) {
		boolean doubleTurn = isDoubleTurn(pitIndex, selectedPitStonesAmount);
		if (!doubleTurn) {
			this.player = this.player == 1 ? 2 : 1;
		}
	}

	private boolean isNotOpponentHouse(int index) {
		return !((this.player == 1 && index == SECOND_PLAYER_HOUSE_INDEX) ||
				(this.player == 2 && index == FIRST_PLAYER_HOUSE_INDEX));
	}

	private boolean shouldCaptureOpponentStones(int index, int stoneToDistribute) {
		return isLastStone(stoneToDistribute) && isAPit(index) &&
				isAnEmptyPit(index) && isAPlayerPit(index);
	}

	private boolean isLastStone(int stoneToDistribute) {
		return stoneToDistribute == 1;
	}

	private boolean isAPit(int index) {

		return index != FIRST_PLAYER_HOUSE_INDEX && index != SECOND_PLAYER_HOUSE_INDEX;
	}

	private boolean isAnEmptyPit(int index) {
		return this.pits[index] == 0;
	}

	private boolean isAPlayerPit(int index) {
		return (this.player == 1 && isIndexInRange(index, FIRST_PLAYER_FIRST_PIT_INDEX, FIRST_PLAYER_HOUSE_INDEX - 1)) ||
				(this.player == 2 && isIndexInRange(index, SECOND_PLAYER_FIRST_PIT_INDEX, SECOND_PLAYER_HOUSE_INDEX - 1));
	}

	private boolean isIndexInRange(int index, int min, int max) {
		return index >= min && index <= max;
	}

	private void captureOpponentStones(int pitIndex) {
		int opponentCorrespondingPitIndex = LAST_PIT_INDEX - pitIndex;
		var opponentCorrespondingPitStones = this.pits[opponentCorrespondingPitIndex];
		this.pits[opponentCorrespondingPitIndex] = 0;

		int houseIndex;
		if (this.player == 1) {
			houseIndex = FIRST_PLAYER_HOUSE_INDEX;
		} else {
			houseIndex = SECOND_PLAYER_HOUSE_INDEX;
		}
		this.pits[houseIndex] += 1 + opponentCorrespondingPitStones;
	}

	private int setNextIndex(int index) {
		if (index == SECOND_PLAYER_HOUSE_INDEX) {
			return 0;
		} else {
			return index + 1;
		}
	}

	private boolean isDoubleTurn(int pitIndex, int selectedPitStonesAmount) {
		return (this.player == 1 && selectedPitStonesAmount + pitIndex == FIRST_PLAYER_HOUSE_INDEX) ||
				(this.player == 2 && selectedPitStonesAmount + pitIndex == SECOND_PLAYER_HOUSE_INDEX);
	}

	private boolean checkFinishedAndCollectStones() {
		if (checkFinished(FIRST_PLAYER_FIRST_PIT_INDEX, FIRST_PLAYER_HOUSE_INDEX)) {
			collectPlayersStones(SECOND_PLAYER_FIRST_PIT_INDEX, SECOND_PLAYER_HOUSE_INDEX);
			return true;
		}
		if (checkFinished(SECOND_PLAYER_FIRST_PIT_INDEX, SECOND_PLAYER_HOUSE_INDEX)) {
			collectPlayersStones(FIRST_PLAYER_FIRST_PIT_INDEX, FIRST_PLAYER_HOUSE_INDEX);
			return true;
		}
		return false;
	}

	private boolean checkFinished(int startPidId, int houseId) {
		return IntStream
				.range(startPidId, houseId)
				.mapToObj(i -> this.pits[i] == 0)
				.reduce(true, (a, b) -> a && b);
	}

	private void collectPlayersStones(int firstPitIndex, int houseIndex) {
		IntStream.range(firstPitIndex, houseIndex).forEach(i -> {
			this.pits[houseIndex] += this.pits[i];
			this.pits[i] = 0;
		});
	}
}
