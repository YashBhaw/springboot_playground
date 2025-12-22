package com.yasheenb.springboot_playground.Controllers;

import com.yasheenb.springboot_playground.Models.User;
import com.yasheenb.springboot_playground.Services.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserControllerImplementation {

    UserService userService;

    public UserControllerImplementation(UserService userService){
        this.userService = userService;
    }

    @PostMapping("/addUser")
    public User addUser(@RequestBody User user){
        return userService.addUser(user);
    }

    @GetMapping
    public List<User> getUsers(){
        return userService.getUsers();
    }

    @GetMapping("/{userID}")
    public Optional<User> getUser(@PathVariable Long userID){
        return userService.getUser(userID);
    }

    @PutMapping("updateUser/{userID}")
    public User updateUser(@PathVariable Long userID, @RequestBody User user){
        return userService.updateUser(userID, user);
    }

    @DeleteMapping("/{userID}")
    public void deleteUser(@PathVariable Long userID){
        userService.deleteUserByID(userID);
    }

}