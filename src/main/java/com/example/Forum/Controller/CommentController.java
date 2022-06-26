package com.example.Forum.Controller;

import com.example.Forum.Entity.Comment;
import com.example.Forum.Service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comment")
public class CommentController {

    private CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/addcomment")
    public ResponseEntity<Comment> addComment(@RequestParam int id, String content) {
        return ResponseEntity.ok(commentService.save(id, content));
    }

    @DeleteMapping("/deleteyourowncommentbyid")
    public ResponseEntity deleteOwnCommentById(@RequestParam int id) {
        commentService.deleteOwnCommentById(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/deletecommentbyid")
    public ResponseEntity delteCommentById(@RequestParam int id) {
        commentService.deleteCommentById(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/updatecomment")
    public ResponseEntity updateComment(@RequestParam String content, int id) {
        commentService.updateComment(content, id);
        return ResponseEntity.ok().build();
    }

}
