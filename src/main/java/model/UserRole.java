package model;

public enum UserRole {
	// This will call enum constructor with one
	// String argument
	ADMIN("ADMIN"), SALES_REP("SALES REPRESENTATIVE");

	// declaring private variable for getting values
	private String displayName;

	// getter method
	public String getDisplayName() {
		return this.displayName;
	}
	
	public String getCode() {
		return this.name();
	}

	// enum constructor - cannot be public or protected
	private UserRole(String displayName) {
		this.displayName = displayName;
	}
}
