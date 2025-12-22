package com.yasheenb.springboot_playground.Controllers;

import com.yasheenb.springboot_playground.Models.User;
import com.yasheenb.springboot_playground.Services.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserControllerImplementation {

    UserService userService;

    public UserControllerImplementation(UserService userService){
        this.userService = userService;
    }

    @GetMapping
    public List<User> getUsers(){
        return userService.getUsers();
    }

    @GetMapping("/{userID}")
    public Optional<User> getUser(@PathVariable Long userID){
        return userService.getUser(userID);
    }
}