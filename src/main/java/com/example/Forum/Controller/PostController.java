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

    @PostMapping("/add")
    public ResponseEntity<Post> addPost(@RequestBody Post post) {
        return ResponseEntity.ok(postService.save(post));
    }

    @GetMapping("/showall")
    public ResponseEntity<List<Post>> showAll() {
        return ResponseEntity.ok(postService.findAll());
    }

    @GetMapping("/showonybyid")
    public ResponseEntity<Optional> showOneById(@RequestParam int id) {
        return ResponseEntity.ok(postService.findById(id));
    }

    @DeleteMapping("/deleteownpostbyid")
    public ResponseEntity deleteOwnPostById(@RequestParam int id) {
        postService.deleteOwnPostById(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/deleteallownpost")
    public ResponseEntity deleteAllOwnPostById() {
        postService.deleteAllOwnPosts();
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/deletepostbyid")
    public ResponseEntity deletePostById(@RequestParam int id) {
        postService.deletePostById(id);
        return ResponseEntity.ok().build();
    }
    
}
