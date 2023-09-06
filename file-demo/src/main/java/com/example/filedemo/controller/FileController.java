package com.example.filedemo.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import javax.activation.FileTypeMap;
import javax.activation.MimetypesFileTypeMap;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;

//import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.core.io.ByteArrayResource;
//import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
//import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
//import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.filedemo.entity.FileMetadata;
import com.example.filedemo.entity.FileUpload;
import com.example.filedemo.entity.Folder;
//import com.example.filedemo.exception.FileStorageException;
import com.example.filedemo.repository.FileUploadRepository;
import com.example.filedemo.repository.Filemetadatarepo;
import com.example.filedemo.repository.FolderRepository;
import com.example.filedemo.repository.OrganizationRepository;
import com.example.filedemo.service.FileService;
import com.example.filedemo.service.FileStorageService;
import com.example.filedemo.util.ResponseSettingUtil;

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class FileController {

	private static final Logger logger = LoggerFactory.getLogger(FileController.class);

	@Autowired
	private FileStorageService fileStorageService;

	@Autowired
	private FileUploadRepository fileUploadRepository;

	private ServletContext servletContext;

	@Autowired
	public FileController(FileUploadRepository fileUploadRepository, ServletContext servletContext) {
		this.fileUploadRepository = fileUploadRepository;
		this.servletContext = servletContext;

	}

	@Autowired
	OrganizationRepository orgRepo;

	@Autowired
	FolderRepository folderRepo;

	@Autowired
	private Filemetadatarepo fileMetadataRepository;

	@Autowired
	private FileService fileService;

	@Autowired
	private FolderRepository folderRepository;
	private HttpServletResponse response;

	@Autowired
	ResponseSettingUtil responseUtil;

	@PostMapping("/uploadFile")
	public ResponseEntity<String> uploadFile(@RequestParam("fileName") MultipartFile fileName,
			@RequestParam("pID") String pID) throws IOException {
		System.out.println(" post method ");

		boolean isUpdatingFile = false;
		FileUpload existingFile = null;
		String originalFileName = fileName.getOriginalFilename();
		String parentOrgId = pID;
		Long parentOrganizationID = Long.parseLong(parentOrgId);
		System.out.print(" oofilename " + originalFileName);

		// Determine the file type
		String fileType = getFileType(fileName.getContentType(), originalFileName);

		// Check if the file already exists in the database
//		List<FileUpload> existingFiles = fileUploadRepository.findFileUploadByFileName(originalFileName);
		List<FileUpload> existingFiles = fileUploadRepository
				.findFileUploadByFileNameAndParentOrganizationId(originalFileName, parentOrganizationID);
		if (!existingFiles.isEmpty()) {
			isUpdatingFile = true;
			existingFile = existingFiles.get(0);
			System.out.println(" exisfile " + existingFile);
		}

		if (isUpdatingFile) {
			System.out.println(" clcikok ");
			// File is being updated
//			FileUpload existingFile1 = fileUploadRepository.findByFileNameAndIsCurrentVersion(originalFileName, true)
//					.get();
			existingFile.setCurrentVersion(false);
			fileUploadRepository.save(existingFile);

			int newVersion = existingFile.getVersion() + 1;

			FileUpload newFileUpload = new FileUpload();
			newFileUpload.setVersion(newVersion);
			newFileUpload.setFileName(originalFileName);
			newFileUpload.setFileType(fileType);
			newFileUpload.setCurrentVersion(true);
			newFileUpload.setParentOrganizationId(parentOrganizationID);

			FileUpload savedFileUpload = fileUploadRepository.save(newFileUpload);

			// Save the file the generated ID as the filename
			fileStorageService.saveFileToLocalStorage(fileName, savedFileUpload.getId());

			byte[] fileBytes = fileName.getBytes();

			String fileContentBase64 = Base64.getEncoder().encodeToString(fileBytes);

			System.out.println(" file content " + fileContentBase64.toString());

			System.out.println(" new file Id " + savedFileUpload.getId());
			System.out.println("New Version: " + savedFileUpload.getVersion());

			String response = "New File ID: " + savedFileUpload.getId() + ", New Version: "
					+ savedFileUpload.getVersion();

			return ResponseEntity.ok("File uploaded with the same name ");
		} else {

			// Save the file to the database and get the generated ID
			FileUpload fileDetails = fileStorageService.storeFile(fileName, isUpdatingFile, parentOrganizationID);
			System.out.println(" file Details " + fileDetails.toString());
			System.out.println(" file Id " + fileDetails.getId());

			// Save the file the generated ID as the filename
			fileStorageService.saveFileToLocalStorage(fileName, fileDetails.getId());

			byte[] fileBytes = fileName.getBytes();

			String fileContentBase64 = Base64.getEncoder().encodeToString(fileBytes);

			System.out.println(" file content " + fileContentBase64.toString());

			// Save file metadata to the database
			FileMetadata fileMetadata = new FileMetadata();
			fileMetadata.setFileName(originalFileName);
			fileMetadata.setFileUpload(fileDetails);
			fileMetadataRepository.save(fileMetadata);

			String downloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/downloadFile/")
					.path(fileDetails.getId().toString()).toUriString();

			return ResponseEntity.ok("File uploaded successfully with ID: " + fileDetails.getId());
		}
	}

	@PostMapping("/checkFilesExistence")
	public ResponseEntity<String> checkFileExistences(@RequestParam("fileName") MultipartFile fileName,
			@RequestParam("pID") String pID) {
		String originalFileName = fileName.getOriginalFilename();

		// Check if the file already exists
//		List<FileUpload> existingFiles = fileUploadRepository.findFileUploadByFileName(originalFileName);
		String parentOrgId = pID;
		Long parentOrganizationID = Long.parseLong(parentOrgId);
		List<FileUpload> existingFiles = fileUploadRepository
				.findFileUploadByFileNameAndParentOrganizationId(originalFileName, parentOrganizationID);
		if (!existingFiles.isEmpty()) {
			// File with the same name already exists
			return ResponseEntity.ok("File Exists!");
		} else {
			// File does not exist
			return ResponseEntity.ok("File does not exist.");
		}
	}

	@PostMapping("/create-folders")
	public ResponseEntity<Folder> createFolder(@RequestBody Folder folder) {
		if (folder == null || folder.getName() == null || folder.getName().isEmpty()) {
			return ResponseEntity.badRequest().build();
		}

		Folder savedFolder = folderRepository.save(folder);

//        // Perform any necessary local operations to create the folder locally
//        String localFolderPath = createLocalFolder(savedFolder);
//
//        // Store the local path in the "localPath" field of the savedFolder
//        savedFolder.setLocalPath(localFolderPath);
		folderRepository.save(savedFolder);

		return ResponseEntity.status(HttpStatus.CREATED).body(savedFolder);
	}

//    private String createLocalFolder(Folder folder) {
//       
//        String baseDirectory = "D:\\exDMS\\newFolders\\";
//
//        // Generate a unique folder name or use the folder name from the entity
//        String folderName = folder.getName();
//        String localFolderPath = baseDirectory + folderName;
//
//        // Perform the actual folder creation
//        File folderFile = new File(localFolderPath);
//        if (!folderFile.exists()) {
//            folderFile.mkdirs();
//        }
//
//        return localFolderPath;
//    
//
//}

	// 1st

//	@PostMapping("/uploadFile")
//	public ResponseEntity<String> uploadFile(@RequestParam("fileName") MultipartFile fileName) throws IOException {
//		System.out.println("post method");
//
//		// Save the file to the database and get the generated ID
//		FileUpload filedetails = fileStorageService.storeFile(fileName);
//		System.out.println("file Details " + filedetails.toString());
//		System.out.println("called");
//		System.out.println("file Id " + filedetails.getId());
//
//		// Save the file to the local storage using the generated ID as the filename
//		fileStorageService.saveFileToLocalStorage(fileName, filedetails.getId());
//
//		// Save file metadata to the database
//		String originalFileName = fileName.getOriginalFilename();
//		FileMetadata fileMetadata = new FileMetadata();
//		fileMetadata.setFileName(originalFileName);
//		fileMetadata.setFileUpload(filedetails);
//		System.out.println(fileMetadata.toString());
//		fileMetadataRepository.save(fileMetadata);
//
//
//		// Generate download URI
//	    String downloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
//	            .path("/downloadFile/")
//	            .path(filedetails.getId().toString())
//	            .toUriString();
//	    
//		return ResponseEntity.ok("File uploaded successfully with ID: " + filedetails.getId());
//
////	    return ResponseEntity.ok("File uploaded successfully with ID: " + filedetails.getId() + "\nDownload URL: " + downloadUri);
//	}

	@PostMapping("/files/revert")
	public ResponseEntity<String> revertFileToPreviousVersion(@RequestParam("fileName") String filename,
			@RequestParam("version") int version, @RequestParam("pID") Long pID) {

		System.out.println("IN REvery");
		FileService.revertToPreviousVersion(filename, version, pID);

		return ResponseEntity.ok(" File is reverted successfully  ");

	}

	@GetMapping("/downloadFile/{Id}")
	public ResponseEntity<FileSystemResource> downloadFile(@PathVariable Long Id, HttpServletResponse response) {
		boolean isCurrentVersion = true;

		ResponseEntity<FileSystemResource> downloadResponse = fileStorageService.Filedownload(Id, isCurrentVersion);
		if (downloadResponse.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
			return ResponseEntity.notFound().build();
		}

		String downloadUri = downloadResponse.getHeaders().getFirst("Download-URI");

		return ResponseEntity.ok().header("Download-URI", downloadUri).body(downloadResponse.getBody());
	}

	private String getFileType(String contentType, String fileName) {
		String fileType;
		if (contentType != null && !contentType.isEmpty()) {
			fileType = contentType;
		} else {
			FileTypeMap fileTypeMap = new MimetypesFileTypeMap();
			fileType = fileTypeMap.getContentType(fileName);
		}
		return fileType;
	}

//	@GetMapping("/file-versions/{filename}")
//	public List<FileUpload> getFileVersions(@PathVariable String filename) {
//	    List<FileUpload> fileVersions = fileUploadRepository.findFileUploadByFileName(filename);
//		return fileVersions;
//
//	    // Debug statement
////	    for (FileUpload fileVersion : fileVersions) {
////	        System.out.println("Version: " + fileVersion.getVersion());
////	    }
//		
//		// converting data into JSON
//
////	    try {
////	        ObjectMapper objectMapper = new ObjectMapper();
////	        String jsonData = objectMapper.writeValueAsString(fileVersions);
////	        System.out.println("fileVersions: " + fileVersions);
////	        System.out.println("Details: " + jsonData);
////	        return jsonData;
////	    } catch (JsonProcessingException e) {
////	        e.printStackTrace();
////	        // Handle the exception appropriately
////	        return "Error converting file versions to JSON.";
////	    }
//	}

	@GetMapping("/files/{Id}/list")
	public String getFilesBasedOnId(@PathVariable Long Id) {
		List<FileUpload> files = fileUploadRepository.getFilesBasedOnId(Id);
		JSONObject filesList = new JSONObject();
		filesList.put("files", files);
		return responseUtil.responseSetter(filesList, true, "", 1).toString();
	}

	@GetMapping("/files/list")
	public String getAllFiles() {
		List<FileUpload> files = fileUploadRepository.getAllFiles();
		JSONObject filesList = new JSONObject();
		filesList.put("files", files);
		return responseUtil.responseSetter(filesList, true, "", 1).toString();
	}

	public String getCustomerName(long parentOrganizationId) {
		String name = orgRepo.getCustomerName(parentOrganizationId);
		return name;
	}

	public String getProjectName(long parentOrganizationId) {
		String name = orgRepo.getProjectName(parentOrganizationId);
		return name;
	}

	public Long getProjectParent(String name) {
		Long id = orgRepo.getProjectParent(name);
		return id;
	}

	public Long getFolderParent(String name) {
		Long id = folderRepo.getFolderParent(name);
		return id;
	}

	public String getFolderName(long parentOrganizationId) {
		String name = folderRepo.getFolderName(parentOrganizationId);
		return name;
	}

	@GetMapping("/files/recent")
	public String getRecentFiles() {
		List<FileUpload> files = fileUploadRepository.getRecentFiles();
		String path = "";
		List<String> pathArray = new ArrayList<String>();
		for (int i = 0; i < files.size(); i++) {
			String customerName = "";
			customerName = getCustomerName(files.get(i).getParentOrganizationId());
			if (customerName != null) {
				path = customerName + " > " + files.get(i).getFileName();
			} else {
				String projectName = getProjectName(files.get(i).getParentOrganizationId());
				if (projectName != null) {
					Long projectId = getProjectParent(projectName);
					customerName = getCustomerName(projectId);
					path = customerName + " > " + projectName + " > " + files.get(i).getFileName();
				} else {
					String folderName = getFolderName(files.get(i).getParentOrganizationId());
					if (folderName != null) {
						Long folderId = getFolderParent(folderName);
						String folderParentName = getFolderName(folderId);
						if (folderParentName != null) {
							Long parentFolderId = getFolderParent(folderParentName);
							customerName = getCustomerName(parentFolderId);
							if (customerName != null) {
								path = customerName + " > " + folderParentName + " > " + folderName + " > "
										+ files.get(i).getFileName();
							} else {
								projectName = getProjectName(parentFolderId);
								if (projectName != null) {
									Long projectId = getProjectParent(projectName);
									customerName = getCustomerName(projectId);
									path = customerName + " > " + projectName + " > " + folderParentName + " > "
											+ folderName + " > " + files.get(i).getFileName();
								}
							}
						} else {
							customerName = getCustomerName(folderId);
							if (customerName != null) {
								path = customerName + " > " + folderName + " > " + files.get(i).getFileName();
							} else {
								projectName = getProjectName(folderId);
								if (projectName != null) {
									Long projectId = getProjectParent(projectName);
									customerName = getCustomerName(projectId);
									path = customerName + " > " + projectName + " > " + folderName + " > "
											+ files.get(i).getFileName();
								}
							}
						}
//						path = customerName+"=>"+projectName+"=>"+folderName+"=>"+files.get(i).getFileName();
					}
				}
			}
			pathArray.add(path);
		}
		JSONObject filesList = new JSONObject();
		filesList.put("files", files);
		filesList.put("paths", pathArray);
		return responseUtil.responseSetter(filesList, true, "", 1).toString();
	}

	@GetMapping("/files/{name}/{Id}/versions")
	public String getFileVersions(@PathVariable String name, @PathVariable String Id) {
		String parentOrgId = Id;
		Long parentOrganizationID = Long.parseLong(parentOrgId);
		List<Long> versions = fileUploadRepository.getFileVersions(name, parentOrganizationID);
		JSONObject filesList = new JSONObject();
		filesList.put("versions", versions);
		return responseUtil.responseSetter(filesList, true, "", 1).toString();
	}

//	@GetMapping("/files/{fileName}/preview")
//	public ResponseEntity<File> FilePreview(@PathVariable String fileName, HttpServletResponse response) {
//		boolean isCurrentVersion = true;
//		File previewResponse = fileStorageService.FilePreview(fileName, isCurrentVersion);
//		System.out.println("File preview details:2 " + previewResponse);
//
//		return ResponseEntity.ok().body(previewResponse);
//	}

	@GetMapping("/files/{fileId}/preview")
	public ResponseEntity<byte[]> filePreview(@PathVariable Long fileId, HttpServletResponse response) {
		boolean isCurrentVersion = true;

		ResponseEntity<byte[]> previewResponse = fileStorageService.FilePreview(fileId, isCurrentVersion);

		if (previewResponse.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
			System.out.println("if condition");
			return ResponseEntity.notFound().build();
		}

		return previewResponse;
	}

}
