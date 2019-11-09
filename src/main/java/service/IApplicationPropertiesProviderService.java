package service;

import java.util.List;
import java.util.Map;

public interface IApplicationPropertiesProviderService {
	Map<String, String> getPolicies(List<String> roles);

	Map<String, String> getControllersAccessPolicies();
	
	public Map<String, String> getPolicyActions(List<String> roles);

}
