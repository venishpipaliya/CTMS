package com.rise.ctms.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rise.ctms.dto.request.ApprovalRequestDto;
import com.rise.ctms.dto.request.TravelRequestDto;
import com.rise.ctms.dto.response.ApiResponse;
import com.rise.ctms.dto.response.TravelResponseDto;
import com.rise.ctms.service.TravelRequestServices;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/request")
@RequiredArgsConstructor
public class TravelRequestController {
	
	private final TravelRequestServices travelRequestServices;
	
//	public TravelRequestController(TravelRequestServices travelRequestServices) {
//		this.travelRequestServices = travelRequestServices;
//	}

	@PostMapping("/{id}/draft")
	public ResponseEntity<ApiResponse<TravelResponseDto>> saveDraft(@PathVariable Long id ,@Valid @RequestBody TravelRequestDto dto){
		TravelResponseDto responseDto = travelRequestServices.draftRequest(dto, id);
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(ApiResponse.success(responseDto, "Draft request submitted successfully"));
	}
	
	@PutMapping("/{id}/update")
	public ResponseEntity<ApiResponse<TravelResponseDto>> updateDraft(@PathVariable Long id ,@Valid @RequestBody TravelRequestDto dto){
		TravelResponseDto responseDto = travelRequestServices.updateDraftRequest(dto, id);
		return ResponseEntity.status(HttpStatus.OK)
				.body(ApiResponse.success(responseDto, "Draft request updated successfully"));
	}
	
	@PutMapping("/{id}/submit")
	public ResponseEntity<ApiResponse<TravelResponseDto>> submitRequest(@PathVariable Long id){
		TravelResponseDto responseDto = travelRequestServices.submitRequest(id);
		return ResponseEntity.status(HttpStatus.OK)
				.body(ApiResponse.success(responseDto, "Submitted successfully"));
	}
	
	
	@PutMapping("/{id}/cancle")
	public ResponseEntity<ApiResponse<TravelResponseDto>> cancleRequest(@PathVariable Long id){
		TravelResponseDto responseDto = travelRequestServices.cancleRequest(id);
		return ResponseEntity.status(HttpStatus.OK)
				.body(ApiResponse.success(responseDto, "Cancelled successfully"));
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse<TravelResponseDto>> getRequestByEmployeeId(@PathVariable Long id){
		TravelResponseDto responseDto = travelRequestServices.getRequestById(id);
		return ResponseEntity.status(HttpStatus.OK)
				.body(ApiResponse.success(responseDto, "Get request by employee id successfully"));
	}
	
	@GetMapping("/getAllRequests")
	public ResponseEntity<ApiResponse<Object>> getAllRequests(){
		return ResponseEntity.status(HttpStatus.OK)
				.body(ApiResponse.success(travelRequestServices.getAllRequests(), "Get all requests successfully"));
	}
	
	@GetMapping("/getEmployeeRequests/{employeeId}")
	public ResponseEntity<ApiResponse<List<TravelResponseDto>>> getEmployeeRequests(@PathVariable Long employeeId){
		return ResponseEntity.status(HttpStatus.OK)
				.body(ApiResponse.success(travelRequestServices.getRequestsByEmployeeId(employeeId), "Get employee requests successfully"));
	}
	
	@GetMapping("/getSubmittedRequestsByManager/{managerId}")
	public ResponseEntity<ApiResponse<List<TravelResponseDto>>> getSubmittedRequestsByManager(@PathVariable Long managerId){
		return ResponseEntity.status(HttpStatus.OK)
				.body(ApiResponse.success(travelRequestServices.getSubmittedRequestsByManagerId(managerId), "Get submitted requests by manager id successfully"));
	}
	
	// manager approval feature
	@PostMapping("/{requestId}/manager/{managerId}/approve")
	public ResponseEntity<ApiResponse<TravelResponseDto>> managerApproveRequest(@PathVariable Long requestId, @PathVariable Long managerId, @Valid @RequestBody ApprovalRequestDto dto){
		TravelResponseDto responseDto = travelRequestServices.approvedByManager(requestId, managerId, dto);
		return ResponseEntity.status(HttpStatus.OK)
				.body(ApiResponse.success(responseDto, "Manager approved request successfully"));
	}
	
	// manager reject feature
	@PostMapping("/{requestId}/manager/{managerId}/reject")
	public ResponseEntity<ApiResponse<TravelResponseDto>> managerRejectRequest(@PathVariable Long requestId, @PathVariable Long managerId, @Valid @RequestBody ApprovalRequestDto dto){
		TravelResponseDto responseDto = travelRequestServices.rejectedByManager(requestId, managerId, dto);
		return ResponseEntity.status(HttpStatus.OK)
				.body(ApiResponse.success(responseDto, "Manager reject request successfully"));
	}
		
	// finance approval feature
	@PostMapping("/{requestId}/finance/approve")
	public ResponseEntity<ApiResponse<TravelResponseDto>> financeApproveRequest(@PathVariable Long requestId, @Valid @RequestBody ApprovalRequestDto dto){
		TravelResponseDto responseDto = travelRequestServices.approvedByFininance(requestId, dto);
		return ResponseEntity.status(HttpStatus.OK)
				.body(ApiResponse.success(responseDto, "Finance approved request successfully"));
	}
	
	// finance rejected feature
	@PostMapping("/{requestId}/finance/reject")
	public ResponseEntity<ApiResponse<TravelResponseDto>> financeRejectRequest(@PathVariable Long requestId, @Valid @RequestBody ApprovalRequestDto dto){
		TravelResponseDto responseDto = travelRequestServices.rejectByFininance(requestId, dto);
		return ResponseEntity.status(HttpStatus.OK)
				.body(ApiResponse.success(responseDto, "Finance Reject request successfully"));
	}

}
