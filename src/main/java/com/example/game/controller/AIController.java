package com.example.game.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AIController {

    @GetMapping("/ai")
    public String aiAssistant() {
        return "ai";
    }

    // AI API 端点 - AI开发者可以在这里添加更多的端点
    // 例如：
    // @PostMapping("/api/ai/chat") - 处理AI聊天
    // @PostMapping("/api/ai/generate") - 生成内容
    // @GetMapping("/api/ai/models") - 获取可用模型
    // @PostMapping("/api/ai/analyze") - 分析故事/场景
}