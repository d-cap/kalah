package com.kalah.api.response.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ApiErrorResponse {
	private final String error;
}
