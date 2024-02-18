package com.racemusconsulting.aircraftenvironmentallimitationsbackend.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.BDDMockito.given;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.racemusconsulting.aircraftenvironmentallimitationsbackend.dtos.TemperatureDeviationRequestDTO;
import com.racemusconsulting.aircraftenvironmentallimitationsbackend.dtos.TemperatureDeviationResponseDTO;
import com.racemusconsulting.aircraftenvironmentallimitationsbackend.services.TemperatureDeviationService;

@ExtendWith(SpringExtension.class)
@WebMvcTest(TemperatureDeviationController.class)
public class TemperatureDeviationControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private TemperatureDeviationService temperatureDeviationService;

	@Test
	public void whenPostRequestToTemperatureDeviation_andValidRequest_thenResponseStatusOk() throws Exception {
		// Given
		TemperatureDeviationRequestDTO requestDTO = new TemperatureDeviationRequestDTO();
		requestDTO.setAcModel("A320");
		requestDTO.setAltitude(10000.0);
		requestDTO.setAeroPhase("CRUISE");

		TemperatureDeviationResponseDTO responseDTO = new TemperatureDeviationResponseDTO();
		responseDTO.setMinTemperature(10.0);
		responseDTO.setMaxTemperature(20.0);

		given(temperatureDeviationService.getTemperatureDeviation("A320", 10000.0, "CRUISE")).willReturn(responseDTO);

		// When & Then
		mockMvc.perform(post("/performance/temperatureDeviation").contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(requestDTO))).andExpect(status().isOk());
	}
}
