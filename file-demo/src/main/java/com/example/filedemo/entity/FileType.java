package com.example.filedemo.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "type_table")
public class FileType {
    @Id
    private Long id;

    private String extension;
    
    private String contentType;

	public FileType(Long id, String extension, String contentType) {
		super();
		this.id = id;
		this.extension = extension;
		this.contentType = contentType;
	}

	public FileType() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getExtension() {
		return extension;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	@Override
	public String toString() {
		return "FileType [id=" + id + ", extension=" + extension + ", contentType=" + contentType + ", getId()=" + getId() + ", getExtension()="
				+ getExtension() + ", getContentType()=" + getContentType() + ", getClass()=" + getClass() + ", hashCode()=" + hashCode() + ", toString()="
				+ super.toString() + "]";
	}

    // Constructors, getters, and setters
}
