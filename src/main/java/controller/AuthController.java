package controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import model.JwtAuthenticationResponse;
import model.LoginRequest;
import model.UserInfo;
import security.JwtTokenProvider;
import security.UserPrincipal;
import service.IApplicationPropertiesProviderService;

@RestController
@Scope("prototype")
public class AuthController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	JwtTokenProvider tokenProvider;

	@Autowired
	private IApplicationPropertiesProviderService applicationPropertiesProviderService;

	@PostMapping("/login")
	public JwtAuthenticationResponse authenticateUser(@RequestBody LoginRequest loginRequest) {
		logger.info("Login - Started");
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUserName(), loginRequest.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);
		List<String> roles = authentication.getAuthorities().stream()
				.map(grantedAuthority -> grantedAuthority.getAuthority()).collect(Collectors.toList());
		Map<String, String> policies = applicationPropertiesProviderService.getPolicies(roles);

		Map<String, String> policyActions = applicationPropertiesProviderService.getPolicyActions(roles);

		UserInfo userInfo = new UserInfo();

		userInfo.setUserId(((UserPrincipal) authentication.getPrincipal()).getUserId());
		userInfo.setUserName(((UserPrincipal) authentication.getPrincipal()).getUsername());
		userInfo.setBusinessUnit(((UserPrincipal) authentication.getPrincipal()).getBusinessUnit());
		userInfo.setUserDisplayName(((UserPrincipal) authentication.getPrincipal()).getUserDisplayName());
		userInfo.setRoles(roles);
		userInfo.setEnabled(((UserPrincipal) authentication.getPrincipal()).isEnabled());
		userInfo.setTmpUser(((UserPrincipal) authentication.getPrincipal()).isTempUser());

		if (policyActions != null) {
			userInfo.setPolicyActions(policyActions);
		}
		if (policies != null) {
			userInfo.setPolicies(policies);
		}

		String jwt = tokenProvider.generateToken(authentication);
		return new JwtAuthenticationResponse(jwt, userInfo);
	}
}