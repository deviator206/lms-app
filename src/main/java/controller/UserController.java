package controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import mapper.ModelMappers;
import model.FilterUserRes;
import model.ForgotPasswordResponse;
import model.User;
import model.UserRegistrationDetails;
import model.UserRes;
import model.UserRoles;
import service.IUserService;

@RestController
@Scope("prototype")
public class UserController {
	@Autowired
	public IUserService userService;

	// @Secured("ADMIN")
	// @PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/users")
	public List<UserRes> getUsers() {
		return userService.getUsers();
	}

	@GetMapping("/user/{userId}")
	public UserRes getUserByUserId(@PathVariable("userId") Long userId) {
		System.out.println("getUserByUserId  ------------- >> " + userId);
		User user = userService.getUserByUserId(userId);
		UserRes userRes = new UserRes();
		ModelMappers.mapUserToUserRes(user, userRes);
		return userRes;
	}	

	@GetMapping("/user")
	public UserRes getUserByUserName(@RequestParam(value = "name", required = false) String userName) {
		User user = userService.getUserByUserName(userName);
		UserRes userRes = new UserRes();
		ModelMappers.mapUserToUserRes(user, userRes);
		return userRes;
	}

	@PostMapping("/user")
	public Long addUser(@RequestBody UserRegistrationDetails userRegistrationDetails) {
		return userService.addUser(userRegistrationDetails);
	}

	@PostMapping("/user/roles")
	public void replaceRoles(@RequestBody UserRoles userRoles) {
		userService.replaceRoles(userRoles);
	}

	@PostMapping("/forgotpassword")
	public ForgotPasswordResponse forgotPassword(@RequestBody UserRegistrationDetails userRegistrationDetails) {
		return userService.forgotPassword(userRegistrationDetails);
	}

	@PostMapping("/changepassword")
	public void changePasswordByUserId(@RequestBody UserRegistrationDetails userRegistrationDetails) {
		userService.changePasswordByUserId(userRegistrationDetails);
	}

	@PostMapping("/search/uers")
	public List<UserRes> filterUsers(@RequestBody FilterUserRes filterUserRes) {
		return userService.filterUsers(filterUserRes);
	}

}
