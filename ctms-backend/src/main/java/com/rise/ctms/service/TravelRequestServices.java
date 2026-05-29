package com.rise.ctms.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import com.rise.ctms.dto.request.ApprovalRequestDto;
import com.rise.ctms.dto.request.TravelRequestDto;
import com.rise.ctms.dto.response.TravelResponseDto;
import com.rise.ctms.entity.RequestStatus;
import com.rise.ctms.entity.Role;
import com.rise.ctms.entity.TravelRequest;
import com.rise.ctms.entity.User;
import com.rise.ctms.exception.ResourceConflictException;
import com.rise.ctms.repository.TravelRequestRepository;
import com.rise.ctms.repository.UserRepository;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
@Validated
public class TravelRequestServices {
	
	private final TravelRequestRepository travelRequestRepository;
	private final UserRepository userRepository;
	
	
	public TravelResponseDto draftRequest(TravelRequestDto dto, Long employeeId) {
		
		User employee = userRepository.findById(employeeId)
				.orElseThrow(() -> new ResourceConflictException("Emloyee Id not found" + employeeId));
	
		TravelRequest travelRequest = TravelRequest.builder()
				.employee(employee)
				.destination(dto.getDestination())
				.startDate(dto.getStartDate())
				.endDate(dto.getEndDate())
				.purpose(dto.getPurpose())
				.travelClass(dto.getTravelClass())
				.estimatedCost(dto.getEstimatedCost())
				.status(RequestStatus.DRAFT)				
				.build();		
		
		TravelRequest draftRequest = travelRequestRepository.save(travelRequest);
		
		log.info("Draft travel request created id={} employeeId={}", draftRequest.getId(), draftRequest.getEmployee().getId());
		
		return TravelResponseDto.from(draftRequest);
	}
	
	public TravelResponseDto updateDraftRequest(@Valid TravelRequestDto dto, Long id) {
		
		TravelRequest travelRequest = travelRequestRepository.findById(id)
				.orElseThrow(() -> new ResourceConflictException("Travel request not found with id: " + id));
		
		if (travelRequest.getStatus() != RequestStatus.DRAFT) {
			throw new ResourceConflictException("Only draft requests can be updated. Current status: " + travelRequest.getStatus());
		}
		
		travelRequest.setDestination(dto.getDestination());
		travelRequest.setStartDate(dto.getStartDate());
		travelRequest.setEndDate(dto.getEndDate());
		travelRequest.setPurpose(dto.getPurpose());
		travelRequest.setTravelClass(dto.getTravelClass());
		travelRequest.setEstimatedCost(dto.getEstimatedCost());
		travelRequest.setUpdatedAt(LocalDateTime.now());
		
		TravelRequest updatedDraft = travelRequestRepository.save(travelRequest);
		
		log.info("Draft travel request updated id={} employeeId={}", updatedDraft.getId(), updatedDraft.getEmployee().getId());
		
		return TravelResponseDto.from(updatedDraft);
	}


	public TravelResponseDto submitRequest(Long id) {
		
		TravelRequest travelRequest = travelRequestRepository.findById(id)
				.orElseThrow(() -> new ResourceConflictException("Travel request not found with id: " + id));
		
		if (travelRequest.getStatus() != RequestStatus.DRAFT) {
			throw new ResourceConflictException("Only draft requests can be submitted. Current status: " + travelRequest.getStatus());
		}
		
		User employee = travelRequest.getEmployee();
		if(employee.getManager() == null) {
			throw new ResourceConflictException("Employee does not have a manager assigned. Cannot submit request.");
		}
		
		travelRequest.setStatus(RequestStatus.SUBMITTED);
		travelRequest.setManagerApprover(employee.getManager());
		travelRequest.setSubmittedAt(LocalDateTime.now());
		travelRequest.setUpdatedAt(LocalDateTime.now());
		
		TravelRequest submitRequest = travelRequestRepository.save(travelRequest);
		
		log.info("Travel request submitted id={} employeeId={} managerId={}", submitRequest.getId(), submitRequest.getEmployee().getId(), submitRequest.getManagerApprover().getId());
		
		return TravelResponseDto.from(submitRequest);
		
		
	}
	
public TravelResponseDto cancleRequest(Long id) {
		
		TravelRequest travelRequest = travelRequestRepository.findById(id)
				.orElseThrow(() -> new ResourceConflictException("Travel request not found with id: " + id));
		
		if (travelRequest.getStatus() != RequestStatus.DRAFT && travelRequest.getStatus() != RequestStatus.SUBMITTED) {
			throw new ResourceConflictException("Only draft or submitted requests can be canclled. Current status: " + travelRequest.getStatus());
		}
		
		
		
		travelRequest.setStatus(RequestStatus.CANCELLED);
		travelRequest.setUpdatedAt(LocalDateTime.now());
		
		TravelRequest cancleRequest = travelRequestRepository.save(travelRequest);
		
		log.info("Travel request cancelled id={} employeeId={}", cancleRequest.getId(), cancleRequest.getEmployee().getId() );
		
		return TravelResponseDto.from(cancleRequest);
		
		
	}


	public TravelResponseDto getRequestById(Long id) {
		TravelRequest travelRequest = travelRequestRepository.findById(id)
				.orElseThrow(() -> new ResourceConflictException("Travel request not found with id: " + id));
		
		return TravelResponseDto.from(travelRequest);
	}


	public Object getAllRequests() {
		return travelRequestRepository.findAll().stream()
				.map(TravelResponseDto::from)
				.toList();
	}


	public List<TravelResponseDto> getRequestsByEmployeeId(Long employeeId) {
		return travelRequestRepository.findByEmployee_Id(employeeId).stream()
				.map(TravelResponseDto::from)
				.toList();
	}
	
