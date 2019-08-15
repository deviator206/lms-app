package config;

import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource(
		  value = "classpath:policies.json", 
		  factory = JsonPropertySourceFactory.class)
@ConfigurationProperties
public class PolicyProperties {
	private Map<String,Map<String,String>> policies;

	public Map<String, Map<String, String>> getPolicies() {
		return policies;
	}

	public void setPolicies(Map<String, Map<String, String>> policies) {
		this.policies = policies;
	}
}
