package com.yasheenb.springboot_playground.Services;

import com.yasheenb.springboot_playground.Controllers.UserController;
import com.yasheenb.springboot_playground.Models.User;
import com.yasheenb.springboot_playground.Repositories.UserRepository;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserController {

    UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> getUser(Long userID) {
        return userRepository.findById(userID);
    }
}
