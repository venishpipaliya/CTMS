	package com.rise.ctms.entity;
	
	import java.math.BigDecimal;
	import java.time.LocalDate;
	import java.time.LocalDateTime;
	import java.util.List;
	
	import org.hibernate.annotations.CreationTimestamp;
	import org.hibernate.annotations.UpdateTimestamp;
	import org.springframework.stereotype.Indexed;
	
	import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
	import lombok.Data;
import lombok.NoArgsConstructor;
	
	@Entity
	@Builder
	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	@Table(name = "travel_requests", indexes = {
			@Index(name = "idx_tr_status", columnList = "status"),
			@Index(name = "idx_tr_employee", columnList = "employee_id"),
			@Index(name = "idx_tr_dates", columnList = "startDate, endDate")
	})
	public class TravelRequest {
		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		private Long id;
		
		@ManyToOne(fetch = FetchType.LAZY,optional = false)
		@JoinColumn(name = "employee_id", nullable = false)
		private User employee;
		
		@Column(nullable = false, length = 200)
		private String destination;
		
		@Column(nullable = false)
		private LocalDate startDate;
		
		@Column(nullable = false)
		private LocalDate endDate;
		
		@Column(nullable = false, length = 500)
		private String purpose;
		
		@Column(nullable = false, precision = 12, scale = 2)
		private BigDecimal estimatedCost;
		
		@Enumerated(EnumType.STRING)
		@Column(nullable = false, length = 30)
		@Builder.Default
		private RequestStatus status = RequestStatus.DRAFT;
		
		@Column(length = 50)
		private String travelClass;
		
		@ManyToOne(fetch = FetchType.LAZY)
		@JoinColumn(name = "manager_approver_id")
		private User managerApprover;
		
		private LocalDateTime managerActionAt;
		
		@Column(length = 1000)
		private String managerComments;
		
		@ManyToOne(fetch = FetchType.LAZY)
		@JoinColumn(name = "finance_approver_id")
		private User financeApprover;
		
		private LocalDateTime financeActionAt;
		
		@Column(length = 1000)
		private String financeComments;
		
		@OneToMany(mappedBy = "travelRequest", cascade = CascadeType.ALL, orphanRemoval = true)
		@Builder.Default
		private List<Itinerary> itineraries = new java.util.ArrayList<>();
		
		@OneToMany(mappedBy = "travelRequest", cascade = CascadeType.ALL, orphanRemoval = true)
		@Builder.Default
		private List<Expense> expenses = new java.util.ArrayList<>();
		
		@CreationTimestamp
		@Column(nullable = false, updatable = false)
		private LocalDateTime createdAt;
		
		@UpdateTimestamp
		private LocalDateTime updatedAt;
		
		private LocalDateTime submittedAt;
		private LocalDateTime completedAt;
		
		
		
	
	}
