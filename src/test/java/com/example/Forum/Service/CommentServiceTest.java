package com.example.Forum.Service;

import com.example.Forum.Entity.Comment;
import com.example.Forum.Entity.Post;
import com.example.Forum.Entity.User;
import com.example.Forum.Exception.CommentNotFoundException;
import com.example.Forum.Exception.PostNotFoundException;
import com.example.Forum.Exception.UserIsNotOwnerException;
import com.example.Forum.Exception.UserNotFoundException;
import com.example.Forum.Repository.CommentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;
    @Mock
    private PostService postService;
    @Mock
    private UserService userService;
    @Captor
    private ArgumentCaptor<Comment> argumentCaptor;
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

        User user = new User.Builder()
                .nickName("nick")
                .login("login")
                .password("dupa")
                .email("email@gmail.com")
                .roles("ROLE_USER")
                .isEnabled(false)
                .build();

        Post post = new Post.Builder()
                .content("content")
                .user(user)
                .build();

        Comment comment =new Comment.Builder()
                .content("comment content")
                .user(user)
                .post(post)
                .build();

        user.getPostList().add(post);
        user.addComment(comment);
        post.addComment(comment);

        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(user);

        given(SecurityContextHolder.getContext().getAuthentication().getName()).willReturn(user.getNickName());

        //when

        when(userService.findByNickName(user.getNickName())).thenReturn(Optional.of(user));
        when(postService.findById(post.getId_post())).thenReturn(Optional.of(post));

        underTest.save(post.getId_post(),comment.getContent());

        //then
        then(commentRepository).should().save(argumentCaptor.capture());
        Comment argumentCaptorValue = argumentCaptor.getValue();
        assertThat(argumentCaptorValue.getContent()).isEqualTo("comment content");
        assertThat(argumentCaptorValue.getUser()).isEqualTo(user);
        assertThat(argumentCaptorValue.getPost()).isEqualTo(post);

    }



    @Test
    void itShouldNotSaveNewCommentBecauseUserDoNotExist(){
        //given

        User user = new User.Builder()
                .nickName("nick")
                .login("login")
                .password("dupa")
                .email("email@gmail.com")
                .roles("ROLE_USER")
                .isEnabled(false)
                .build();

        Post post = new Post.Builder()
                .content("content")
                .user(user)
                .build();

        Comment comment =new Comment.Builder()
                .content("comment content")
                .user(user)
                .post(post)
                .build();

        user.getPostList().add(post);
        user.addComment(comment);
        post.addComment(comment);

        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(user);

        given(SecurityContextHolder.getContext().getAuthentication().getName()).willReturn(user.getNickName());

        //when

        when(userService.findByNickName(user.getNickName())).thenReturn(Optional.empty());
        when(postService.findById(post.getId_post())).thenReturn(Optional.of(post));

        //then
        assertThatThrownBy(() -> underTest.save(post.getId_post(),comment.getContent()))
                .isExactlyInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("we cant find user with that Id");
    }

    @Test
    void itShouldNotSaveNewCommentBecauseThatPostDoNotExist(){
        //given

        User user = new User.Builder()
                .nickName("nick")
                .login("login")
                .password("dupa")
                .email("email@gmail.com")
                .roles("ROLE_USER")
                .isEnabled(false)
                .build();

        Post post = new Post.Builder()
                .content("content")
                .user(user)
                .build();

        Comment comment =new Comment.Builder()
                .content("comment content")
                .user(user)
                .post(post)
                .build();

        user.getPostList().add(post);
        user.addComment(comment);
        post.addComment(comment);

        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(user);

        given(SecurityContextHolder.getContext().getAuthentication().getName()).willReturn(user.getNickName());

        //when

        when(userService.findByNickName(user.getNickName())).thenReturn(Optional.of(user));
        when(postService.findById(post.getId_post())).thenReturn(Optional.empty());

        //then
        assertThatThrownBy(() -> underTest.save(post.getId_post(),comment.getContent()))
                .isExactlyInstanceOf(PostNotFoundException.class)
                .hasMessageContaining("we cant find post with that Id");
    }

    @Test
    void itShouldReturnCommentById(){
        //given

        User user = new User.Builder()
                .nickName("nick")
                .login("login")
                .password("dupa")
                .email("email@gmail.com")
                .roles("ROLE_USER")
                .isEnabled(false)
                .build();

        Post post = new Post.Builder()
                .content("content")
                .user(user)
                .build();

        Comment comment =new Comment.Builder()
                .content("comment content")
                .user(user)
                .post(post)
                .build();

        user.getPostList().add(post);
        user.addComment(comment);
        post.addComment(comment);

        given(commentRepository.findById(post.getId_post())).willReturn(Optional.of(comment));

        //when
        //then
        Optional<Comment> commentOptional=underTest.findById(comment.getId_comment());
        assertThat(commentOptional.isPresent()).isTrue();
        assertThat(commentOptional.get().getContent()).isEqualTo("comment content");
        assertThat(commentOptional.get().getUser()).isEqualTo(user);
        assertThat(commentOptional.get().getPost()).isEqualTo(post);
    }

    @Test
    void itShouldNotReturnCommentByIdBecauseThatCommentDoNotExist(){
        //given

        User user = new User.Builder()
                .nickName("nick")
                .login("login")
                .password("dupa")
                .email("email@gmail.com")
                .roles("ROLE_USER")
                .isEnabled(false)
                .build();

        Post post = new Post.Builder()
                .content("content")
                .user(user)
                .build();

        Comment comment =new Comment.Builder()
                .content("comment content")
                .user(user)
                .post(post)
                .build();

        user.getPostList().add(post);
        user.addComment(comment);
        post.addComment(comment);


        given(commentRepository.findById(post.getId_post())).willReturn(Optional.empty());

        //when
        //then
        assertThatThrownBy(() -> underTest.findById(comment.getId_comment()))
                .isExactlyInstanceOf(CommentNotFoundException.class)
                .hasMessageContaining("we cant find comment with that id");
    }

    @Test
    void itShouldDeleteOwnCommentById(){
        //given

        User user = new User.Builder()
                .nickName("nick")
                .login("login")
                .password("dupa")
                .email("email@gmail.com")
                .roles("ROLE_USER")
                .isEnabled(false)
                .build();

        Post post = new Post.Builder()
                .content("content")
                .user(user)
                .build();

        Comment comment =new Comment.Builder()
                .content("comment content")
                .user(user)
                .post(post)
                .build();

        user.getPostList().add(post);
        user.addComment(comment);
        post.addComment(comment);

        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(user);

        given(SecurityContextHolder.getContext().getAuthentication().getName()).willReturn(user.getNickName());

        //when
        when(userService.findByNickName(user.getNickName())).thenReturn(Optional.of(user));
        when(postService.findById(post.getId_post())).thenReturn(Optional.of(post));
        when(commentRepository.findById(comment.getId_comment())).thenReturn(Optional.of(comment));


        //then
        underTest.deleteOwnCommentById(comment.getId_comment());
        verify(commentRepository).deleteOwnCommentById(comment.getId_comment());
    }

    @Test
    void itShouldNotDeleteOwnCommentByIdBecauseUserDoNotExist(){
        //given

        User user = new User.Builder()
                .nickName("nick")
                .login("login")
                .password("dupa")
                .email("email@gmail.com")
                .roles("ROLE_USER")
                .isEnabled(false)
                .build();

        Post post = new Post.Builder()
                .content("content")
                .user(user)
                .build();

        Comment comment =new Comment.Builder()
                .content("comment content")
                .user(user)
                .post(post)
                .build();

        user.getPostList().add(post);
        user.addComment(comment);
        post.addComment(comment);

        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(user);

        given(SecurityContextHolder.getContext().getAuthentication().getName()).willReturn(user.getNickName());

        //when

        when(userService.findByNickName(user.getNickName())).thenReturn(Optional.empty());
        when(postService.findById(post.getId_post())).thenReturn(Optional.of(post));


        //then

        assertThatThrownBy(() -> underTest.deleteOwnCommentById(comment.getId_comment()))
                .isExactlyInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("we cant find user with that Id");
    }

    @Test
    void itShouldNotDeleteOwnCommentByIdBecauseThatPostDoNotExist(){
        //given

        User user = new User.Builder()
                .nickName("nick")
                .login("login")
                .password("dupa")
                .email("email@gmail.com")
                .roles("ROLE_USER")
                .isEnabled(false)
                .build();

        Post post = new Post.Builder()
                .content("content")
                .user(user)
                .build();

        Comment comment =new Comment.Builder()
                .content("comment content")
                .user(user)
                .post(post)
                .build();

        user.getPostList().add(post);
        user.addComment(comment);
        post.addComment(comment);

        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(user);

        given(SecurityContextHolder.getContext().getAuthentication().getName()).willReturn(user.getNickName());

        //when

        when(userService.findByNickName(user.getNickName())).thenReturn(Optional.of(user));
        when(postService.findById(post.getId_post())).thenReturn(Optional.empty());
        when(commentRepository.findById(comment.getId_comment())).thenReturn(Optional.of(comment));

        //then

        assertThatThrownBy(() -> underTest.deleteOwnCommentById(comment.getId_comment()))
                .isExactlyInstanceOf(PostNotFoundException.class)
                .hasMessageContaining("we cant find post with that Id");
    }


    @Test
    void itShouldNotDeleteOwnCommentByIdBecauseUserIsNotOwner(){
        //given

        User user = new User.Builder()
                .nickName("nick")
                .login("login")
                .password("dupa")
                .email("email@gmail.com")
                .roles("ROLE_USER")
                .isEnabled(false)
                .build();

        Post post = new Post.Builder()
                .content("content")
                .user(user)
                .build();

        Comment comment =new Comment.Builder()
                .content("comment content")
                .user(user)
                .post(post)
                .build();

        user.getPostList().add(post);
        post.addComment(comment);

        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(user);

        given(SecurityContextHolder.getContext().getAuthentication().getName()).willReturn(user.getNickName());

        //when

        when(userService.findByNickName(user.getNickName())).thenReturn(Optional.of(user));
        when(postService.findById(post.getId_post())).thenReturn(Optional.of(post));
        when(commentRepository.findById(comment.getId_comment())).thenReturn(Optional.of(comment));

        //then

        assertThatThrownBy(() -> underTest.deleteOwnCommentById(comment.getId_comment()))
                .isExactlyInstanceOf(UserIsNotOwnerException.class)
                .hasMessageContaining("You can not delete not your own comment");
    }
    @Test
    void itShouldDeleteCommentById(){
        //given

        User user = new User.Builder()
                .nickName("nick")
                .login("login")
                .password("dupa")
                .email("email@gmail.com")
                .roles("ROLE_USER")
                .isEnabled(false)
                .build();

        Post post = new Post.Builder()
                .content("content")
                .user(user)
                .build();

        Comment comment =new Comment.Builder()
                .content("comment content")
                .user(user)
                .post(post)
                .build();

        user.getPostList().add(post);
        user.addComment(comment);
        post.addComment(comment);

        //when

        when(commentRepository.findById(comment.getId_comment())).thenReturn(Optional.of(comment));
        underTest.deleteCommentById(comment.getId_comment());

        //then

        verify(commentRepository).deleteById(comment.getId_comment());
    }



    @Test
    void itShouldNotDeleteCommentByIdBecauseThatCommentDoNotExist(){
        //given

        User user = new User.Builder()
                .nickName("nick")
                .login("login")
                .password("dupa")
                .email("email@gmail.com")
                .roles("ROLE_USER")
                .isEnabled(false)
                .build();

        Post post = new Post.Builder()
                .content("content")
                .user(user)
                .build();

        Comment comment =new Comment.Builder()
                .content("comment content")
                .user(user)
                .post(post)
                .build();

        user.getPostList().add(post);
        user.addComment(comment);
        post.addComment(comment);


        //when

        when(commentRepository.findById(comment.getId_comment())).thenReturn(Optional.empty());
        //then

        assertThatThrownBy(() -> underTest.deleteCommentById(comment.getId_comment()))
                .isExactlyInstanceOf(CommentNotFoundException.class)
                .hasMessageContaining("we cant find comment with that id");

    }

    @Test
    void itShouldUpdateOwnCommentById(){
        //given

        User user = new User.Builder()
                .nickName("nick")
                .login("login")
                .password("dupa")
                .email("email@gmail.com")
                .roles("ROLE_USER")
                .isEnabled(false)
                .build();

        Post post = new Post.Builder()
                .content("content")
                .user(user)
                .build();

        Comment comment =new Comment.Builder()
                .content("comment content")
                .user(user)
                .post(post)
                .build();

        user.getPostList().add(post);
        user.addComment(comment);
        post.addComment(comment);

        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(user);

        given(SecurityContextHolder.getContext().getAuthentication().getName()).willReturn(user.getNickName());

        //when

        when(userService.findByNickName(user.getNickName())).thenReturn(Optional.of(user));
        when(commentRepository.findById(post.getId_post())).thenReturn(Optional.of(comment));
        underTest.updateComment("new content",post.getId_post());

        //then

        verify(commentRepository).updateComment("new content",post.getId_post());
    }

    @Test
    void itShouldNotUpdateOwnCommentByIdBecauseThatUserDoNotExistOrCommentDoNotExist(){
        //given

        User user = new User.Builder()
                .nickName("nick")
                .login("login")
                .password("dupa")
                .email("email@gmail.com")
                .roles("ROLE_USER")
                .isEnabled(false)
                .build();

        Post post = new Post.Builder()
                .content("content")
                .user(user)
                .build();

        Comment comment =new Comment.Builder()
                .content("comment content")
                .user(user)
                .post(post)
                .build();

        user.getPostList().add(post);
        user.addComment(comment);
        post.addComment(comment);

        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(user);

        given(SecurityContextHolder.getContext().getAuthentication().getName()).willReturn(user.getNickName());

        //when

        when(userService.findByNickName(user.getNickName())).thenReturn(Optional.empty());
        when(commentRepository.findById(comment.getId_comment())).thenReturn(Optional.of(comment));

        //then

        assertThatThrownBy(() -> underTest.updateComment("new content",comment.getId_comment()))
                .isExactlyInstanceOf(CommentNotFoundException.class)
                .hasMessageContaining("we cant find comment or user with that id");
    }


}