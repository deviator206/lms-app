package model;

import org.springframework.core.io.ByteArrayResource;

public class DownloadFileRes {
	private ByteArrayResource fileResource;
	private Long contentLength;
	public ByteArrayResource getFileResource() {
		return fileResource;
	}
	public void setFileResource(ByteArrayResource fileResource) {
		this.fileResource = fileResource;
	}
	public Long getContentLength() {
		return contentLength;
	}
	public void setContentLength(Long contentLength) {
		this.contentLength = contentLength;
	}

}
