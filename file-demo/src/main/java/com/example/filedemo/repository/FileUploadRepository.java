package com.example.filedemo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import com.example.filedemo.entity.FileMetadata;
import com.example.filedemo.entity.FileUpload;

@Repository
public interface FileUploadRepository extends JpaRepository<FileUpload, Long> {

	List<FileUpload> findFileUploadByFileName(String fileName);

	Optional<FileUpload> findByFileNameAndIsCurrentVersion(MultipartFile fileName, boolean isCurrentVersion);

	Optional<FileUpload> findByFileNameAndVersion(String filename, int version);
	
	Optional<FileUpload> findByFileNameAndIsCurrentVersion(String fileName, boolean isCurrentVersion);
	
	@Query("SELECT u from FileUpload u WHERE u.isCurrentVersion = True ORDER BY u.id DESC")
	public List<FileUpload> getRecentFiles();
	
	@Query("SELECT u from FileUpload u WHERE u.isCurrentVersion = True")
	public List<FileUpload> getAllFiles();
	
	@Query("SELECT u from FileUpload u WHERE u.parentOrganizationId=?1 AND u.isCurrentVersion = True")
	public List<FileUpload> getFilesBasedOnId(Long id);
	
	@Query("SELECT u.version from FileUpload u WHERE u.fileName=?1 AND u.isCurrentVersion = False AND u.parentOrganizationId=?2")
	public List<Long> getFileVersions(String name, Long Id);

	List<FileUpload> findFileUploadByFileNameAndParentOrganizationId(String originalFileName,
			Long parentOrganizationID);

	Optional<FileUpload> findByFileNameAndIsCurrentVersionAndParentOrganizationId(String fileName, boolean b,
			Long pID);
	
	Optional<FileUpload> findByFileNameAndVersionAndParentOrganizationId(String fileName, int version,
			Long pID);

	Optional<FileUpload> findByIdAndIsCurrentVersion(Long fileId, boolean isCurrentVersion);

}
