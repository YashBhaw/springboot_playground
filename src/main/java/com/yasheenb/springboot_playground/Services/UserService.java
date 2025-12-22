package com.yasheenb.springboot_playground.Services;

import com.yasheenb.springboot_playground.Models.User;
import com.yasheenb.springboot_playground.Repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public User addUser(User user) {
        return userRepository.save(user);
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUser(Long userID) {
        return userRepository.findById(userID);
    }

    public User updateUser(Long userID, User user) {
        user.setUserID(userID);
        return userRepository.save(user);
    }

    public void deleteUserByID(Long userID) {
        userRepository.deleteById(userID);
    }
}
