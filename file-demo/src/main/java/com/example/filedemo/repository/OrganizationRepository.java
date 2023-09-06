package com.example.filedemo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.filedemo.entity.Organization;

@Repository
public interface OrganizationRepository extends JpaRepository<Organization, Long> {

	@Query("SELECT u FROM Organization u WHERE u.type_ = 'project' and u.parentOrganizationId = ?1")
	public List<Organization> getAllCustomerProjects(long customerId);
	
//	@Query("SELECT t FROM Organization t WHERE t.type_='customer' and t.organizationId in ?1")
//	public List<Organization> getAllCustomers(List<Long> orgIdList);
//	
//	@Query("SELECT u FROM Organization u WHERE u.type_ = 'project' and u.parentOrganizationId = ?1 and u.organizationId in ?2")
//	public List<Organization> getAllCustomerProjectsUserBased(long customerId,List<Long> orgIdList);
//	
//	@Query("SELECT t FROM Organization t WHERE t.type_='project' and t.organizationId in ?1")
//	public List<Organization> getAllProjects(List<Long> orgIdList);
//	
//	@Query("SELECT t FROM Organization t WHERE t.type_='customer' and t.organizationId = ?1")
//	public Organization getCustomer(long customerId);
	
	
	@Query("SELECT t FROM Organization t WHERE t.type_='customer'")
	public List<Organization> getAllCustomersList();
	
	@Query("SELECT t FROM Organization t where t.type_='project'")
	public List<Organization> getAllProjectsList();
	
	@Query("SELECT u.name FROM Organization u WHERE u.organizationId = ?1 AND u.type_='customer'")
	public String getCustomerName(long parentOrganizationId);
	
	@Query("SELECT u.name FROM Organization u WHERE u.organizationId = ?1 AND u.type_='project'")
	public String getProjectName(long parentOrganizationId);
	
	@Query("SELECT u.parentOrganizationId FROM Organization u WHERE u.name = ?1 AND u.type_='project'")
	public Long getProjectParent(String name);

}

