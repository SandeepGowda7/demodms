package com.example.filedemo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.filedemo.entity.FileMetadata;

@Repository
public interface Filemetadatarepo extends JpaRepository<FileMetadata, Long> {

//	Optional<FileMetadata> findFirstByFileNameOrderByVersionDesc(String originalFilename);
	// Additional methods, if required
}
