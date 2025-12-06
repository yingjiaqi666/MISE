//package com.example.game.controller;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.*;
//import com.example.game.service.AIService;
//import com.example.game.model.AnalysisRequest;
//import com.example.game.model.ContentGenerationRequest;
//
//import java.util.Map;
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/ai")
//public class AIController {
//
//    @Autowired
//    private AIService aiService;
//
//
//    // AI聊天端点 - 处理通用AI对话
//    @PostMapping("/chat")
//    @ResponseBody
//    public ResponseEntity<Map<String, Object>> handleAIChat(@RequestBody Map<String, String> request) {
//        String prompt = request.get("prompt");
//
//        // 输入验证[6](@ref)
//        if (prompt == null || prompt.trim().isEmpty()) {
//            return ResponseEntity.badRequest()
//                    .body(Map.of("code", "400", "msg", "prompt不能为空"));
//        }
//
//        try {
//            String aiResponse = aiService.getAIResponse(prompt);
//            return ResponseEntity.ok()
//                    .body(Map.of("code", "200", "msg", "成功", "data", aiResponse));
//        } catch (Exception e) {
//            return ResponseEntity.internalServerError()
//                    .body(Map.of("code", "500", "msg", "AI服务处理失败: " + e.getMessage()));
//        }
//    }
//
//    // 故事/场景分析端点 - 专门用于TRPG游戏场景分析
//    @PostMapping("/analyze")
//    @ResponseBody
//    public ResponseEntity<Map<String, Object>> analyzeStoryScene(@RequestBody AnalysisRequest request) {
//        try {
//            String analysisResult = aiService.analyzeScene(
//                    request.getSceneDescription(),
//                    request.getPlayerAction(),
//                    request.getBranches()
//            );
//
//            return ResponseEntity.ok()
//                    .body(Map.of("code", "200", "msg", "分析成功", "data", analysisResult));
//        } catch (Exception e) {
//            return ResponseEntity.internalServerError()
//                    .body(Map.of("code", "500", "msg", "场景分析失败: " + e.getMessage()));
//        }
//    }
//
//    // 内容生成端点 - 用于生成故事内容、描述等
//    @PostMapping("/generate")
//    @ResponseBody
//    public ResponseEntity<Map<String, Object>> generateContent(@RequestBody ContentGenerationRequest request) {
//        try {
//            String generatedContent = aiService.generateContent(
//                    request.getContentType(),
//                    request.getTheme(),
//                    request.getParameters()
//            );
//
//            return ResponseEntity.ok()
//                    .body(Map.of("code", "200", "msg", "生成成功", "data", generatedContent));
//        } catch (Exception e) {
//            return ResponseEntity.internalServerError()
//                    .body(Map.of("code", "500", "msg", "内容生成失败: " + e.getMessage()));
//        }
//    }
//
//
//}

//of是java 9的方法，为了兼容项目java 8版本，改成如下代码：

package com.example.game.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.game.service.AIService;
import com.example.game.model.AnalysisRequest;
import com.example.game.model.ContentGenerationRequest;

import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/ai")
public class AIController {

    @Autowired
    private AIService aiService;

    // AI聊天端点 - 处理通用AI对话
    @PostMapping("/chat")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> handleAIChat(@RequestBody Map<String, String> request) {
        String prompt = request.get("prompt");

        // 输入验证
        if (prompt == null || prompt.trim().isEmpty()) {
            Map<String, Object> response = new HashMap<>();
            response.put("code", "400");
            response.put("msg", "prompt不能为空");
            return ResponseEntity.badRequest().body(response);
        }

        try {
            String aiResponse = aiService.getAIResponse(prompt);
            Map<String, Object> response = new HashMap<>();
            response.put("code", "200");
            response.put("msg", "成功");
            response.put("data", aiResponse);
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("code", "500");
            response.put("msg", "AI服务处理失败: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    // 故事/场景分析端点 - 专门用于TRPG游戏场景分析
    @PostMapping("/analyze")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> analyzeStoryScene(@RequestBody AnalysisRequest request) {
        try {
            String analysisResult = aiService.analyzeScene(
                    request.getSceneDescription(),
                    request.getPlayerAction(),
                    request.getBranches()
            );

            Map<String, Object> response = new HashMap<>();
            response.put("code", "200");
            response.put("msg", "分析成功");
            response.put("data", analysisResult);
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("code", "500");
            response.put("msg", "场景分析失败: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    // 内容生成端点 - 用于生成故事内容、描述等
    @PostMapping("/generate")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> generateContent(@RequestBody ContentGenerationRequest request) {
        try {
            String generatedContent = aiService.generateContent(
                    request.getContentType(),
                    request.getTheme(),
                    request.getParameters()
            );

            Map<String, Object> response = new HashMap<>();
            response.put("code", "200");
            response.put("msg", "生成成功");
            response.put("data", generatedContent);
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("code", "500");
            response.put("msg", "内容生成失败: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
}