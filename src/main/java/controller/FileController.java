package controller;

import java.io.IOException;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import config.FileStorageProperties;
import model.DownloadFileRes;
import service.IFileStorageService;

@RestController
public class FileController {

	@Autowired
	FileStorageProperties fileStorageProperties;

	@Autowired
	IFileStorageService fileStorageService;

	// Single file upload
	@PostMapping("/file/upload")
	public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile uploadfile) {

		if (uploadfile.isEmpty()) {
			return new ResponseEntity("You must select a file!", HttpStatus.OK);
		}

		try {

			fileStorageService.uploadLeadAttachment(new Long(1111), Arrays.asList(uploadfile));

		} catch (IOException e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity("Successfully uploaded - " + uploadfile.getOriginalFilename(), new HttpHeaders(),
				HttpStatus.OK);

	}

	@RequestMapping(path = "/file/download", method = RequestMethod.GET)
	public ResponseEntity<Resource> download(@RequestParam(value = "name", required = false) String name)
			throws IOException {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
		headers.add("Pragma", "no-cache");
		headers.add("Expires", "0");
		DownloadFileRes downloadFileRes = fileStorageService.downloadLeadAttachment(new Long(1111), name);
		return ResponseEntity.ok().headers(headers).contentLength(downloadFileRes.getContentLength())
				.contentType(MediaType.parseMediaType("application/octet-stream"))
				.body(downloadFileRes.getFileResource());
	}
}
