package controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
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
import security.ApplicationPropertiesProvider;
import security.JwtTokenProvider;
import security.UserPrincipal;
import service.IApplicationPropertiesProviderService;

@RestController
public class AuthController {

	@Autowired
	AuthenticationManager authenticationManager;

	// @Autowired
	// UserRepository userRepository;

	// @Autowired
	// RoleRepository roleRepository;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	JwtTokenProvider tokenProvider;

	@Autowired
	private IApplicationPropertiesProviderService applicationPropertiesProviderService;

	@PostMapping("/login")
	public JwtAuthenticationResponse authenticateUser(@RequestBody LoginRequest loginRequest) {

		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUserName(), loginRequest.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);
		List<String> roles = authentication.getAuthorities().stream()
				.map(grantedAuthority -> grantedAuthority.getAuthority()).collect(Collectors.toList());
		Map<String, String> policies = applicationPropertiesProviderService.getPolicies(roles);

		UserInfo userInfo = new UserInfo();
		userInfo.setPolicies(policies);
		userInfo.setUserId(((UserPrincipal) authentication.getPrincipal()).getUserId());
		userInfo.setUserName(((UserPrincipal) authentication.getPrincipal()).getUsername());
		userInfo.setBusinessUnit(((UserPrincipal) authentication.getPrincipal()).getBusinessUnit());
		userInfo.setUserDisplayName(((UserPrincipal) authentication.getPrincipal()).getUserDisplayName());
		userInfo.setRoles(roles);
		userInfo.setEnabled(((UserPrincipal) authentication.getPrincipal()).isEnabled());

		String jwt = tokenProvider.generateToken(authentication);
		return new JwtAuthenticationResponse(jwt, userInfo);
	}

	/*
	 * @PostMapping("/signup") public ResponseEntity<?>
	 * registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
	 * if(userRepository.existsByUsername(signUpRequest.getUsername())) { return new
	 * ResponseEntity(new ApiResponse(false, "Username is already taken!"),
	 * HttpStatus.BAD_REQUEST); }
	 * 
	 * if(userRepository.existsByEmail(signUpRequest.getEmail())) { return new
	 * ResponseEntity(new ApiResponse(false, "Email Address already in use!"),
	 * HttpStatus.BAD_REQUEST); }
	 * 
	 * // Creating user's account User user = new User(signUpRequest.getName(),
	 * signUpRequest.getUsername(), signUpRequest.getEmail(),
	 * signUpRequest.getPassword());
	 * 
	 * user.setPassword(passwordEncoder.encode(user.getPassword()));
	 * 
	 * Role userRole = roleRepository.findByName(RoleName.ROLE_USER) .orElseThrow(()
	 * -> new AppException("User Role not set."));
	 * 
	 * user.setRoles(Collections.singleton(userRole));
	 * 
	 * User result = userRepository.save(user);
	 * 
	 * URI location = ServletUriComponentsBuilder
	 * .fromCurrentContextPath().path("/api/users/{username}")
	 * .buildAndExpand(result.getUsername()).toUri();
	 * 
	 * return ResponseEntity.created(location).body(new ApiResponse(true,
	 * "User registered successfully")); }
	 */
}