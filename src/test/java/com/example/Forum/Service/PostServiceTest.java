package com.example.Forum.Service;

import com.example.Forum.Entity.User;
import com.example.Forum.Repository.PostRepository;
import com.example.Forum.Repository.TokenRepository;
import com.example.Forum.Repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

class PostServiceTest {


    @Mock
    private UserService userService;
    @Mock
    private PostRepository postRepository;
    @Captor
    private ArgumentCaptor<User> argumentCaptor;
    private PostService underTest;


    @BeforeEach
    @Deprecated
    void setUp() {
        MockitoAnnotations.initMocks(this);
        underTest=new PostService(postRepository,userService);
    }

    @Test
    void itShouldSaveNewPost(){
        //given
        //when
        //then
    }

    @Test
    void itShouldNotSaveNewPostBecauseUserIsNotAutenthicated(){
        //given
        //when
        //then
    }

    @Test
    void itShouldNotSaveNewPostBecauseUserDoNotExist(){
        //given
        //when
        //then
    }

    @Test
    void itShouldReturnListOfAllPosts(){
        //given
        //when
        //then
    }

    @Test
    void itShouldReturnPostById(){
        //given
        //when
        //then
    }

    @Test
    void itShouldNotReturnPostByIdBecauseThatPostDoNotExist(){
        //given
        //when
        //then
    }

    @Test
    void itShouldDeletePostOwnByUser(){
        //given
        //when
        //then
    }

    @Test
    void itShouldNotDeletePostOwnByUserBecauseThatUserIsNotAuthenticated(){
        //given
        //when
        //then
    }

    @Test
    void itShouldNotDeletePostOwnByUserBecauseThatUserDoNotExist(){
        //given
        //when
        //then
    }

    @Test
    void itShouldDeleteAllPostsOwnByUser(){
        //given
        //when
        //then
    }

    @Test
    void itShouldNotDeleteAllPostsOwnByUserBecauseUserIsNotAutenticated(){
        //given
        //when
        //then
    }

    @Test
    void itShouldNotDeleteAllPostsOwnByUserBecauseUserDoNotExist(){
        //given
        //when
        //then
    }

    @Test
    void itShouldDeletePostById(){
        //given
        //when
        //then
    }

    @Test
    void itShouldNotDeletePostByIdBecauseUserIsNotAdmin(){
        //given
        //when
        //then
    }

    @Test
    void itShouldNotDeletePostByIdBecauseThatPostDoNotExist(){
        //given
        //when
        //then
    }

    @Test
    void itShouldUpdateOwnPostById(){
        //given
        //when
        //then
    }

    @Test
    void itShouldNotUpdateOwnPostByIdBecauseUserDoNotExist(){
        //given
        //when
        //then
    }

    @Test
    void itShouldNotUpdateOwnPostByIdBecauseThatPostDoNotExist(){
        //given
        //when
        //then
    }

    @Test
    void itShouldNotUpdateOwnPostByIdBecauseUserIsNotAutenticated(){
        //given
        //when
        //then
    }
}