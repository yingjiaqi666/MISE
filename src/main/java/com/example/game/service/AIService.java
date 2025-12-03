package com.example.game.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.*;

@Service
public class AIService {

    @Value("${ai.api.key:sk-jnemwzsxroidoysnnthpgqgftqzmzrukqqqagqnnpgzqorgv}")
    private String apiKey;

    @Value("${ai.api.url:https://api.siliconflow.cn/v1/chat/completions}")
    private String apiUrl;

    @Value("${ai.model:deepseek-ai/DeepSeek-R1-0528-Qwen3-8B}")
    private String modelName;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public AIService(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    /**
     * 通用AI对话服务
     */
    public String getAIResponse(String prompt) {
        try {
            // 构建请求头
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + apiKey);

            // 构建请求体（使用Map更安全，避免字符串拼接问题）
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", modelName);
            requestBody.put("messages", Collections.singletonList(
                    Map.of("role", "user", "content", prompt)
            ));
            requestBody.put("max_tokens", 2048);
            requestBody.put("temperature", 0.7);

            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

            // 发送请求[1](@ref)
            ResponseEntity<String> response = restTemplate.exchange(
                    apiUrl, HttpMethod.POST, requestEntity, String.class);

            // 解析响应
            if (response.getStatusCode() == HttpStatus.OK) {
                JsonNode root = objectMapper.readTree(response.getBody());
                return root.path("choices").get(0).path("message").path("content").asText();
            } else {
                throw new RuntimeException("AI API请求失败，状态码: " + response.getStatusCode());
            }
        } catch (Exception e) {
            throw new RuntimeException("获取AI响应失败: " + e.getMessage(), e);
        }
    }

    /**
     * 场景分析方法 - 专门用于TRPG游戏分支分析
     */
    public String analyzeScene(String sceneDescription, String playerAction, List<String> branches) {
        // 构建针对TRPG场景分析的提示词
        String systemPrompt = buildSceneAnalysisPrompt(sceneDescription, branches);
        String fullPrompt = systemPrompt + "\n玩家行动: " + playerAction;

        return getAIResponse(fullPrompt);
    }

    /**
     * 内容生成方法
     */
    public String generateContent(String contentType, String theme, Map<String, String> parameters) {
        String prompt = buildGenerationPrompt(contentType, theme, parameters);
        return getAIResponse(prompt);
    }


    /**
     * 构建场景分析提示词
     */
    private String buildSceneAnalysisPrompt(String sceneDescription, List<String> branches) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("你是一个TRPG游戏主持人。分析玩家行动并匹配最合适的分支。\n\n")
                .append("场景描述：").append(sceneDescription).append("\n\n")
                .append("可选分支：\n");

        for (int i = 0; i < branches.size(); i++) {
            prompt.append(i).append(": ").append(branches.get(i)).append("\n");
        }

        prompt.append("\n请分析玩家行动最匹配哪个分支。你需要且只需要给出一个整数的值，" +
                "若玩家的行为满足了可选分支中的某一条，则你应该输出该分支的编号（从0开始）" +
                "若玩家的操作同时满足了可选分支中的多条，则只输出最靠前的一条分支的编号" +
                "若玩家的操作未满足任何一条可选分支中的内容，则你应该输出-1" +
                "除了这些之外，不要输出任何内容，确保你的输出内容包含且仅包含如上所描述的一个整数。");
        return prompt.toString();
    }

    /**
     * 构建内容生成提示词
     */
    private String buildGenerationPrompt(String contentType, String theme, Map<String, String> parameters) {
        switch (contentType.toLowerCase()) {
            case "story":
                return String.format("创作一个关于'%s'的TRPG故事场景，包含环境描述、角色和互动可能性。", theme);
            case "description":
                return String.format("为'%s'场景编写详细的环境描述，包含视觉、听觉、嗅觉和氛围细节。", theme);
            case "dialogue":
                return String.format("为'%s'情境创作角色对话，体现角色性格和情节发展。", theme);
            case "character":
                return String.format("创建一个关于'%s'的TRPG角色描述，包括外貌、性格、背景和能力。", theme);
            default:
                return String.format("根据主题'%s'生成相关内容。", theme);
        }
    }
}