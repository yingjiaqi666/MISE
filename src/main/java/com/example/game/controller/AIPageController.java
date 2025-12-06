package com.example.game.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AIPageController {

    // 专门处理页面路由
    @GetMapping("/ai")
    public String aiAssistantPage() {
        return "ai"; // 返回ai.html页面
    }
}