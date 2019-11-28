package security;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import service.IApplicationPropertiesProviderService;

@Component
public class ApplicationPropertiesProvider {
	@Autowired
	private IApplicationPropertiesProviderService policyProviderService;

	public Map<String, String> getPolicies(List<String> role) {
		return policyProviderService.getPolicies(role);
	}

	public Map<String, String> getControllersAccessPolicies() {
		return policyProviderService.getControllersAccessPolicies();
	}
}
