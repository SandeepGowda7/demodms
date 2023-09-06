package com.example.filedemo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.example.filedemo.entity.FileType;
import com.example.filedemo.model.FileStorageProperties;
import com.example.filedemo.repository.FileTypeRepository;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
@EnableConfigurationProperties({ FileStorageProperties.class })
public class FileDemoApplication implements CommandLineRunner {
	
	 @Autowired
	    private FileTypeRepository fileTypeRepository;

	 
	 public static void main(String[] args) {
			SpringApplication.run(FileDemoApplication.class, args);		
		}

	 @Override
	 public void run(String... args) {
	     FileType existingFileType = fileTypeRepository.findByContentType("text/plain");

	     if (existingFileType == null) {
	         
	         FileType txtFileType = new FileType();
	         txtFileType.setId(1L); // Set static ID value
	         txtFileType.setExtension(".txt");
	         txtFileType.setContentType("text/plain");
	         fileTypeRepository.save(txtFileType);
	     }

	    
	     createFileTypeIfNotExists(2L, ".pdf", "application/pdf");

	     createFileTypeIfNotExists(3L, ".docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document");
//	     createFileTypeIfNotExists(4L, ".xls", "application/vnd.ms-excel");
	     createFileTypeIfNotExists(4L, ".xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
//	     createFileTypeIfNotExists(5L, ".ppt", "application/vnd.ms-powerpoint");
	     createFileTypeIfNotExists(5L, ".pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation");
	     createFileTypeIfNotExists(6L, ".mp4", "video/mp4");
	     createFileTypeIfNotExists(7L, ".png", "image/png");

	     // Retrieve all file types
	     Iterable<FileType> fileTypes = fileTypeRepository.findAll();
	     for (FileType fileType : fileTypes) {
	         System.out.println("Extension: " + fileType.getExtension());
	     }
	 }

	 private void createFileTypeIfNotExists(Long id, String extension, String contentType) {
	     FileType existingFileType = fileTypeRepository.findById(id).orElse(null);
	     if (existingFileType == null) {
	         FileType fileType = new FileType();
	         fileType.setId(id);
	         fileType.setExtension(extension);
	         fileType.setContentType(contentType);
	         fileTypeRepository.save(fileType);
	     }
	 }

	
}




   

