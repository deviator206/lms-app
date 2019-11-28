package service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import model.DownloadFileRes;
import model.UploadFileRes;

public interface IFileStorageService {
	// void uploadFiles(List<MultipartFile> files) throws IOException;

	public UploadFileRes uploadLeadAttachment(Long id, List<MultipartFile> files) throws IOException;

	public UploadFileRes uploadLeadContactAttachment(Long id, List<MultipartFile> files) throws IOException;

	public UploadFileRes uploadMiAttachment(Long id, List<MultipartFile> files) throws IOException;

	public DownloadFileRes downLoadFile(String downloadDirPath, String name) throws IOException;

	public DownloadFileRes downloadLeadAttachment(Long leadId, String name) throws IOException;

	public DownloadFileRes downloadMiAttachment(Long miId, String name) throws IOException;
	public DownloadFileRes downloadContactAttachment(Long leadId, String name) throws IOException;
}
