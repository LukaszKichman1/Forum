package com.example.Forum.Exception;

public class CommentNotFoundException extends RuntimeException{

    private String errorMessage;

    public CommentNotFoundException(String message)
    {
        super(message);
    }


    }
