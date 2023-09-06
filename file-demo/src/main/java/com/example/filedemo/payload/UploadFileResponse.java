package com.example.filedemo.payload;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;


public class UploadFileResponse {
    private String fileName;
    private String downloadUri;
    private ResponseEntity<FileSystemResource> responseEntity;
    private File downloadedFile;

    // Constructor and getter/setter methods omitted for brevity

    public UploadFileResponse() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getDownloadUri() {
		return downloadUri;
	}

	public void setDownloadUri(String downloadUri) {
		this.downloadUri = downloadUri;
	}

	public ResponseEntity<FileSystemResource> getResponseEntity() {
		return responseEntity;
	}

	public void setResponseEntity(ResponseEntity<FileSystemResource> responseEntity) {
		this.responseEntity = responseEntity;
	}

	public File getDownloadedFile() {
		return downloadedFile;
	}

	public void setDownloadedFile(File downloadedFile) {
		this.downloadedFile = downloadedFile;
	}

	public UploadFileResponse(String fileName, String downloadUri, ResponseEntity<FileSystemResource> responseEntity,
			File downloadedFile) {
		super();
		this.fileName = fileName;
		this.downloadUri = downloadUri;
		this.responseEntity = responseEntity;
		this.downloadedFile = downloadedFile;
	}

	// Add a method to trigger file download
    public void downloadFile(HttpServletResponse response) throws IOException {
        response.setContentType(responseEntity.getHeaders().getContentType().toString());
        response.setHeader("Content-Disposition", responseEntity.getHeaders().getFirst("Content-Disposition"));

        try (InputStream inputStream = new FileInputStream(downloadedFile)) {
            int readBytes;
            byte[] buffer = new byte[4096];
            while ((readBytes = inputStream.read(buffer)) != -1) {
                response.getOutputStream().write(buffer, 0, readBytes);
            }
        }
        response.flushBuffer();
    }
}