package com.example.Forum.Controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Who {

    @GetMapping("/who")
    public String whoIm() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

}
