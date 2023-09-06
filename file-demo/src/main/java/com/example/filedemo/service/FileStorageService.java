package com.example.filedemo.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;

import javax.activation.FileTypeMap;
import javax.activation.MimetypesFileTypeMap;

import org.apache.commons.io.FileExistsException;
import org.apache.commons.io.FileUtils;
import org.hibernate.mapping.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.filedemo.entity.FileUpload;
import com.example.filedemo.exception.FileStorageException;
import com.example.filedemo.model.FileStorageProperties;
import com.example.filedemo.payload.UploadFileResponse;
import com.example.filedemo.repository.FileUploadRepository;

@Service
@Transactional
public class FileStorageService {

	private final Path fileStorageLocation;

	@Autowired
	private FileUploadRepository fileUploadRepository;

	@Autowired
	public FileStorageService(FileStorageProperties fileStorageProperties) {
		this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir()).toAbsolutePath().normalize();

		try {
			Files.createDirectories(this.fileStorageLocation);
		} catch (Exception ex) {
			throw new FileStorageException("Could not create the directory where the uploaded files will be stored.",
					ex);
		}
	}

	/// storing file after uploading

	public FileUpload storeFile(MultipartFile file, boolean isUpdatingFile, Long pID) throws IOException {
	    // Normalize file name
	    String fileName = StringUtils.cleanPath(file.getOriginalFilename());

	    //filename contains invalid characters
	    if (fileName.contains("..")) {
	        throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
	    }

	    // Determine the file type
	    String fileType = getFileType(file.getContentType(), fileName);

	    if (isUpdatingFile) {
	        // File is being updated
	        FileUpload existingFile = fileUploadRepository.findByFileNameAndIsCurrentVersion(fileName, true).get();
	        existingFile.setCurrentVersion(false);
	        fileUploadRepository.save(existingFile);

	        int newVersion = existingFile.getVersion() + 1;

	        FileUpload newFileUpload = new FileUpload();
	        newFileUpload.setVersion(newVersion);
	        newFileUpload.setFileName(fileName);
	        newFileUpload.setFileType(fileType);
	        newFileUpload.setCurrentVersion(true);
	        newFileUpload.setParentOrganizationId(pID);

	        return fileUploadRepository.save(newFileUpload);
	    } else {
	        // File is a new upload
	        int newVersion = 1;

	        FileUpload newFileUpload = new FileUpload();
	        newFileUpload.setVersion(newVersion);
	        newFileUpload.setFileName(fileName);
	        newFileUpload.setFileType(fileType);
	        newFileUpload.setCurrentVersion(true);
	        newFileUpload.setParentOrganizationId(pID);

	        return fileUploadRepository.save(newFileUpload);
	    }
	}

	
