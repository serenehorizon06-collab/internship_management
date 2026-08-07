package com.example.internship_management.dto.response;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import com.example.internship_management.entity.EvaluationCriterion;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

class EvaluationCriterionResponseSerializationTests {

	private final ObjectMapper objectMapper = new ObjectMapper();

	@Test
	void evaluationCriterionResponseOnlyExposesDtoFields() throws Exception {
		EvaluationCriterion criterion = new EvaluationCriterion();
		criterion.setCriterionId(1);
		criterion.setCriterionName("ThĂ¡i Ä‘á»™ lĂ m viá»‡c");
		criterion.setDescription("ÄĂ¡nh giĂ¡ thĂ¡i Ä‘á»™ vĂ  trĂ¡ch nhiá»‡m");
		criterion.setMaxScore(new BigDecimal("10.00"));

		JsonNode json = objectMapper.readTree(objectMapper.writeValueAsString(EvaluationCriterionResponse.from(criterion)));

		assertEquals(1, json.get("criterionId").asInt());
		assertEquals("ThĂ¡i Ä‘á»™ lĂ m viá»‡c", json.get("criterionName").asText());
		assertEquals(10.00, json.get("maxScore").asDouble());
		assertFalse(json.has("hibernateLazyInitializer"));
		assertFalse(json.has("handler"));
	}
}
