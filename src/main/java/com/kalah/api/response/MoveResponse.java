package com.kalah.api.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

@AllArgsConstructor
@Getter
public class MoveResponse {
	private final int id;
	private final String uri;
	private final Map<String, String> status;
}
