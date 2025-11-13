package com.example.game.service;

import com.example.game.entity.Comment;
import com.example.game.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    public Comment save(Comment comment) {
        return commentRepository.save(comment);
    }

    public Comment findById(Long id) {
        return commentRepository.findById(id).orElse(null);
    }

    public void deleteById(Long id) {
        commentRepository.deleteById(id);
    }

    public List<Comment> findByAuthorId(Long authorId) {
        return commentRepository.findByAuthorId(authorId);
    }

    public Comment updateComment(Long id, Comment commentDetails) {
        Comment comment = findById(id);
        if (comment != null) {
            comment.setText(commentDetails.getText());
            comment.setPicture(commentDetails.getPicture());
            return commentRepository.save(comment);
        }
        return null;
    }
}
