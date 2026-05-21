package com.rise.ctms.dto.request;

import com.rise.ctms.entity.Role;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateUserDto {

	@NotBlank(message = "First Name is requird")
	@Size(max = 100, message = "First name must not exceed 100 character")
	private String firstName;
	
	@NotBlank(message = "Last Name is requird")
	@Size(max = 100, message = "Last name must not exceed 100 character")
	private String lastName;
	
	@NotBlank(message = "Email is requird")
	@Email(message = "Invalid email format")
	@Size(max = 100)
	private String email;
	
	
	@NotNull(message = "Role is required")
	private Role role;
	
	@Size(max = 100)
	private String department;
	
	private Long managerId;
	
	private boolean enabled;
}
