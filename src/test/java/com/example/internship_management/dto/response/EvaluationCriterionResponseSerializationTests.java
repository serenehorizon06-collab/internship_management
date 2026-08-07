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
		criterion.setCriterionName("Thái độ làm việc");
		criterion.setDescription("Đánh giá thái độ và trách nhiệm");
		criterion.setMaxScore(new BigDecimal("10.00"));

		JsonNode json = objectMapper.readTree(objectMapper.writeValueAsString(EvaluationCriterionResponse.from(criterion)));

		assertEquals(1, json.get("criterionId").asInt());
		assertEquals("Thái độ làm việc", json.get("criterionName").asText());
		assertEquals(10.00, json.get("maxScore").asDouble());
		assertFalse(json.has("hibernateLazyInitializer"));
		assertFalse(json.has("handler"));
	}
}
