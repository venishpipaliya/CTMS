package com.rise.ctms.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.rise.ctms.entity.RequestStatus;
import com.rise.ctms.entity.TravelRequest;
import com.rise.ctms.entity.User;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TravelResponseDto {

	private Long id;
	private Long employeeId;
	private String employeeName;
	private String employeeEmail;
	private String employeeDepartment;
	private String destination;
	private LocalDate startDate;
	private LocalDate endDate;
	private String purpose;
	private BigDecimal estimatedCost;
	private RequestStatus status;
	private Long managerId;
	private String managerName;
	private String managerEmail;
	private String managerDepartment;
	private Long financeId;
	private String financeName;
	private String financeEmail;
	private String managerComments;
	private String financeComments;
	private LocalDateTime createdAt;
	
	
	public static TravelResponseDto from(TravelRequest travelRequest) {
		return TravelResponseDto.builder()
				.id(travelRequest.getId())
				.employeeId(travelRequest.getEmployee().getId())
				.employeeName(travelRequest.getEmployee().getFirstName() + " " + travelRequest.getEmployee().getLastName())
				.employeeEmail(travelRequest.getEmployee().getEmail())
				.employeeDepartment(travelRequest.getEmployee().getDepartment())
				.destination(travelRequest.getDestination())
				.startDate(travelRequest.getStartDate())
				.endDate(travelRequest.getEndDate())
				.purpose(travelRequest.getPurpose())
				.estimatedCost(travelRequest.getEstimatedCost())
				.status(travelRequest.getStatus())
				.managerId(travelRequest.getManagerApprover() != null ? travelRequest.getManagerApprover().getId() : null)
				.managerName(travelRequest.getManagerApprover() != null
								? travelRequest.getManagerApprover().getFirstName() + " " +
									travelRequest.getManagerApprover().getLastName() : null)
				.managerEmail(travelRequest.getManagerApprover() != null ? travelRequest.getManagerApprover().getEmail() : null)
				.managerDepartment(travelRequest.getManagerApprover() != null ? travelRequest.getManagerApprover().getDepartment() : null)
				.financeId(travelRequest.getFinanceApprover() != null ? travelRequest.getFinanceApprover().getId() : null)
				.financeName(travelRequest.getFinanceApprover() != null
								? travelRequest.getFinanceApprover().getFirstName() + " " +
									travelRequest.getFinanceApprover().getLastName() : null)
				.financeEmail(travelRequest.getFinanceApprover() != null ? travelRequest.getFinanceApprover().getEmail() : null)
				.createdAt(travelRequest.getCreatedAt())	
				.managerComments(travelRequest.getManagerComments())
				.financeComments(travelRequest.getFinanceComments())
				.build();
	}
	
}
