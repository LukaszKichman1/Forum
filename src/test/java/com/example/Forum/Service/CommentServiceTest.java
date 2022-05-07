package com.example.Forum.Service;

import com.example.Forum.Entity.User;
import com.example.Forum.Repository.CommentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;

class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;
    @Mock
    private PostService postService;
    @Mock
    private UserService userService;
    @Captor
    private ArgumentCaptor<User> argumentCaptor;
    private CommentService underTest;




    @BeforeEach
    @Deprecated
    void setUp() {
        MockitoAnnotations.initMocks(this);
        underTest=new CommentService(commentRepository,postService,userService);
    }

    @Test
    void itShouldSaveNewComment(){
        //given
        //when
        //then
    }

    @Test
    void itShouldNotSaveNewCommentBecauseUserIsNotAuthenticated(){
        //given
        //when
        //then
    }

    @Test
    void itShouldNotSaveNewCommentBecauseUserDoNotExist(){
        //given
        //when
        //then
    }

    @Test
    void itShouldNotSaveNewCommentBecauseThatPostDoNotExist(){
        //given
        //when
        //then
    }

    @Test
    void itShouldReturnCommentById(){
        //given
        //when
        //then
    }

    @Test
    void itShouldNotReturnCommentByIdBecauseThatPostDoNotExist(){
        //given
        //when
        //then
    }

    @Test
    void itShouldDeleteOwnCommentById(){
        //given
        //when
        //then
    }

    @Test
    void itShouldNotDeleteOwnCommentByIdBecauseUserDoNotExist(){
        //given
        //when
        //then
    }

    @Test
    void itShouldNotDeleteOwnCommentByIdBecauseThatUserDoNotExist(){
        //given
        //when
        //then
    }

    @Test
    void itShouldDeleteCommentById(){
        //given
        //when
        //then
    }

    @Test
    void itShouldNotDeleteCommentByIdBecauseUserIsNotAdmin(){
        //given
        //when
        //then
    }

    @Test
    void itShouldNotDeleteCommentByIdBecauseThatCommentDoNotExist(){
        //given
        //when
        //then
    }

    @Test
    void itShouldUpdateCommentById(){
        //given
        //when
        //then
    }

    @Test
    void itShouldNotUpdateCommentByIdBecauseThatCommentDoNotExist(){
        //given
        //when
        //then
    }

    @Test
    void itShouldNotUpdateCommentByIdUserIsNotAuthenticated(){
        //given
        //when
        //then
    }
}