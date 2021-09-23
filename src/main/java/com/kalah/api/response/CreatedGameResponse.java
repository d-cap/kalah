package com.kalah.api.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CreatedGameResponse {
	private final int id;
	private final String uri;
}
