package security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import model.User;
import service.IUserService;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	public IUserService userService;

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		// Let people login with either username or email
		User user = userService.getUserByUserName(userName);
		// .orElseThrow(
		// () -> new UsernameNotFoundException("User not found with username or email :
		// " + usernameOrEmail));

		return UserPrincipal.create(user);
	}

	// This method is used by JWTAuthenticationFilter
	@Transactional
	public UserDetails loadUserById(Long id) {
		User user = userService.getUserByUserId(id);
		// .orElseThrow(() -> new UsernameNotFoundException("User not found with id : "
		// + id));

		return UserPrincipal.create(user);
	}
}