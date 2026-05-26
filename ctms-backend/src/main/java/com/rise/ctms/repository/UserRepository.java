package com.rise.ctms.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rise.ctms.entity.Role;
import com.rise.ctms.entity.User;

import java.util.List;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByEmail(String email);
	
	List<User> findByRole(Role role);
	boolean existsByEmail(String email);
	
	List<User> findAll();
	
	Optional<User> findById(Long id);

	Optional<User> findByDepartmentAndRole(String string, Role manager);

	

	
	
	
	

	
	
}
