package com.example.internship_management.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateEvaluationCriterionRequest {

	@NotBlank(message = "TĂªn tiĂªu chĂ­ Ä‘Ă¡nh giĂ¡ khĂ´ng Ä‘Æ°á»£c Ä‘á»ƒ trá»‘ng")
	@Size(max = 200, message = "TĂªn tiĂªu chĂ­ Ä‘Ă¡nh giĂ¡ khĂ´ng Ä‘Æ°á»£c vÆ°á»£t quĂ¡ 200 kĂ½ tá»±")
	private String criterionName;

	private String description;

	@NotNull(message = "Äiá»ƒm tá»‘i Ä‘a khĂ´ng Ä‘Æ°á»£c Ä‘á»ƒ trá»‘ng")
	@DecimalMin(value = "0.0", inclusive = false, message = "Äiá»ƒm tá»‘i Ä‘a pháº£i lá»›n hÆ¡n 0")
	private BigDecimal maxScore;
}
