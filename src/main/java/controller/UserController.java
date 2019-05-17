package controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import mapper.ModelMappers;
import model.User;
import model.UserRegistrationDetails;
import model.UserRes;
import service.IUserService;

@RestController
public class UserController {
	@Autowired
	public IUserService userService;

	//@Secured("ADMIN")
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/user")
	public List<UserRes> getUser() {
		List<User> users = userService.getUsers();
		List<UserRes> userResList = new ArrayList<UserRes>();
		for (User user : users) {
			UserRes userRes = new UserRes();
			ModelMappers.mapUserToUserRes(user, userRes);
			userResList.add(userRes);
		}
		return userResList;
	}

	@Secured("SALES_REP")
	@GetMapping("/user/{userName}")
	public UserRes getUserByUserName(@PathVariable("userName") String userName) {
		User user = userService.getUserByUserName(userName);
		UserRes userRes = new UserRes();
		ModelMappers.mapUserToUserRes(user, userRes);
		return userRes;
	}

	@PostMapping("/user")
	public void addUser(@RequestBody UserRegistrationDetails userRegistrationDetails) {
		userService.addUser(userRegistrationDetails);
	}
}
