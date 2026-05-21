package com.rise.ctms.service;

import java.util.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rise.ctms.dto.request.CreateUserDto;
import com.rise.ctms.dto.request.UpdateUserDto;
import com.rise.ctms.dto.response.UserResponseDto;
import com.rise.ctms.entity.Role;
import com.rise.ctms.entity.User;
import com.rise.ctms.exception.ResourceConflictException;
import com.rise.ctms.repository.UserRepository;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserService{
	private final UserRepository userRepository ;
	
	@Transactional
	public UserResponseDto createUser(CreateUserDto dto) {
		if (userRepository.existsByEmail(dto.getEmail())) {
			throw new ResourceConflictException("Email already exist: " + dto.getEmail());
		}
		
		User manager = null;
		if (dto.getManagerId() != null) {
			manager = userRepository.findById(dto.getManagerId())
					.orElseThrow(() -> new ResourceConflictException("Manager not found with id: " + dto.getManagerId()));
		}

		User user = User.builder()
				.firstName(dto.getFirstName())
				.lastName(dto.getLastName())
				.email(dto.getEmail())
				.passwordHash(dto.getPassword())
				.role(dto.getRole())
				.department(dto.getDepartment())
				.manager(manager)
				.enabled(true)
				.build();
		
		User savedUser = userRepository.save(user);
		
		log.info("Created user id={} email={} role={}",
				savedUser.getId(), savedUser.getEmail(), savedUser.getRole());
		
		return UserResponseDto.from(savedUser);
		
	}
	
	private String hashPassword(String plainPassword) {
		return "hashed_" + plainPassword;
	}
		
	public List<UserResponseDto> getAllUsers() {
		List<User> users = userRepository.findAll();
		return users.stream()
				.map(UserResponseDto::from)
				.toList();
	}
	
	public UserResponseDto getUserById(Long id) {
		User user = userRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("User Not Found"));
		
		return UserResponseDto.from(user);		
				
	}

	public void deleteUser(Long id) {
		
		 if (!userRepository.existsById(id)) {
		        throw new RuntimeException("User not found");
		    }

	    userRepository.deleteById(id);
	}

	public UserResponseDto updateUser(Long id, @Valid UpdateUserDto dto) {
		User user = userRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("User Not Found"));
		
		if (!user.getEmail().equals(dto.getEmail()) && userRepository.existsByEmail(dto.getEmail())) {
			throw new ResourceConflictException("Email already exist: " + dto.getEmail());
		}
		
		User manager = null;
		if (dto.getManagerId() != null) {
			manager = userRepository.findById(dto.getManagerId())
					.orElseThrow(() -> new ResourceConflictException("Manager not found with id: " + dto.getManagerId()));
		}
		
		user.setFirstName(dto.getFirstName());
		user.setLastName(dto.getLastName());
		user.setEmail(dto.getEmail());
		user.setRole(dto.getRole());
		user.setDepartment(dto.getDepartment());
		user.setManager(manager);
		user.setEnabled(dto.isEnabled());
		
		User updatedUser = userRepository.save(user);
		
		log.info("Updated user id={} email={} role={}",
				updatedUser.getId(), updatedUser.getEmail(), updatedUser.getRole());
		
		return UserResponseDto.from(updatedUser);
	}

	public Object getAllManagers() {
		List<User> managers = userRepository.findByRole(Role.MANAGER);
		return managers.stream()
				.map(UserResponseDto::from)
				.toList();
	}
	
	
	
	
		
	

}

