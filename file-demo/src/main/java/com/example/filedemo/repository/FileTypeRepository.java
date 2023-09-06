package com.example.filedemo.repository;


import org.springframework.data.repository.CrudRepository;

import com.example.filedemo.entity.FileType;

public interface FileTypeRepository extends CrudRepository<FileType, Long> {

	FileType findByExtension(String string);

	FileType findByContentType(String string);
	
	
}


