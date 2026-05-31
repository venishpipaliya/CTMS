package com.rise.ctms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.rise.ctms.entity.TravelRequest;

/**
 * 
 */
@Repository
public interface TravelRequestRepository extends JpaRepository<TravelRequest, Long>{

	List<TravelRequest> findByEmployee_Id(Long employeeId);
	
	
	@Query(
			value = """
					select tr.* 
					from travel_requests tr
					join users u 
					on tr.employee_id = u.id
					join users m
					on tr.manager_approver_id = m.id
					where tr.status IN ('SUBMITTED', 'MANAGER_APPROVED', 'MANAGER_REJECTED', 'FINANCE_APPROVED' ,'FINANCE_REJECTED', 'COMPLETED')
					And u.manager_id = :managerId """,
			
	nativeQuery = true)
	
	List<TravelRequest> findSubmittedRequestsByManagerId(
			@Param("managerId") Long managerId);
	
	@Query(
			value = """
					select * 
					from travel_requests 
					where status IN ('MANAGER_APPROVED', 'FINANCE_APPROVED', 'FINANCE_REJECTED', 'COMPLETED')
					""",
			nativeQuery=true )
	
	List<TravelRequest> getFinanceRequests( );
	
}
