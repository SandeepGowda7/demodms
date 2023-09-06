package com.example.filedemo.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;

@Entity
@Table(name = "file_upload_table")
public class FileUpload {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private Long parentOrganizationId;

	@Column(nullable = false)
	private String fileName;

	@Column(nullable = false)
	private String fileType;
	
//	@ManyToOne
//    @JoinColumn(name = "file_type_id", nullable = false)
//    private FileType fileType;

	@Column(name = "Version")
	private int version;

	@Column(name = "isCurrentVersion")
	private boolean isCurrentVersion;
	
	// Constructors, getters, and setters

//	public FileUpload() {
//		super();
//		// TODO Auto-generated constructor stub
//	}
//
//	public FileUpload(Long id, String fileName, FileType fileType, int version, boolean isCurrentVersion) {
//		super();
//		this.id = id;
//		this.fileName = fileName;
//		this.fileType = fileType;
//		this.version = version;
//		this.isCurrentVersion = isCurrentVersion;
//	}
//
//	public Long getId() {
//		return id;
//	}
//
//	public void setId(Long id) {
//		this.id = id;
//	}
//
//	public String getFileName() {
//		return fileName;
//	}
//
//	public void setFileName(String fileName) {
//		this.fileName = fileName;
//	}
//
//	public FileType getFileType() {
//		return fileType;
//	}
//
//	public void setFileType(FileType fileType) {
//		this.fileType = fileType;
//	}
//
//	public int getVersion() {
//		return version;
//	}
//
//	public void setVersion(int version) {
//		this.version = version;
//	}
//
//	public boolean isCurrentVersion() {
//		return isCurrentVersion;
//	}
//
//	public void setCurrentVersion(boolean isCurrentVersion) {
//		this.isCurrentVersion = isCurrentVersion;
//	}

	// Constructors, getters, and setters

	
	
	public FileUpload() {
		super();
		
	}

	public FileUpload(Long id, Long parentOrganizationId, String fileName, String fileType, int version, boolean isCurrentVersion) {
		super();
		this.id = id;
		this.parentOrganizationId = parentOrganizationId;
		this.fileName = fileName;
		this.fileType = fileType;
		this.version = version;
		this.isCurrentVersion = isCurrentVersion;
	}

	public boolean isCurrentVersion() {
		return isCurrentVersion;
	}

	public void setCurrentVersion(boolean isCurrentVersion) {
		this.isCurrentVersion = isCurrentVersion;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public void setVersion(int version) {
		this.version = version;
	}
	
	public int getVersion() {
		return version;
	}
	
	public Long getParentOrganizationId() {
		return parentOrganizationId;
	}

	public void setParentOrganizationId(Long parentOrganizationId) {
		this.parentOrganizationId = parentOrganizationId;
	}

	@Override
	public String toString() {
		return "FileUpload [id=" + id + ", fileName=" + fileName + ", fileType=" + fileType + ", version=" + version
				+ ", isCurrentVersion=" + isCurrentVersion + "]";
	}

	public void setStoragePath(String storagePath) {
		// TODO Auto-generated method stub
		
	}

}
