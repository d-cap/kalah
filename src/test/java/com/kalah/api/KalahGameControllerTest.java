package com.kalah.api;

import com.kalah.api.response.CreatedGameResponse;
import com.kalah.api.response.MoveResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static com.kalah.util.CreationUtil.createSingleMoveResult;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class KalahGameControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void shouldCreateGame() throws Exception {
		this.mockMvc.perform(post("/games")).andExpect(status().isOk());
	}


	@Test
	void shouldMakeMove() throws Exception {
		var result = this.mockMvc.perform(post("/games")).andReturn();

		ObjectMapper objectMapper = new ObjectMapper();
		var game = objectMapper.readValue(result.getResponse().getContentAsString(), CreatedGameResponse.class);
		var url = "/games/{gameId}/pits/{pitId}";
		result = this.mockMvc.perform(post(url, game.getId(), 1)).andExpect(status().isOk()).andReturn();
		var move = objectMapper.readValue(result.getResponse().getContentAsString(), MoveResponse.class);
		assertEquals(createSingleMoveResult(), move.getStatus());
	}

	@Test
	void shouldReturnErrorForMakeMoveWithWrongInput() throws Exception {
		var result = this.mockMvc.perform(post("/games")).andReturn();

		ObjectMapper objectMapper = new ObjectMapper();
		var game = objectMapper.readValue(result.getResponse().getContentAsString(), CreatedGameResponse.class);
		var url = "/games/{gameId}/pits/{pitId}";
		this.mockMvc.perform(post(url, game.getId(), 8)).andExpect(status().isBadRequest());
	}
}