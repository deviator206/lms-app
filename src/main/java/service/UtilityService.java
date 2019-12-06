package service;

import java.util.Base64;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import util.EncryptionUtils;

@Service
@Scope("prototype")
public class UtilityService implements IUtilityService {

	@Value("${security.encrypt-decrypt.kestore-dir}")
	private String keyStoreDir;

	@Value("${security.encrypt-decrypt.kestore-file}")
	private String keyStoreFile;

	@Override
	public String encrypt(String decryptedStr) {
		byte[] encryptrdArr = EncryptionUtils.encrypt_gcm(EncryptionUtils.loadSecKey(keyStoreDir + keyStoreFile),
				decryptedStr);
		String encryptedStr = Base64.getEncoder().encodeToString(encryptrdArr);
		return encryptedStr;
	}

	@Override
	public String decrypt(String encryptedStr) {
		byte[] encryptrdArr = Base64.getDecoder().decode(encryptedStr);
		String decrptedStr = EncryptionUtils.decrypt_gcm(EncryptionUtils.loadSecKey(keyStoreDir + keyStoreFile),
				encryptrdArr);
		return decrptedStr;
	}

	@Override
	public void generateAndStoreFile() {
		EncryptionUtils.generateSecKey(keyStoreDir + keyStoreFile);

	}

}
