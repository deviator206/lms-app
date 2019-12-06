package model;

public class EncryptDecrypt {
	private String encrypted;
	private String decrypted;
	Boolean save;

	public Boolean getSave() {
		return save;
	}

	public void setSave(Boolean save) {
		this.save = save;
	}

	public String getEncrypted() {
		return encrypted;
	}

	public void setEncrypted(String encrypted) {
		this.encrypted = encrypted;
	}

	public String getDecrypted() {
		return decrypted;
	}

	public void setDecrypted(String decrypted) {
		this.decrypted = decrypted;
	}
}
