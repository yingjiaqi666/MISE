package com.example.game.controller;

import com.example.game.dto.ApiResponse;
import com.example.game.dto.ProductDTO;
import com.example.game.entity.Product;
import com.example.game.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
public class ProductApiController {

    @Autowired
    private ProductService productService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<Long>> createProduct(@RequestBody Product product) {
        Product savedProduct = productService.save(product);
        return ResponseEntity.ok(ApiResponse.success(savedProduct.getId(), "创建成功"));
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ApiResponse<ProductDTO>> getProductById(@PathVariable Long productId) {
        Product product = productService.findById(productId);
        if (product == null) {
            return ResponseEntity.badRequest().body(ApiResponse.error("400", "道具不存在"));
        }
        return ResponseEntity.ok(ApiResponse.success(new ProductDTO(product)));
    }

    @PutMapping("/")
    public ResponseEntity<ApiResponse<String>> updateProduct(@RequestBody Product product) {
        if (productService.findById(product.getId()) == null) {
            return ResponseEntity.badRequest().body(ApiResponse.error("400", "道具不存在"));
        }
        productService.updateProduct(product.getId(), product);
        return ResponseEntity.ok(ApiResponse.success("更新成功"));
    }
}
