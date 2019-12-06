package util;

import java.security.SecureRandom;

// Java code to explain how to generate random 
// password 

// Here we are using random() method of util 
// class in Java 
import java.util.*;

public class PasswordGenerationUtil {
	private static final int PASSWORD_LENGTH = 8;

	public static void main(String[] args) {
		// Length of your password as I have choose
		// here to be 8
		System.out.println(getPassword());
	}

	// This our Password generating method
	// We have use static here, so that we not to
	// make any object for it
	public static String getPassword() {
		System.out.println("Generating password using random() : ");
		System.out.print("Your new password is : ");

		// A strong password has Cap_chars, Lower_chars,
		// numeric value and symbols. So we are using all of
		// them to generate our password
		String capitalChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		String smallChars = "abcdefghijklmnopqrstuvwxyz";
		String numbers = "0123456789";
		String symbols = "!@#$%^&*_=+-/.?<>)";

		String values = capitalChars + smallChars + numbers + symbols;

		// Using random method
		Random rndm_method = new SecureRandom();

		char[] password = new char[PASSWORD_LENGTH];

		for (int i = 0; i < PASSWORD_LENGTH; i++) {
			// Use of charAt() method : to get character value
			// Use of nextInt() as it is scanning the value as int
			password[i] = values.charAt(rndm_method.nextInt(values.length()));

		}
		return String.valueOf(password);
	}
}