//	public FileUpload storeFile(MultipartFile file) throws IOException {
//		// Normalize file name
//		String fileName = StringUtils.cleanPath(file.getOriginalFilename());
//
//		System.out.println(" file name is " + fileName);
//
//		// Check if the file's name contains invalid characters
//		if (fileName.contains("..")) {
//			throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
//		}
//		// Check if the file already exists in the database or file system
//		String fileExists = fileExists(fileName);
//		System.out.println(" existing file is " + fileExists);
//		
//		// Determine the file type
//		String fileType = getFileType(file.getContentType(), fileName);
//
//		System.out.println("file " + fileExists);
//
//		// File already exists, update its details
//		java.util.List<FileUpload> existingFileUpload = fileUploadRepository.findFileUploadByFileName(fileName);
//
//		int oldVersion = existingFileUpload.size();
//
//		System.out.println(" old version is " + oldVersion);
//		int newVersion = existingFileUpload.size() + 1;
//		if (oldVersion == 0) {
//
//			System.out.println(" new version " + newVersion);
//			FileUpload newFileUpload = new FileUpload();
//			newFileUpload.setVersion(newVersion);
//			newFileUpload.setFileName(fileName);
//			newFileUpload.setFileType(fileType);
//			newFileUpload.setCurrentVersion(true);
//			// Save the updated existingFileUpload object back to the repository
//			return fileUploadRepository.save(newFileUpload);
//		} else {
//			System.out.println("else condition");
//
//			FileUpload currentFile = fileUploadRepository.findByFileNameAndIsCurrentVersion(fileName, true).get();
//			System.out.println(" current(old) file " + currentFile.toString());
//			currentFile.setCurrentVersion(false);
//			System.out.println(" updated current(old) file " + currentFile.toString());
//
//			// Save the updated existingFileUpload object back to the repository
//			FileUpload test = fileUploadRepository.save(currentFile);
//
//			System.out.println("new version " + newVersion);
//			FileUpload newFileUpload = new FileUpload();
//			newFileUpload.setVersion(newVersion);
//			newFileUpload.setFileName(fileName);
//			newFileUpload.setFileType(fileType);
//			newFileUpload.setCurrentVersion(true);
//
//			FileUpload uploadedFile = fileUploadRepository.save(newFileUpload);
//			System.out.println("new uploaded file " + uploadedFile.toString());
//
//			return uploadedFile;
//
//		}
//
//	}

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

	public void saveFileToLocalStorage(MultipartFile file, long fileId) throws IOException {
        byte[] fileData = file.getBytes();

        String originalFileName = file.getOriginalFilename();
        System.out.println("Original file name: " + originalFileName);

        // Extract the file extension from the original file name
        String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
        System.out.println("File extension: " + fileExtension);

        // Define the directory where you want to save files
        String directoryPath = "/exDMS/uploaded file";

        // Create the directory if it doesn't exist
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        
        String newFileName = fileId + fileExtension;
        System.out.println("New file name: " + newFileName);

        
        Path filePath = Paths.get(directoryPath, newFileName);

        try {
            
            Files.write(filePath, fileData);
            System.out.println("File saved successfully to: " + filePath.toString());
        } catch (IOException e) {
            
            System.err.println("Error saving the file: " + e.getMessage());// You might want to log the exception or take appropriate action here.
        }
    }

	//// File download

	public ResponseEntity<FileSystemResource> Filedownload(Long Id, boolean isCurrentVersion) {
//		FileUpload fileDownload = fileUploadRepository.findByFileNameAndIsCurrentVersion(fileName, true).orElse(null);
		FileUpload fileDownload = fileUploadRepository.findByIdAndIsCurrentVersion(Id, isCurrentVersion).orElse(null);
		System.out.println("File details: " + fileDownload);

		if (fileDownload == null) {
			return ResponseEntity.notFound().build();
		}

		File file = getFileFromLocalStorage(fileDownload.getId(),fileDownload.getFileType());
		System.out.println(file);

		if (file == null) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}

		String downloadDirectory = "D:\\exDMS\\downloaded\\"; // Specify the local directory where the file should be
																// saved

		String downloadFilePath = downloadDirectory + fileDownload.getFileName();
		File downloadFile = new File(downloadFilePath);
		System.out.println("Download File: " + downloadFile);

		try {
			FileUtils.copyFile(file, downloadFile);
		} catch (IOException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}

		FileSystemResource resource = new FileSystemResource(downloadFile);
		System.out.println("Resource: " + resource);

		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"");

		// Construct the download URI
		String downloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/downloadFile/").path(resource.getFilename())
				.toUriString();

		// Add the download URI to the response headers
		headers.add("Download-URI", downloadUri);

		System.out.println("Download URI: " + downloadUri);

		return ResponseEntity.ok().headers(headers).contentLength(resource.getFile().length())
				.contentType(MediaType.APPLICATION_OCTET_STREAM).body(resource);
	}

	public File getFileFromLocalStorage(long fileId, String fileType) {
		String fileExtension = "";
		switch(fileType) {
		case "text/plain": fileExtension = ".txt"; break;
		case "application/pdf": fileExtension = ".pdf"; break;
		case "application/vnd.openxmlformats-officedocument.wordprocessingml.document": fileExtension = ".docx"; break;
		case "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet": fileExtension = ".xlsx"; break;
		case "application/vnd.openxmlformats-officedocument.presentationml.presentation": fileExtension = ".pptx"; break;
		case "video/mp4": fileExtension = ".mp4"; break;
		case "image/png": fileExtension = ".png"; break;
		case "text/csv": fileExtension =".csv"; break;
		case "application/x-zip-compressed": fileExtension = ".zip"; break;
		}
		String filePath = "/exDMS/uploaded file/" + fileId + fileExtension; // Replace with your actual file path
		File file = new File(filePath);
		System.out.println("FileId: " + fileId);
		System.out.println("File: " + file);
		if (file.exists()) {
			return file;
		}
		return null;
	}

	public String fileExists(String fileName) {
		// TODO Auto-generated method stub
		return fileName;
	}
	
	public ResponseEntity<byte[]> FilePreview(Long fileId, boolean isCurrentVersion) {
        Optional<FileUpload> file = fileUploadRepository.findByIdAndIsCurrentVersion(fileId, true);

        System.out.print(" uploaded file " + file);

        if (!file.isPresent()) {
            System.out.println("if condition service");
            return ResponseEntity.notFound().build();
        }

        byte[] fileContent = readFileContent(file);
        System.out.println("file content " + fileContent);
        HttpHeaders headers = new HttpHeaders();
        System.out.println("ext"+getFileExtension(file.get().getFileName()));
        if((getFileExtension(file.get().getFileName())).equalsIgnoreCase("png")) {
        	System.out.println("in png");
        	headers.setContentType(MediaType.IMAGE_PNG);
        }else if((getFileExtension(file.get().getFileName())).equalsIgnoreCase("jpeg")) {
        	headers.setContentType(MediaType.IMAGE_JPEG);
        } else if((getFileExtension(file.get().getFileName())).equalsIgnoreCase("pdf")) {
        	headers.setContentType(MediaType.APPLICATION_PDF);
        } else if((getFileExtension(file.get().getFileName())).equalsIgnoreCase("txt")) {
        	headers.setContentType(MediaType.TEXT_PLAIN);
        } else if((getFileExtension(file.get().getFileName())).equalsIgnoreCase("xlsx")) {
        	headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
        } else {
        	headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        }
        headers.setContentDispositionFormData("attachment", file.get().getFileName());
        return new ResponseEntity<>(fileContent, headers, HttpStatus.OK);
    }

    private byte[] readFileContent(Optional<FileUpload> file) {
            if (file.isPresent()) {
                try {
                    
                    String fileNameWithExtension = file.get().getId().toString() + "." + getFileExtension(file.get().getFileName());
                    Path filePath = Paths.get("D:\\exDMS\\uploaded file\\", fileNameWithExtension);   
                    if (Files.exists(filePath)) {
                      
                        return Files.readAllBytes(filePath);
                    } else {
                        System.out.println("File not found: " + filePath.toString());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            
            return new byte[0]; 
        }


        private String getFileExtension(String fileName) {
            int lastDotIndex = fileName.lastIndexOf(".");
            if (lastDotIndex != -1) {
                return fileName.substring(lastDotIndex + 1);
            }
            return ""; 
        }
	
//	public File FilePreview(String fileName, boolean isCurrentVersion) {
//		FileUpload fileDownload = fileUploadRepository.findByFileNameAndIsCurrentVersion(fileName, true).orElse(null);
//		File file = getFileFromLocalStorage(fileDownload.getId(),fileDownload.getFileType());
//		System.out.println(file);
//		return file;
//    }
	

	//// zip

//	public UploadFileResponse Filedownload(String fileName, boolean isCurrentVersion) {
//        FileUpload fileDownload = fileUploadRepository.findByFileNameAndIsCurrentVersion(fileName, true).orElse(null);
//        System.out.println("File details: " + fileDownload);
//
//        if (fileDownload == null) {
//            return new UploadFileResponse(fileName, null, ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
//        }
//
//        File file = getFileFromLocalStorage(fileDownload.getId());
//        if (file == null) {
//            return new UploadFileResponse(fileName, null,
//                    ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null));
//        }
//
//        String zipFileName = fileDownload.getFileName() + ".zip";
//        System.out.println("zipFileName: " + zipFileName);
//
//        String downloadDirectory = "D:\\exDMS\\downloaded\\"; // Specify the local directory where the file should be saved
//
//        String fileNameWithoutExtension = fileDownload.getFileName().substring(0, fileDownload.getFileName().lastIndexOf("."));
//        String desiredZipFileName = fileNameWithoutExtension + ".zip";
//
//        File zipFile = new File(downloadDirectory + desiredZipFileName);
//        System.out.println("zipFile: " + zipFile);
//
//        try {
//            FileOutputStream fos = new FileOutputStream(zipFile);
//            ZipOutputStream zos = new ZipOutputStream(fos);
//
//            // Add the selected file to the zip
//            addToZip(file, zos);
//
//            // Close the zip stream
//            zos.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//            return new UploadFileResponse(fileName, null,
//                    ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null));
//        }
//
//        FileSystemResource resource = new FileSystemResource(zipFile);
//        System.out.println("Resource: " + resource);
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"");
//
//        String downloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/download/").path(fileName)
//                .toUriString();
//        headers.add("Download-URI", downloadUri);
//
//        System.out.println("Download URI: " + downloadUri);
//
//        ResponseEntity<FileSystemResource> responseEntity = ResponseEntity.ok().headers(headers)
//                .contentLength(resource.getFile().length()).contentType(MediaType.APPLICATION_OCTET_STREAM)
//                .body(resource);
//
//        return new UploadFileResponse(fileName, downloadUri, responseEntity);
//    }
//
//	private void addToZip(File file, ZipOutputStream zipOutputStream) throws IOException {
//	    FileInputStream fis = new FileInputStream(file);
//	    ZipEntry zipEntry = new ZipEntry(file.getName());
//	    zipOutputStream.putNextEntry(zipEntry);
//
//	    byte[] buffer = new byte[1024];
//	    int length;
//	    while ((length = fis.read(buffer)) > 0) {
//	        zipOutputStream.write(buffer, 0, length);
//	    }
//
//	    zipOutputStream.closeEntry();
//	    fis.close();
//	}
//
//
//    public File getFileFromLocalStorage(long fileId) {
//        String filePath = "/exDMS/uploaded file/" + fileId; // Replace with your actual file path
//        File file = new File(filePath);
//        System.out.println("FileId: " + fileId);
//        System.out.println("File: " + file);
//        if (file.exists()) {
//            return file;
//        }
//        return null;
//    
//}

	/////
	

}