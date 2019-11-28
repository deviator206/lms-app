package security;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import service.IPolicyProviderService;

@Component
public class PolicyProvider {
	@Autowired
	private IPolicyProviderService policyProviderService;

	Map<String, String> getPolicies(List<String> role) {
		return policyProviderService.getPolicies(role);
	}
}
