package com.rise.ctms.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
//import org.springframework.security.core.userdetails.User;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "expenses", indexes = {
		@Index(name = "idx_exp_request", columnList = "travel_request_id"),
		@Index(name = "idx_exp_status", columnList = "status")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Expense {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "travel_request_id", nullable = false)
	private TravelRequest travelRequest;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 30)
	private Category category;
	
	@Column(nullable = false, precision = 12, scale = 2)
	private BigDecimal claimedAmount;
	
	@Column(precision = 12, scale = 2)
	private String approvedaAmount;
	
	@Column(nullable = false, length = 50)
	private Category description;
	
	@Column(nullable = false)
	private LocalDate expenseDate;
	
	@Column(length = 500)
	private String reciptPath;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 30)
	@Builder.Default
	private ExpenseStatus status = ExpenseStatus.PENDING;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="approved_by_id")
	private User approvedBy;
	
	private LocalDateTime approvedAt;
	
	@Column(length = 1000)
	private String financeComment;
	
	@Enumerated(EnumType.STRING)
//	@OneToMany(mappedBy = "expense", cascade = CascadeType.ALL, orphanRemoval = true,
//			fetch = FetchType.LAZY)
	private ReimbursementStatus reimbursent;

//	private List<ReimbursementStatus> reimbursent = new ArrayList<>();
	
	@CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;


public enum Category {

    FOOD,
    STAY,
    TRANSPORT,
    MISCELLANEOUS

}
	

	
	
	
	
	

}
