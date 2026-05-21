package com.rise.ctms.dto.response;

import java.time.LocalDateTime;

import com.rise.ctms.entity.Role;
import com.rise.ctms.entity.User;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserResponseDto {
	private Long id;
	private String firstName;
	private String lastName;
	private String email;
	private Role role;
	private String department;
	private Long managerId;
	private String managerName;
	private boolean enabled;
	private LocalDateTime createdAt;
	
	
	public static UserResponseDto from(User user) {
		return UserResponseDto.builder()
				.id(user.getId())
				.firstName(user.getFirstName())
				.lastName(user.getLastName())
				.email(user.getEmail())
				.role(user.getRole())
				.department(user.getDepartment())
				.managerId(user.getManager() != null ? user.getManager().getId() : null)
				.managerName(user.getManager() != null
							? user.getManager().getFirstName() + " " + user.getManager().getLastName()
							: null)
				.enabled(user.isEnabled())
				.createdAt(user.getCreatedAt())
				.build();
	}
	
	
	

}
