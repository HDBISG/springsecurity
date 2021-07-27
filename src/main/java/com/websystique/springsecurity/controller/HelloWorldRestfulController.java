package com.websystique.springsecurity.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.websystique.springsecurity.model.User;
import com.websystique.springsecurity.service.UserService;

@RestController
@RequestMapping(value = { "/restful" })
public class HelloWorldRestfulController {

	@Autowired
	UserService service;
	
    @RequestMapping(value = { "/", "/list" }, method = RequestMethod.GET)
    public Object listAllUsers(ModelMap model) {
 
        List<User> users = service.findAllUsers();
        return users;
    }
	
    @RequestMapping(value = { "/edit-user-{id}" }, method = RequestMethod.GET)
    public Object editUser(@PathVariable int id, ModelMap model) {
        User user  = service.findById(id);
        model.addAttribute("user", user);
        model.addAttribute("edit", true);
        
        
        return user;
    }
    
    @RequestMapping(value = { "/edit-user-{id}" }, method = RequestMethod.POST)
    public Object updateUser(User user, ModelMap model, @PathVariable int id) {
        service.updateUser(user);
        model.addAttribute("success", "User " + user.getFirstName()  + " updated successfully");
        return user;
    }
    
    @RequestMapping(value = { "/delete-user-{id}" }, method = RequestMethod.GET)
    public Object deleteUser(@PathVariable int id) {
        service.deleteUser(id);

        String userName = getPrincipal();
        
        return userName;
    }

    
    
    
	@RequestMapping(value = "/Access_Denied", method = RequestMethod.GET)
	public String accessDeniedPage(ModelMap model) {
		model.addAttribute("user", getPrincipal());
		return "accessDenied";
	}

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String loginPage() {
		return "login";
	}

	@RequestMapping(value="/logout", method = RequestMethod.GET)
	public String logoutPage (HttpServletRequest request, HttpServletResponse response) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null){    
			new SecurityContextLogoutHandler().logout(request, response, auth);
		}
		return "redirect:/login?logout";
	}

	private String getPrincipal(){
		String userName = null;
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		if (principal instanceof UserDetails) {
			userName = ((UserDetails)principal).getUsername();
		} else {
			userName = principal.toString();
		}
		return userName;
	}

}