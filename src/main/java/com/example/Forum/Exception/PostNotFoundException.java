package com.example.Forum.Exception;

public class PostNotFoundException extends RuntimeException {

    private String errorMessage;

    public PostNotFoundException(String message) {
        super(message);
    }


}