	public List<TravelResponseDto> getSubmittedRequestsByManagerId(Long managerId) {
		return travelRequestRepository.findSubmittedRequestsByManagerId(managerId).stream()
				.map(TravelResponseDto::from)
				.toList();
	}
	
	// manager approval feature
	
	public TravelResponseDto approvedByManager(Long requestId,Long managerId, ApprovalRequestDto dto) {
		TravelRequest travelRequest = travelRequestRepository.findById(requestId)
				.orElseThrow(() -> new ResourceConflictException("Travel request not found with id: " + requestId));
		
		if (travelRequest.getManagerApprover() == null || !travelRequest.getManagerApprover().getId().equals(managerId)) {
			throw new ResourceConflictException("Manager is not authorized to approve this request." + managerId);
			
		}
		
		if (travelRequest.getStatus() != RequestStatus.SUBMITTED) {
			throw new ResourceConflictException("Only submitted requests can be approved. Current status: " + travelRequest.getStatus());
		}
		
		travelRequest.setStatus(RequestStatus.MANAGER_APPROVED);
		travelRequest.setManagerComments(dto.getComment());
		travelRequest.setManagerActionAt(LocalDateTime.now());
		travelRequest.setUpdatedAt(LocalDateTime.now());
		
		TravelRequest approvedRequest = travelRequestRepository.save(travelRequest);
		
		log.info("Travel request approved by manager id={} employeeId={} managerId={}", approvedRequest.getId(), approvedRequest.getEmployee().getId(), approvedRequest.getManagerApprover().getId());
		
		return TravelResponseDto.from(approvedRequest);
	}
	
	// manager reject feature
	
		public TravelResponseDto rejectedByManager(Long requestId,Long managerId, ApprovalRequestDto dto) {
			TravelRequest travelRequest = travelRequestRepository.findById(requestId)
					.orElseThrow(() -> new ResourceConflictException("Travel request not found with id: " + requestId));
			
			if (travelRequest.getManagerApprover() == null || !travelRequest.getManagerApprover().getId().equals(managerId)) {
				throw new ResourceConflictException("Manager is not authorized to approve this request." + managerId);
				
			}
			
			if (travelRequest.getStatus() != RequestStatus.SUBMITTED) {
				throw new ResourceConflictException("Only submitted requests can be approved. Current status: " + travelRequest.getStatus());
			}
			
			travelRequest.setStatus(RequestStatus.REJECTED);
			travelRequest.setManagerComments(dto.getComment());
			travelRequest.setManagerActionAt(LocalDateTime.now());
			travelRequest.setUpdatedAt(LocalDateTime.now());
			
			TravelRequest approvedRequest = travelRequestRepository.save(travelRequest);
			
			log.info("Travel request rejected By manager id={} employeeId={} managerId={}", approvedRequest.getId(), approvedRequest.getEmployee().getId(), approvedRequest.getManagerApprover().getId());
			
			return TravelResponseDto.from(approvedRequest);
		}
		
		// finance approval feature
		
		public TravelResponseDto approvedByFininance(Long requestId, ApprovalRequestDto dto) {
			TravelRequest travelRequest = travelRequestRepository.findById(requestId)
					.orElseThrow(() -> new ResourceConflictException("Travel request not found with id: " + requestId));
			
			if (travelRequest.getStatus() != RequestStatus.MANAGER_APPROVED) {
				throw new ResourceConflictException("Only manager approved requests can be approved by finance. Current status: " + travelRequest.getStatus());
			}
			
			travelRequest.setFinanceApprover(userRepository.findByDepartmentAndRole("Finance", Role.MANAGER)
					.orElseThrow(() -> new ResourceConflictException("Finance user not found")));
			
			
			travelRequest.setStatus(RequestStatus.FINANCE_APPROVED);
			travelRequest.setFinanceComments(dto.getComment());
			travelRequest.setFinanceActionAt(LocalDateTime.now());
			travelRequest.setUpdatedAt(LocalDateTime.now());
			
			TravelRequest approvedRequest = travelRequestRepository.save(travelRequest);
			
			log.info("Travel request approved by finance id={} employeeId={} financeId={}", approvedRequest.getId(), approvedRequest.getEmployee().getId(), approvedRequest.getFinanceApprover().getId());
			
			return TravelResponseDto.from(approvedRequest);
		}
		
		// finance Rejected feature
		
			public TravelResponseDto rejectByFininance(Long requestId, ApprovalRequestDto dto) {
				TravelRequest travelRequest = travelRequestRepository.findById(requestId)
						.orElseThrow(() -> new ResourceConflictException("Travel request not found with id: " + requestId));
				
				if (travelRequest.getStatus() != RequestStatus.MANAGER_APPROVED) {
					throw new ResourceConflictException("Only manager approved requests can be Reject by finance. Current status: " + travelRequest.getStatus());
				}
				
				travelRequest.setFinanceApprover(userRepository.findByDepartmentAndRole("Finance", Role.MANAGER)
						.orElseThrow(() -> new ResourceConflictException("Finance user not found ")));
				
				
				travelRequest.setStatus(RequestStatus.REJECTED);
				travelRequest.setFinanceComments(dto.getComment());
				travelRequest.setFinanceActionAt(LocalDateTime.now());
				travelRequest.setUpdatedAt(LocalDateTime.now());
				
				TravelRequest approvedRequest = travelRequestRepository.save(travelRequest);
				
				log.info("Travel request rejected by finance id={} employeeId={} financeId={}", approvedRequest.getId(), approvedRequest.getEmployee().getId(), approvedRequest.getFinanceApprover().getId());
				
				return TravelResponseDto.from(approvedRequest);
			}


			
	
	

}
