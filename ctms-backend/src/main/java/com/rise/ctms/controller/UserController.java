package com.rise.ctms.controller;


import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rise.ctms.dto.request.CreateUserDto;
import com.rise.ctms.dto.request.UpdateUserDto;
import com.rise.ctms.dto.response.ApiResponse;
import com.rise.ctms.dto.response.UserResponseDto;
import com.rise.ctms.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
	private final UserService userService;

	

	@PostMapping("/create")
	public ResponseEntity<ApiResponse<UserResponseDto>> createUser(@Valid @RequestBody CreateUserDto dto) {
		UserResponseDto responseDto = userService.createUser(dto);
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(ApiResponse.success(responseDto, "User created successfully"));
	}
	
	@GetMapping("/getAllUsers")
	public ResponseEntity<ApiResponse<List<UserResponseDto>>> getAllUsers() {
		return ResponseEntity.status(HttpStatus.OK)
				.body(ApiResponse.success(userService.getAllUsers(), "Get all users successfully"));
	}
	
	@GetMapping("/getById/{id}")
	public ResponseEntity<ApiResponse<UserResponseDto>> getById(@PathVariable("id") Long id) {
		return ResponseEntity.status(HttpStatus.OK)
				.body(ApiResponse.success(userService.getUserById(id), "Get user by id sucessfully"));
	}
	
	@DeleteMapping("/deleteUser/{id}")
	public ResponseEntity<ApiResponse<String>> deleteUser(@PathVariable Long id) {

	    userService.deleteUser(id);

	    return ResponseEntity.status(HttpStatus.OK)
	            .body(ApiResponse.success(null, "User deleted successfully"));
	}
	
	@PutMapping("/updateUser/{id}")
	public ResponseEntity<ApiResponse<UserResponseDto>> updateUser(@PathVariable Long id, @Valid @RequestBody UpdateUserDto dto) {
	    UserResponseDto responseDto = userService.updateUser(id, dto);
	    return ResponseEntity.status(HttpStatus.OK)
	            .body(ApiResponse.success(responseDto, "User updated successfully"));
	}
	
	@GetMapping("/getAllManagers")
	public ResponseEntity<ApiResponse<Object>> getAllManagers() {
		return ResponseEntity.status(HttpStatus.OK)
				.body(ApiResponse.success(userService.getAllManagers(), "Get all managers successfully"));
	}
	
}