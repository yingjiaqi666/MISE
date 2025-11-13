package com.example.game.controller;

import com.example.game.dto.ApiResponse;
import com.example.game.dto.OptionDTO;
import com.example.game.entity.Option;
import com.example.game.service.OptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/options")
public class OptionApiController {

    @Autowired
    private OptionService optionService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<Long>> createOption(@RequestBody Option option) {
        Option savedOption = optionService.save(option);
        return ResponseEntity.ok(ApiResponse.success(savedOption.getId(), "创建成功"));
    }

    @DeleteMapping("/{optionId}")
    public ResponseEntity<ApiResponse<Void>> deleteOption(@PathVariable Long optionId) {
        if (optionService.findById(optionId) == null) {
            return ResponseEntity.badRequest().body(ApiResponse.error("400", "选项不存在"));
        }
        optionService.deleteById(optionId);
        return ResponseEntity.ok(ApiResponse.success(null, "删除成功"));
    }

    @GetMapping("/{optionId}")
    public ResponseEntity<ApiResponse<OptionDTO>> getOptionById(@PathVariable Long optionId) {
        Option option = optionService.findById(optionId);
        if (option == null) {
            return ResponseEntity.badRequest().body(ApiResponse.error("400", "选项不存在"));
        }
        return ResponseEntity.ok(ApiResponse.success(new OptionDTO(option)));
    }

    @PutMapping("/")
    public ResponseEntity<ApiResponse<String>> updateOption(@RequestBody Option option) {
        if (optionService.findById(option.getId()) == null) {
            return ResponseEntity.badRequest().body(ApiResponse.error("400", "选项不存在"));
        }
        optionService.updateOption(option.getId(), option);
        return ResponseEntity.ok(ApiResponse.success("更新成功"));
    }
}
