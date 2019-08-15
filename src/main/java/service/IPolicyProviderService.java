package service;

import java.util.List;
import java.util.Map;

public interface IPolicyProviderService {
	Map<String, String> getPolicies(List<String> roles);

}
