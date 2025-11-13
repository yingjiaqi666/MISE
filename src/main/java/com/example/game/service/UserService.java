package com.example.game.service;

import com.example.game.entity.User;
import com.example.game.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public User register(String username, String password) {
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(passwordEncoder.encode(password));
        return userRepository.save(newUser);
    }

    public boolean checkPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public List<User> findByName(String name) {
        return userRepository.findByNameContaining(name);
    }

    public User updateUser(Long id, User userDetails) {
        User user = findById(id);
        if (user != null) {
            user.setName(userDetails.getName());
            user.setPicture(userDetails.getPicture());
            user.setPoints(userDetails.getPoints());
            user.setPower(userDetails.getPower());
            user.setAgile(userDetails.getAgile());
            user.setPerception(userDetails.getPerception());
            user.setIntelligence(userDetails.getIntelligence());
            user.setCharm(userDetails.getCharm());
            // Products might need special handling for update
            return userRepository.save(user);
        }
        return null;
    }

    public List<User> searchUsers(String keyword) {
        return userRepository.findByUsernameContaining(keyword);
    }
}
