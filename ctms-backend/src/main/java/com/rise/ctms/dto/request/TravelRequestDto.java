package com.rise.ctms.dto.request;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class TravelRequestDto {
	
	@NotBlank(message = "Destination is required")
	@Size(max = 200, message = "Destination must not exceed 200 characters")
	private String destination;
	
	@NotNull(message = "Start date is required")
	private LocalDate startDate;
	
	@NotNull(message = "End date is required")
	private LocalDate endDate;
	
	@NotBlank(message = "Purpose is required")
	@Size(max = 255, message = "Purpose must not exceed 255 characters")
	private String purpose;
	
	@NotNull(message = "Estimate cost is required")
	private BigDecimal estimatedCost;
	
	
	
	
	
	
	
	
}
