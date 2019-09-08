package service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import model.DownloadFileRes;
import model.UploadFileRes;

public interface IFileStorageService {
	// void uploadFiles(List<MultipartFile> files) throws IOException;

	UploadFileRes uploadLeadAttachment(Long id, List<MultipartFile> files) throws IOException;

	UploadFileRes uploadMiAttachment(Long id, List<MultipartFile> files) throws IOException;

	DownloadFileRes downLoadFile(String downloadDirPath, String name) throws IOException;

	DownloadFileRes downloadLeadAttachment(Long leadId, String name) throws IOException;
	
	DownloadFileRes downloadMiAttachment(Long miId, String name) throws IOException;
}
