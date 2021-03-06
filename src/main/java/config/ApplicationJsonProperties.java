package config;

import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource(value = "classpath:applicationConfig.json", factory = JsonPropertySourceFactory.class)
@ConfigurationProperties
public class ApplicationJsonProperties {

	private Map<String, Map<String, String>> policies;
	private Map<String, Map<String, String>> policyActions;

	public Map<String, Map<String, String>> getPolicyActions() {
		return policyActions;
	}

	public void setPolicyActions(Map<String, Map<String, String>> policyActions) {
		this.policyActions = policyActions;
	}

	public Map<String, Map<String, String>> getPolicies() {
		return this.policies;
	}

	public void setPolicies(Map<String, Map<String, String>> policies) {
		this.policies = policies;
	}

	private Map<String, String> controllersAccessPolicies;

	public Map<String, String> getControllersAccessPolicies() {
		return controllersAccessPolicies;
	}

	public void setControllersAccessPolicies(Map<String, String> controllersAccessPolicies) {
		this.controllersAccessPolicies = controllersAccessPolicies;
	}
}
