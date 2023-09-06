package com.example.filedemo.controller;
import java.util.List;

import org.json.JSONObject;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.filedemo.entity.Folder;
import com.example.filedemo.repository.FolderRepository;
import com.example.filedemo.util.ResponseSettingUtil;

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class FolderController {
	
	@Autowired
	FolderRepository folderRepo;
	
	@Autowired
	ResponseSettingUtil responseUtil;
	
	@GetMapping("/folder/{parentOrganizationId}/list")
	public String getFolders(@PathVariable long parentOrganizationId) {
		
		List<Folder> folders =  folderRepo.getFolders(parentOrganizationId);
		JSONObject foldersList = new JSONObject();
		foldersList.put("folders", folders);
		return responseUtil.responseSetter(foldersList, true, "", 1).toString();
	}
	
	@GetMapping("/folder/list")
	public String getAllFolders() {
		List<Folder> folders = folderRepo.findAll();
		JSONObject foldersList = new JSONObject();
		foldersList.put("folders", folders);
		return responseUtil.responseSetter(foldersList, true, "", 1).toString();
	}
	
	public String getFolderName(long parentOrganizationId) {
		String name = folderRepo.getFolderName(parentOrganizationId);
		return name;
	}

}
