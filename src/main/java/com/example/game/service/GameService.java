package com.example.game.service;

import com.example.game.entity.Option;
import com.example.game.entity.User;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class GameService {

    private final Random random = new Random();

    public Long getNextSceneId(User user, Option option) {
        String type = option.getType();
        if (type == null || type.isBlank() || "通用".equals(type) || "None".equalsIgnoreCase(type)) {
            return option.getSuccess();
        }

        int roll = random.nextInt(20) + 1;
        int attributeValue = 0;

        // Support both English keys (power, agile, perception, intelligence, charm)
        // and Chinese display names (力量, 敏捷, 智力, 感知, 魅力)
        switch (type.toLowerCase()) {
            case "力量":
            case "power":
                attributeValue = user.getPower();
                break;
            case "敏捷":
            case "agile":
                attributeValue = user.getAgile();
                break;
            case "智力":
            case "intelligence":
                attributeValue = user.getIntelligence();
                break;
            case "感知":
            case "perception":
                attributeValue = user.getPerception();
                break;
            case "魅力":
            case "charm":
                attributeValue = user.getCharm();
                break;
            default:
                // Unknown attribute type - treat as automatic success
                return option.getSuccess();
        }

        if (roll + attributeValue >= option.getDc()) {
            return option.getSuccess();
        } else {
            return option.getFail();
        }
    }
}
