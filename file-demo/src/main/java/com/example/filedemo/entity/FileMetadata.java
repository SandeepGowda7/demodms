package com.example.filedemo.entity;

import javax.persistence.*;

@Entity
@Table(name = "metadata_table")
public class FileMetadata {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column
	private String fileName;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "file_upload_table_id")
	private FileUpload fileUpload;

	public FileMetadata(Long id, String fileName, FileUpload fileUpload) {
		super();
		this.id = id;
		this.fileName = fileName;

		this.fileUpload = fileUpload;
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

	public FileUpload getFileUpload() {
		return fileUpload;
	}

	public void setFileUpload(FileUpload fileUpload) {
		this.fileUpload = fileUpload;
	}

	public FileMetadata() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "FileMetadata [id=" + id + ", fileName=" + fileName + ", fileUpload=" + fileUpload + ", getId()="
				+ getId() + ", getFileName()=" + getFileName();
	}

	// Other metadata properties, getters, and setters
}
