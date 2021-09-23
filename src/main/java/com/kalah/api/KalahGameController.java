package com.kalah.api;

import com.kalah.api.response.CreatedGameResponse;
import com.kalah.api.response.MoveResponse;
import com.kalah.api.response.error.ApiErrorResponse;
import com.kalah.game.KalahManager;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@AllArgsConstructor
@RestController()
@RequestMapping(Paths.GAME_PATH)
public class KalahGameController {

	private final KalahManager manager;

	@PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CreatedGameResponse> createGame(HttpServletRequest request) {
		var id = manager.createGame();
		return ResponseEntity.ok(new CreatedGameResponse(id, createGameUrl(request, id)));
	}


	@PostMapping(path = "/{gameId}/pits/{pitId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<MoveResponse> makeMove(
			HttpServletRequest request,
			@PathVariable() int gameId,
			@PathVariable() int pitId) {
		var game = manager.getGame(gameId);
		if (game == null) {
			throw new IllegalArgumentException("Game id is not valid");
		}
		game.makeMove(pitId);
		return ResponseEntity.ok(
				new MoveResponse(
						gameId,
						createGameUrl(request, gameId),
						game.getPits()
				)
		);
	}

	private String createGameUrl(HttpServletRequest request, int id) {
		return request.getRequestURL().toString() + Paths.GAME_PATH + "/" + id;
	}

	@ExceptionHandler(Exception.class)
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public @ResponseBody
	ApiErrorResponse handleException(IllegalArgumentException e) {
		return new ApiErrorResponse(e.getMessage());
	}
}
