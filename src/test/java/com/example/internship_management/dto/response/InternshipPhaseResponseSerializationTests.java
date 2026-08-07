package com.example.internship_management.dto.response;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import com.example.internship_management.entity.InternshipPhase;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

class InternshipPhaseResponseSerializationTests {

	private final ObjectMapper objectMapper = new ObjectMapper();

	@Test
	void internshipPhaseResponseOnlyExposesDtoFields() throws Exception {
		InternshipPhase phase = new InternshipPhase();
		phase.setPhaseId(1);
		phase.setPhaseName("Thực tập cơ sở 1");
		phase.setStartDate(LocalDate.of(2026, 8, 1));
		phase.setEndDate(LocalDate.of(2026, 12, 1));
		phase.setDescription("Giai đoạn thực tập cơ sở");

		JsonNode json = objectMapper.readTree(objectMapper.writeValueAsString(InternshipPhaseResponse.from(phase)));

		assertEquals(1, json.get("phaseId").asInt());
		assertEquals("Thực tập cơ sở 1", json.get("phaseName").asText());
		assertFalse(json.has("hibernateLazyInitializer"));
		assertFalse(json.has("handler"));
	}
}
