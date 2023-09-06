package com.example.filedemo.controller;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.filedemo.entity.Organization;
import com.example.filedemo.repository.OrganizationRepository;
import com.example.filedemo.util.ResponseSettingUtil;

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class OrganizationController {

	@Autowired
	OrganizationRepository orgRepo;

//	@Autowired
//	UsersOrgsRepository userOrgRepo;
//
//	@Autowired
//	UserOrgUtil userOrgUtil;

	@Autowired
	ResponseSettingUtil responseUtil;

	private final Logger _log = LoggerFactory.getLogger(this.getClass());

//	@GetMapping("/customer/list")
//	public String getAllCustomers() {
//
//		// fetching userId of current user logged in
//
//		_log.info("userId " + userOrgUtil.getCurrentLogInUserId());
//		List<UsersOrgs> organizations = userOrgRepo.getAllCustomers(userOrgUtil.getCurrentLogInUserId());
//		List<Long> orgIdList = new ArrayList<Long>();
//		for (UsersOrgs id : organizations) {
//			orgIdList.add(id.getOrganizationId());
//		}
//
//		// get all customers user has access
//		List<Organization> customers = orgRepo.getAllCustomers(orgIdList);
//
//		// get all projects user has access
//		List<Organization> projects = orgRepo.getAllProjects(orgIdList);
//
//		List<Long> customerIds = new ArrayList<Long>();
//		for (Organization customer : customers) {
//			customerIds.add(customer.getOrganizationId());
//		}
//
//		List<Organization> projectCustomers = new ArrayList<Organization>();
//		for (Organization project : projects) {
//
//			// Adding the customer organization of the project user has access
//			if (customerIds.contains(project.getParentOrganizationId()) == false) {
//				projectCustomers.add(orgRepo.getCustomer(project.getParentOrganizationId()));
//			}
//		}
//
//		// all the customer organizations user has access to both customer and project
//		// level
//		customers.addAll(projectCustomers);
//		JSONObject customersList = new JSONObject();
//		customersList.put("customers", customers);
//		return responseUtil.responseSetter(customersList, true, "", 1).toString();
//	}
//
//	@GetMapping("/customer/{customerId}/project/list")
//	public String getAllCustomerProjects(@PathVariable long customerId) {
//
//		List<Organization> projects = new ArrayList<Organization>();
//		// checking if user has customer level access
//		if (userOrgUtil.validateUser(customerId, userOrgUtil.getCurrentLogInUserId())) {
//			// fetch all projects of the customer
//			projects = orgRepo.getAllCustomerProjects(customerId);
//		}
//
//		else {
//			List<UsersOrgs> organizations = userOrgRepo.getAllCustomers(userOrgUtil.getCurrentLogInUserId());
//			List<Long> orgIdList = new ArrayList<Long>();
//			for (UsersOrgs id : organizations) {
//				orgIdList.add(id.getOrganizationId());
//			}
//			// fetch only projects that user has access
//			projects = orgRepo.getAllCustomerProjectsUserBased(customerId, orgIdList);
//		}
//		JSONObject projectsList = new JSONObject();
//		projectsList.put("projects", projects);
//		return responseUtil.responseSetter(projectsList, true, "", 1).toString();
//	}
	
	@GetMapping("/customer/list")
	public String getAllCustomers() {
		
		List<Organization> customers =  orgRepo.getAllCustomersList();
		JSONObject customersList = new JSONObject();
		customersList.put("customers", customers);
		return responseUtil.responseSetter(customersList, true, "", 1).toString();
	}
	
	@GetMapping("/customer/{customerId}/project/list")
	public String getAllCustomerProjects(@PathVariable long customerId) {

		List<Organization> projects = orgRepo.getAllCustomerProjects(customerId);
		JSONObject projectsList = new JSONObject();
		projectsList.put("projects", projects);
		return responseUtil.responseSetter(projectsList, true, "", 1).toString();
	}
	
	@GetMapping("/project/list")
	public String getAllProjects() {
		
		List<Organization> projects =  orgRepo.getAllProjectsList();
		JSONObject projectsList = new JSONObject();
		projectsList.put("projects", projects);
		return responseUtil.responseSetter(projectsList, true, "", 1).toString();
	}
	
	public String getCustomerName(long parentOrganizationId) {
		String name = orgRepo.getCustomerName(parentOrganizationId);
		return name;
	}
	
	public String getProjectName(long parentOrganizationId) {
		String name = orgRepo.getProjectName(parentOrganizationId);
		return name;
	}
}

