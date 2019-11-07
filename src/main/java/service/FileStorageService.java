package service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import config.FileStorageProperties;
import model.DownloadFileRes;
import model.UploadFileRes;

@Service
public class FileStorageService implements IFileStorageService {

	@Autowired
	FileStorageProperties fileStorageProperties;

	/*
	 * @Override public void uploadFiles(List<MultipartFile> files) throws
	 * IOException { for (MultipartFile file : files) {
	 * 
	 * if (file.isEmpty()) { continue; // next pls }
	 * 
	 * byte[] bytes = file.getBytes(); // Path path = Paths.get(UPLOADED_FOLDER +
	 * file.getOriginalFilename()); Path path =
	 * Paths.get(fileStorageProperties.getUploadDir() + file.getOriginalFilename());
	 * Files.write(path, bytes);
	 * 
	 * }
	 * 
	 * }
	 */

	@Override
	public UploadFileRes uploadLeadAttachment(Long id, List<MultipartFile> files) throws IOException {
		UploadFileRes uploadFileRes = new UploadFileRes();
		if (!files.isEmpty() && (files.get(0) != null)) {
			uploadFileRes = this.uploadAttachment(id, fileStorageProperties.getLeadUploadDir(), files.get(0));
		}
		return uploadFileRes;
	}

	@Override
	public UploadFileRes uploadLeadContactAttachment(Long id, List<MultipartFile> files) throws IOException {
		UploadFileRes uploadFileRes = new UploadFileRes();
		if (!files.isEmpty() && (files.get(0) != null)) {
			uploadFileRes = this.uploadAttachment(id,fileStorageProperties.getLeadContactDir(), files.get(0));
		}
		return uploadFileRes;
	}
	
	@Override
	public UploadFileRes uploadMiAttachment(Long id, List<MultipartFile> files) throws IOException {
		UploadFileRes uploadFileRes = new UploadFileRes();
		if (!files.isEmpty() && (files.get(0) != null)) {
			uploadFileRes = this.uploadAttachment(id, fileStorageProperties.getMarketIntlUploadDir(), files.get(0));
		}
		return uploadFileRes;
	}

	public UploadFileRes uploadAttachment(Long id, String uploadDirPath, MultipartFile file) throws IOException {
		UploadFileRes uploadFileRes = new UploadFileRes();
		String folderPath = uploadDirPath + String.valueOf(id);
		File tempFile = new File(folderPath);
		if (!tempFile.exists()) {
			if (tempFile.mkdir()) {
				System.out.println("Directory is created!");
			} else {
				System.out.println("Failed to create directory!");
			}
		} else {
			File[] allContents = tempFile.listFiles();
			if (allContents != null) {
				for (File innerFile : allContents) {
					innerFile.delete();
				}
			}
		}
		String strPath = folderPath + "/" + file.getOriginalFilename();
		Path path = Paths.get(strPath);
		byte[] bytes = file.getBytes();
		Files.write(path, bytes);
		uploadFileRes.setAttachmentPath(path.toUri().getPath());
		uploadFileRes.setFileName(file.getOriginalFilename());
		return uploadFileRes;
	}

	@Override
	public DownloadFileRes downLoadFile(String downloadDirPath, String name) throws IOException {
		String filePath = downloadDirPath + name;
		File file = new File(filePath);
		Path path = Paths.get(file.getAbsolutePath());
		DownloadFileRes downloadFileRes = new DownloadFileRes();
		downloadFileRes.setContentLength(file.length());
		downloadFileRes.setFileResource(new ByteArrayResource(Files.readAllBytes(path)));
		return downloadFileRes;
	}

	@Override
	public DownloadFileRes downloadLeadAttachment(Long leadId, String name) throws IOException {
		return this.downLoadFile(fileStorageProperties.getLeadUploadDir() + String.valueOf(leadId) + "/", name);
	}

	@Override
	public DownloadFileRes downloadMiAttachment(Long miId, String name) throws IOException {
		return this.downLoadFile(fileStorageProperties.getMarketIntlUploadDir() + String.valueOf(miId) + "/", name);
	}
	
	@Override
	public DownloadFileRes downloadContactAttachment(Long leadId, String name) throws IOException {
		return this.downLoadFile(fileStorageProperties.getLeadContactDir() + String.valueOf(leadId) + "/", name);
	}
}
