package controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import model.EncryptDecrypt;
import service.IUtilityService;

@RestController
@Scope("prototype")
public class UtilityServicesController {
	@Autowired
	public IUtilityService utilityService;

	// @PreAuthorize("hasAnyAuthority('ADMIN')")
	@PostMapping("/encode")
	public EncryptDecrypt encode(@RequestBody EncryptDecrypt encodeDecode) {
		EncryptDecrypt encryptDecrypt = new EncryptDecrypt();
		encryptDecrypt.setEncrypted(utilityService.encrypt(encodeDecode.getDecrypted()));
		return encryptDecrypt;
	}

	// @PreAuthorize("hasAnyAuthority('ADMIN')")
	@PostMapping("/decode")
	public EncryptDecrypt decode(@RequestBody EncryptDecrypt encodeDecode) {
		EncryptDecrypt encryptDecrypt = new EncryptDecrypt();
		encryptDecrypt.setDecrypted(utilityService.decrypt(encodeDecode.getEncrypted()));
		return encryptDecrypt;
	}

	@PostMapping("/generateSecKey")
	public void generateSecKey(@RequestBody EncryptDecrypt encodeDecode) {
		utilityService.generateAndStoreFile();
	}
}
