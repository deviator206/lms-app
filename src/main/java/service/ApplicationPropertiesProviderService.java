package service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import config.ApplicationJsonProperties;

@Service
public class ApplicationPropertiesProviderService implements IApplicationPropertiesProviderService {

	@Autowired
	private ApplicationJsonProperties applicationProperties;

	@Override
	public Map<String, String> getPolicies(List<String> roles) {
		Map<String, Map<String, String>> policies = applicationProperties.getPolicies();
		Map<String, String> policyMap = new HashMap<String, String>();
		Map<String, String> policyAttributes = new HashMap<String, String>();
		for (String role : roles) {
			policyMap = policies.get(role);
			if (policyMap != null) {
				for (Map.Entry<String, String> entry : policyMap.entrySet()) {
					policyAttributes.put(entry.getKey(), entry.getValue());
				}
			}

		}
		return policyAttributes;
	}

	@Override
	public Map<String, String> getControllersAccessPolicies() {
		return applicationProperties.getControllersAccessPolicies();
	}

}
