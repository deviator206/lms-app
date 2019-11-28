package service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import config.PolicyProperties;

@Service
public class PolicyProviderService implements IPolicyProviderService {

	@Autowired
	private PolicyProperties PolicyProperties;

	@Override
	public Map<String, String> getPolicies(List<String> roles) {
		Map<String, Map<String, String>> policies = PolicyProperties.getPolicies();
		Map<String, String> policyMap = new HashMap<String, String>();
		Map<String, String> policyAttributes = new HashMap<String, String>();
		for (String role : roles) {
			policyMap = policies.get(role);
			for (Map.Entry<String, String> entry : policyMap.entrySet()) {
				policyAttributes.put(entry.getKey(), entry.getValue());
			}

		}
		return policyAttributes;
	}

}
