package com.example.game.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/profile")
    public String profile() {
        return "profile";
    }

    @GetMapping("/story-editor")
    public String storyEditor() {
        return "story-editor";
    }
    
    @GetMapping("/scene-editor")
    public String sceneEditor() {
        return "scene-editor";
    }
    
    @GetMapping("/play")
    public String play() {
        return "play";
    }
    
    @GetMapping("/my-stories")
    public String myStories() {
        return "my-stories";
    }

    @GetMapping("/my-scenes")
    public String myScenes() {
        return "my-scenes";
    }
}
