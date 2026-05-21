package com.rise.ctms.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "notifications", indexes = {
		@Index(name = "idx_noif_recipient", columnList = "recipient_id,read_flag"),
		
})

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "recipient_id", nullable = false)
	private User recipient;
	
	@Column(nullable = false, length = 200)
	private String title;
	
	@Column(nullable = false, length = 1000)
	private String message;
	
	@Column(length = 1000)
	private String linkUrl;
	
	@Column(name = "read_flag", nullable = false)
	@Builder.Default
	private boolean read = false;
	
	@CreationTimestamp
	@Column(nullable = false, updatable = false)
	private LocalDateTime createdAt;
	
	private LocalDateTime readAt;
	
}
