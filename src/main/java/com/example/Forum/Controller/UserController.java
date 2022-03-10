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
    public ResponseEntity<User> userRegistraion(@RequestBody User user)
    {
        return ResponseEntity.ok(userService.save(user));
    }

    @PostMapping("/activation")
    public ResponseEntity userActivation(@RequestParam String login,int valueOfToken)
    {
        userService.activationUser(login,valueOfToken);
        return ResponseEntity.ok().build();
    }


    @GetMapping("/showoneuserbyid")
    public ResponseEntity<Optional> findUserById(@RequestParam int id)
    {
        return ResponseEntity.ok(userService.findById(id));
    }

    @GetMapping("/showoneuserbynickname")
    public ResponseEntity<Optional> findUserByNickName(@RequestParam String nickName)
    {
        return ResponseEntity.ok(userService.findByNickName(nickName));
    }

    @DeleteMapping("/deletebyid")
    public ResponseEntity deleteUserById(@RequestParam int id)
    {
        userService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/all")
    public ResponseEntity<List<User>> findAllUsers()
    {
        return ResponseEntity.ok(userService.findAll());
    }

}