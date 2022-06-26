package com.example.Forum.Controller;

import com.example.Forum.Entity.Post;
import com.example.Forum.Entity.User;
import com.example.Forum.Service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/post")
public class PostController {

    private PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping("/addpost")
    public ResponseEntity<Post> addPost(@RequestBody Post post) {
        return ResponseEntity.ok(postService.save(post));
    }

    @GetMapping("/showall")
    public ResponseEntity<List<Post>> showAll() {
        return ResponseEntity.ok(postService.findAll());
    }

    @GetMapping("/showonebyid")
    public ResponseEntity<Optional> showOneById(@RequestParam int id) {
        return ResponseEntity.ok(postService.findById(id));
    }

    @DeleteMapping("/deleteyourownpostbyid")
    public ResponseEntity deleteOwnPostById(@RequestParam int id) {
        postService.deleteOwnPostById(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/deleteallyporownpost")
    public ResponseEntity deleteAllOwnPostById() {
        postService.deleteAllOwnPosts();
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/deleteonepostbyid")
    public ResponseEntity deletePostById(@RequestParam int id) {
        postService.deletePostById(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/updatepost")
    public ResponseEntity updatePost(@RequestParam String content, int id) {
        postService.updatePost(content, id);
        return ResponseEntity.ok().build();
    }

}