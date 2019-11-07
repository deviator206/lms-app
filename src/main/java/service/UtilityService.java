package service;

import java.util.Base64;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("prototype")
public class UtilityService implements IUtilityService {
	@Override
	public String encode(String decodedStr) {
		String encodedString = Base64.getEncoder().encodeToString(decodedStr.getBytes());
		return encodedString;

	}

	@Override
	public String decode(String encodedStr) {
		byte[] decodedBytes = Base64.getDecoder().decode(encodedStr);
		String decodedString = new String(decodedBytes);
		return decodedString;

	}
}
