package config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "file")
public class FileStorageProperties {
	@Value("${file.lead-upload-dir}")
	private String leadUploadDir;
	public String getLeadUploadDir() {
		return leadUploadDir;
	}
	public void setLeadUploadDir(String leadUploadDir) {
		this.leadUploadDir = leadUploadDir;
	}
	public String getMarketIntlUploadDir() {
		return marketIntlUploadDir;
	}
	public void setMarketIntlUploadDir(String marketIntlUploadDir) {
		this.marketIntlUploadDir = marketIntlUploadDir;
	}
	@Value("${file.mi-upload-dir}")
	private String marketIntlUploadDir;
}