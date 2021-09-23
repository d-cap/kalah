package com.kalah.util;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

public class CreationUtil {
	public static Map<String, String> createSingleMoveResult() {
		Map<String, String> pits = new TreeMap<>(Comparator.comparing(Integer::valueOf));
		pits.put("1", "0");
		pits.put("2", "7");
		pits.put("3", "7");
		pits.put("4", "7");
		pits.put("5", "7");
		pits.put("6", "7");
		pits.put("7", "1");
		pits.put("8", "6");
		pits.put("9", "6");
		pits.put("10", "6");
		pits.put("11", "6");
		pits.put("12", "6");
		pits.put("13", "6");
		pits.put("14", "0");
		return pits;
	}

	public static Map<String, String> createGameDefaultStatus() {
		Map<String, String> pits = new TreeMap<>(Comparator.comparing(Integer::valueOf));
		pits.put("1", "6");
		pits.put("2", "6");
		pits.put("3", "6");
		pits.put("4", "6");
		pits.put("5", "6");
		pits.put("6", "6");
		pits.put("7", "0");
		pits.put("8", "6");
		pits.put("9", "6");
		pits.put("10", "6");
		pits.put("11", "6");
		pits.put("12", "6");
		pits.put("13", "6");
		pits.put("14", "0");
		return pits;
	}

	public static Map<String, String> createEmptyStonePickup() {
		Map<String, String> pits = new TreeMap<>(Comparator.comparing(Integer::valueOf));
		pits.put("1", "0");
		pits.put("2", "2");
		pits.put("3", "4");
		pits.put("4", "0");
		pits.put("5", "10");
		pits.put("6", "10");
		pits.put("7", "3");
		pits.put("8", "1");
		pits.put("9", "0");
		pits.put("10", "0");
		pits.put("11", "10");
		pits.put("12", "9");
		pits.put("13", "9");
		pits.put("14", "14");
		return pits;
	}
}
