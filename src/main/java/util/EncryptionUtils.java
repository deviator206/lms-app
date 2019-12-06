package util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.ShortBufferException;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EncryptionUtils {
	private final static Logger logger = LoggerFactory.getLogger(EncryptionUtils.class);
	public static final int GCM_TAG_LENGTH = 16;
	public static final int GCM_IV_LENGTH = 12;

	public static SecretKey generateKey() {
		try {
			KeyGenerator kgen = KeyGenerator.getInstance("AES");
			kgen.init(128);
			return kgen.generateKey();
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalStateException(e.toString());
		}
	}

	public static SecretKeySpec loadSecKey(String keyStorePath) {
		byte[] keyb;
		SecretKeySpec skey = null;
		try {
			keyb = Files.readAllBytes(Paths.get(keyStorePath));
			skey = new SecretKeySpec(keyb, "AES");
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
		return skey;
	}

	public static void generateSecKey(String keyStorePath) {
		try (FileOutputStream out = new FileOutputStream(keyStorePath)) {
			SecretKey skey = generateKey();
			byte[] keyb = skey.getEncoded();
			out.write(keyb);
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
	}

	public static byte[] encrypt_gcm(SecretKey skey, String plaintext) {
		/*
		 * Precond: skey is valid and GCM mode is available in the JRE; otherwise
		 * IllegalStateException will be thrown.
		 */
		try {
			byte[] ciphertext = null;
			Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
			byte[] initVector = new byte[GCM_IV_LENGTH];
			(new SecureRandom()).nextBytes(initVector);
			GCMParameterSpec spec = new GCMParameterSpec(GCM_TAG_LENGTH * java.lang.Byte.SIZE, initVector);
			cipher.init(Cipher.ENCRYPT_MODE, skey, spec);
			byte[] encoded = plaintext.getBytes(java.nio.charset.StandardCharsets.UTF_8);
			ciphertext = new byte[initVector.length + cipher.getOutputSize(encoded.length)];
			for (int i = 0; i < initVector.length; i++) {
				ciphertext[i] = initVector[i];
			}
			// Perform encryption
			cipher.doFinal(encoded, 0, encoded.length, ciphertext, initVector.length);
			return ciphertext;
		} catch (NoSuchPaddingException | InvalidAlgorithmParameterException | ShortBufferException
				| BadPaddingException | IllegalBlockSizeException | InvalidKeyException | NoSuchAlgorithmException e) {
			/* None of these exceptions should be possible if precond is met. */
			throw new IllegalStateException(e.toString());
		}
	}

	public static String decrypt_gcm(SecretKey skey,
			byte[] ciphertext) /* these indicate corrupt or malicious ciphertext */
	/*
	 * Note that AEADBadTagException may be thrown in GCM mode; this is a subclass
	 * of BadPaddingException
	 */
	{
		/*
		 * Precond: skey is valid and GCM mode is available in the JRE; otherwise
		 * IllegalStateException will be thrown.
		 */
		try {
			Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
			byte[] initVector = Arrays.copyOfRange(ciphertext, 0, GCM_IV_LENGTH);
			GCMParameterSpec spec = new GCMParameterSpec(GCM_TAG_LENGTH * java.lang.Byte.SIZE, initVector);
			cipher.init(Cipher.DECRYPT_MODE, skey, spec);
			byte[] plaintext = cipher.doFinal(ciphertext, GCM_IV_LENGTH, ciphertext.length - GCM_IV_LENGTH);
			return new String(plaintext);
		} catch (NoSuchPaddingException | InvalidAlgorithmParameterException | InvalidKeyException | BadPaddingException
				| IllegalBlockSizeException | NoSuchAlgorithmException e) {
			/* None of these exceptions should be possible if precond is met. */
			throw new IllegalStateException(e.toString());
		}
	}
}
