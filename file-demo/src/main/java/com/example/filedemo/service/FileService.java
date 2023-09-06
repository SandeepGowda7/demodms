package com.example.filedemo.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.filedemo.entity.FileMetadata;
import com.example.filedemo.entity.FileUpload;
import com.example.filedemo.repository.FileUploadRepository;
import com.example.filedemo.repository.Filemetadatarepo;

@Service
@Transactional
public class FileService {
	private final Filemetadatarepo fileMetadataRepository;

	@Autowired
	private static FileUploadRepository fileUploadRepository;

	public FileService(Filemetadatarepo fileMetadataRepository, FileUploadRepository fileUploadRepository) {
		this.fileMetadataRepository = fileMetadataRepository;
		this.fileUploadRepository = fileUploadRepository;
	}

	public void storeFile(MultipartFile file) {
		// Store the file in the local file system
		String fileDirectory = "\\exDMS\\uploaded file";
		String originalFilename = file.getOriginalFilename();

//		System.out.println("origin" + originalFilename);

		// Create FileMetadata object
		FileMetadata fileMetadata = new FileMetadata();
		fileMetadata.setFileName(originalFilename);

		// Save file metadata to the database
		fileMetadataRepository.save(fileMetadata);

		// Retrieve the entity from the database
		FileMetadata storedFileMetadata = fileMetadataRepository.findById(fileMetadata.getId()).orElse(null);

		// Check if the entity exists
		if (storedFileMetadata != null) {
			// Update the entity
			storedFileMetadata.setFileName(originalFilename);

			// Persist the changes
			fileMetadataRepository.save(storedFileMetadata);

			// Print the metadata
			System.out.println("File metadata: " + storedFileMetadata);
		}
	}

	public static void revertToPreviousVersion(String fileName, int version, Long pID) {
		// Find the current version of the file
//		FileUpload currentFile = fileUploadRepository.findByFileNameAndIsCurrentVersion(fileName, true).get();
		FileUpload currentFile = fileUploadRepository.findByFileNameAndIsCurrentVersionAndParentOrganizationId(fileName, true, pID).get();
		System.out.println(" currentfile details " + currentFile);
		System.out.println(" revert to version  " + version);

		if (currentFile != null) {
			// Update the current version's iscurrentversion status to false
			currentFile.setCurrentVersion(false);
			System.out.println(" updated current file is " + currentFile );
			fileUploadRepository.save(currentFile);
//			System.out.println("The file is already the current version!!");
		}
		// Find the previous version of the file
		Optional<FileUpload> previousFile = fileUploadRepository.findByFileNameAndVersionAndParentOrganizationId(fileName, version, pID);
		System.out.println(" previousFile is " + previousFile.toString());
		if (previousFile != null) {
			// Update the current version's iscurrentversion status to false
			previousFile.get().setCurrentVersion(true);
			System.out.println(" updated previous file is " + previousFile );
			fileUploadRepository.save(previousFile.get());
			System.out.println(" File is Reverted !!");
		}
	}

}

