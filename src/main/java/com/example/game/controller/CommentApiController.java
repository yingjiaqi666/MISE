package com.example.game.controller;

import com.example.game.dto.ApiResponse;
import com.example.game.dto.CommentDTO;
import com.example.game.entity.Comment;
import com.example.game.entity.User;
import com.example.game.service.CommentService;
import com.example.game.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/comments")
public class CommentApiController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private UserService userService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<Long>> createComment(@RequestBody Comment comment) {
        User author = userService.findById(comment.getAuthor().getId());
        if (author == null) {
            return ResponseEntity.badRequest().body(ApiResponse.error("400", "作者不存在"));
        }
        comment.setAuthor(author);
        Comment savedComment = commentService.save(comment);
        return ResponseEntity.ok(ApiResponse.success(savedComment.getId(), "创建成功"));
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<ApiResponse<Void>> deleteComment(@PathVariable Long commentId) {
        if (commentService.findById(commentId) == null) {
            return ResponseEntity.badRequest().body(ApiResponse.error("400", "评论不存在"));
        }
        commentService.deleteById(commentId);
        return ResponseEntity.ok(ApiResponse.success(null, "删除成功"));
    }

    @GetMapping("/{commentId}")
    public ResponseEntity<ApiResponse<CommentDTO>> getCommentById(@PathVariable Long commentId) {
        Comment comment = commentService.findById(commentId);
        if (comment == null) {
            return ResponseEntity.badRequest().body(ApiResponse.error("400", "评论不存在"));
        }
        return ResponseEntity.ok(ApiResponse.success(new CommentDTO(comment)));
    }

    @PostMapping("/search/author")
    public ResponseEntity<ApiResponse<List<CommentDTO>>> searchCommentByAuthor(@RequestBody AuthorIdSearchRequest request) {
        List<Comment> comments = commentService.findByAuthorId(request.getAuthorId());
        List<CommentDTO> commentDTOs = comments.stream().map(CommentDTO::new).collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success(commentDTOs));
    }

    @PutMapping("/")
    public ResponseEntity<ApiResponse<String>> updateComment(@RequestBody Comment comment) {
        if (commentService.findById(comment.getId()) == null) {
            return ResponseEntity.badRequest().body(ApiResponse.error("400", "评论不存在"));
        }
        commentService.updateComment(comment.getId(), comment);
        return ResponseEntity.ok(ApiResponse.success("更新成功"));
    }

    static class AuthorIdSearchRequest {
        private Long authorId;
        public Long getAuthorId() { return authorId; }
        public void setAuthorId(Long authorId) { this.authorId = authorId; }
    }
}
