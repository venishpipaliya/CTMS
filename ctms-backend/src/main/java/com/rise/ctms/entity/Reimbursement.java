package com.rise.ctms.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "reimbursements", indexes = {
		@Index(name = "idx_reimb_status", columnList = "status")
})

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reimbursement {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@OneToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "expense_id", nullable = false, unique = true)
	private Expense expense;
	
	@Column(nullable = false, precision = 12, scale = 2)
	private BigDecimal amount;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 30)
	@Builder.Default
	private ReimbursementStatus status = ReimbursementStatus.PENDING;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "processed_by_id")
	private User processedBy;
	
	private LocalDateTime processedAt;
	
	@Column(length = 100)
	private String referenceNumber;
	
	@CreationTimestamp
	@Column(nullable = false, updatable = false)
	private LocalDateTime createdAt;
	
	@UpdateTimestamp
	private LocalDateTime updatedAt;
	
	
}
