package com.example.Forum.Controller;


import com.example.Forum.Entity.User;
import com.example.Forum.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/registration")
    public ResponseEntity userRegistraion(@RequestBody User user)
    {
        userService.save(user);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/activation")
    public ResponseEntity userActivation(@RequestParam String login,int valueOfToken)
    {
        userService.activationUser(login,valueOfToken);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/oneuser")
    public Optional<User> findUserById(@RequestParam int id)
    {
        return userService.findById(id);
    }


}