package com.example.filedemo.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.data.jpa.repository.JpaRepository;

@Entity
public class Folder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;
    private String localPath;
    private long parentOrganizationId;
    
    public long getParentOrganizationId() {
		return parentOrganizationId;
	}
	public void setParentOrganizationId(long parentOrganizationId) {
		this.parentOrganizationId = parentOrganizationId;
	}
	public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getLocalPath() {
        return localPath;
    }
    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }
    @Override
    public String toString() {
        return "Folder [id=" + id + ", name=" + name + ", localPath=" + localPath + ", getId()=" + getId()
                + ", getName()=" + getName() + ", getLocalPath()=" + getLocalPath() + ", getClass()=" + getClass()
                + ", hashCode()=" + hashCode() + ", toString()=" + super.toString() + "]";
    }
    public Folder(long id, String name, String localPath, long parentOrganizationId) {
        super();
        this.id = id;
        this.name = name;
        this.localPath = localPath;
        this.parentOrganizationId = parentOrganizationId;
    }
    public Folder() {
        super();
        // TODO Auto-generated constructor stub
    }

}
