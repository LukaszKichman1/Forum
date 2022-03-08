package com.example.Forum.Service;

import com.example.Forum.Repository.TokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TokenSerivce {

    private TokenRepository tokenRepository;

    @Autowired
    public TokenSerivce(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }
}
