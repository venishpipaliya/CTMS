package com.rise.ctms.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "policies",indexes = {
		@Index(name = "idx_policy_role", columnList = "applied_to_role"),
		@Index(name = "idx_policy_active", columnList = "active")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Policy {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false, length = 200)
	private String name;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "applied_to_role", nullable = false, length = 20)
	private Role appliedToRole;
	
	@Column(nullable = false, precision = 12, scale = 2)
	private BigDecimal maxBudget;
	
	@Column(length = 50)
	private String maxTravelClass;
	
	@Column(nullable = false)
	@Builder.Default
	private boolean hardBlockOnViolation = false;
	
	@Column(nullable = false)
	@Builder.Default
	private boolean active = true;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "created_by_id")
	private User createdBy;
	
	@CreationTimestamp
	@Column(nullable = false, updatable = false)
	private LocalDateTime createdAt;
	
	@UpdateTimestamp
	private LocalDateTime updatedAt;
	
}
