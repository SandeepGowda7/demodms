package com.example.filedemo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.filedemo.entity.Folder;

@Repository
public interface FolderRepository extends JpaRepository<Folder, Long> {
	@Query("SELECT u FROM Folder u WHERE u.parentOrganizationId = ?1")
	public List<Folder> getFolders(long parentOrganizationId);
	
	@Query("SELECT u.name FROM Folder u WHERE u.id = ?1")
	public String getFolderName(long parentOrganizationId);
	
	@Query("SELECT u.parentOrganizationId FROM Folder u WHERE u.name = ?1")
	public Long getFolderParent(String name);
		
}
