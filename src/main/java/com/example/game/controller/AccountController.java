package com.example.game.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.game.dto.ApiResponse;
import com.example.game.dto.AttributeUpdateRequest;
import com.example.game.dto.LoginRequest;
import com.example.game.dto.RegisterRequest;
import com.example.game.dto.UserDTO;
import com.example.game.entity.User;
import com.example.game.repository.UserRepository;
import com.example.game.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<String>> authenticateUser(@RequestBody LoginRequest loginRequest, HttpServletRequest request) {
        User user = userService.findByUsername(loginRequest.getUsername());
        if (user != null && userService.checkPassword(loginRequest.getPassword(), user.getPassword())) {
            HttpSession session = request.getSession(true);
            session.setAttribute("user", user);
            return ResponseEntity.ok(ApiResponse.success(user.getUsername(), "Login successful"));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.error("401", "Invalid credentials"));
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserDTO>> registerUser(@RequestBody RegisterRequest registerRequest) {
        User existingUser = userService.findByUsername(registerRequest.getUsername());
        if (existingUser != null) {
            return ResponseEntity.badRequest().body(ApiResponse.error("400", "Username already exists"));
        }
        User newUser = userService.register(registerRequest.getUsername(), registerRequest.getPassword());
        return ResponseEntity.ok(ApiResponse.success(new UserDTO(newUser), "User registered successfully"));
    }

    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<UserDTO>> getProfile(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.error("401", "Not logged in"));
        }
        return ResponseEntity.ok(ApiResponse.success(new UserDTO(user), "Profile fetched successfully"));
    }

    @PutMapping("/profile/attributes")
    public ResponseEntity<ApiResponse<UserDTO>> updateAttributes(@RequestBody AttributeUpdateRequest request, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.error("401", "Not logged in"));
        }
        
        // Re-fetch user to ensure data is fresh before update
        user = userService.findByUsername(user.getUsername());

        // Validate that new attributes are not negative
        if (request.getPower() < 0 || request.getAgile() < 0 || request.getPerception() < 0 || request.getIntelligence() < 0 || request.getCharm() < 0) {
            return ResponseEntity.badRequest().body(ApiResponse.error("400", "Attribute values cannot be negative."));
        }

        int pointsToSpend = 0;
        int pointsToRefund = 0;

        // Calculate points to spend
        if (request.getPower() > user.getPower()) pointsToSpend += request.getPower() - user.getPower();
        if (request.getAgile() > user.getAgile()) pointsToSpend += request.getAgile() - user.getAgile();
        if (request.getPerception() > user.getPerception()) pointsToSpend += request.getPerception() - user.getPerception();
        if (request.getIntelligence() > user.getIntelligence()) pointsToSpend += request.getIntelligence() - user.getIntelligence();
        if (request.getCharm() > user.getCharm()) pointsToSpend += request.getCharm() - user.getCharm();

        if (pointsToSpend > user.getPoints()) {
            return ResponseEntity.badRequest().body(ApiResponse.error("400", "Not enough points to spend."));
        }

        // Calculate points to refund
        if (request.getPower() < user.getPower()) pointsToRefund += user.getPower() - request.getPower();
        if (request.getAgile() < user.getAgile()) pointsToRefund += user.getAgile() - request.getAgile();
        if (request.getPerception() < user.getPerception()) pointsToRefund += user.getPerception() - request.getPerception();
        if (request.getIntelligence() < user.getIntelligence()) pointsToRefund += user.getIntelligence() - request.getIntelligence();
        if (request.getCharm() < user.getCharm()) pointsToRefund += user.getCharm() - request.getCharm();

        // Update user attributes and points
        user.setPoints(user.getPoints() - pointsToSpend + pointsToRefund);
        user.setPower(request.getPower());
        user.setAgile(request.getAgile());
        user.setPerception(request.getPerception());
        user.setIntelligence(request.getIntelligence());
        user.setCharm(request.getCharm());
        
        User updatedUser = userRepository.save(user);
        session.setAttribute("user", updatedUser); // Update session with the latest user data
        return ResponseEntity.ok(ApiResponse.success(new UserDTO(updatedUser), "Attributes updated successfully!"));
    }

    @PostMapping("/profile/reset-attributes")
    public ResponseEntity<ApiResponse<UserDTO>> resetAttributes(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.error("401", "Not logged in"));
        }
        user = userService.findByUsername(user.getUsername());
        int totalAttributes = user.getPower() + user.getAgile() + user.getPerception() + user.getIntelligence() + user.getCharm();
        
        user.setPoints(user.getPoints() + totalAttributes);
        user.setPower(0);
        user.setAgile(0);
        user.setPerception(0);
        user.setIntelligence(0);
        user.setCharm(0);

        User updatedUser = userRepository.save(user);
        session.setAttribute("user", updatedUser); // Update session with the latest user data
        return ResponseEntity.ok(ApiResponse.success(new UserDTO(updatedUser), "Attributes reset"));
    }
    
    @PostMapping("/search")
    public ResponseEntity<ApiResponse<List<UserDTO>>> searchUserByName(@RequestBody UserSearchRequest request) {
        List<User> users = userService.findByName(request.getName());
        List<UserDTO> userDTOs = users.stream().map(UserDTO::new).collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success(userDTOs));
    }

    @PutMapping("/{userId}")
    public ResponseEntity<ApiResponse<String>> updateUser(@PathVariable Long userId, @RequestBody User userUpdateRequest) {
        User user = userService.findById(userId);
        if (user == null) {
            return ResponseEntity.badRequest().body(ApiResponse.error("400", "用户不存在"));
        }
        userService.updateUser(userId, userUpdateRequest);
        return ResponseEntity.ok(ApiResponse.success("更新成功"));
    }

    static class UserSearchRequest {
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
