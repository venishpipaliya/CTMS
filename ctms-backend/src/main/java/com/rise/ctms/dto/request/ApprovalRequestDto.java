package com.rise.ctms.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ApprovalRequestDto {
	
	@NotBlank(message = "commnet is required")
	private String comment;

}
