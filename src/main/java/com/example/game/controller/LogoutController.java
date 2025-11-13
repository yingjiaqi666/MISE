package com.example.game.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LogoutController {

    /**
     * Handle POST logout (used by the header form). Invalidate session and redirect to login page.
     */
    @PostMapping("/logout")
    public String logoutPost(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/login";
    }

    /**
     * Convenience GET logout for browsers / testing.
     */
    @GetMapping("/logout")
    public String logoutGet(HttpServletRequest request) {
        return logoutPost(request);
    }
}
