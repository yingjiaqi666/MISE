package com.example.game.controller;

import com.example.game.dto.ApiResponse;
import com.example.game.dto.PlayRequest;
import com.example.game.entity.Option;
import com.example.game.entity.User;
import com.example.game.service.GameService;
import com.example.game.service.OptionService;
import com.example.game.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/game")
public class GameController {

    @Autowired
    private GameService gameService;

    @Autowired
    private UserService userService;

    @Autowired
    private OptionService optionService;

    @PostMapping("/play")
    public ResponseEntity<ApiResponse<Long>> play(@RequestBody PlayRequest playRequest, jakarta.servlet.http.HttpSession session) {
        // Try userId from request first; fall back to session user
        User user = null;
        if (playRequest.getUserId() != null) {
            user = userService.findById(playRequest.getUserId());
        }
        if (user == null) {
            Object sessionUser = session.getAttribute("user");
            if (sessionUser instanceof User) user = (User) sessionUser;
        }
        if (user == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("401", "用户未登录或不存在"));
        }

        Option option = optionService.findById(playRequest.getOptionId());
        if (option == null) {
            return ResponseEntity.badRequest().body(ApiResponse.error("400", "选项不存在"));
        }

        Long nextSceneId = gameService.getNextSceneId(user, option);
        if( nextSceneId == option.getSuccess()){
            return ResponseEntity.ok(ApiResponse.success(nextSceneId, "检定成功"));
        }else if(nextSceneId == option.getFail()){
            return ResponseEntity.ok(ApiResponse.success(nextSceneId, "检定失败"));

        }
        return ResponseEntity.ok(ApiResponse.success(nextSceneId, "检定成功"));
    }
}
