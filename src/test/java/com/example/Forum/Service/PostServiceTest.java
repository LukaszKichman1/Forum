package com.example.Forum.Service;

import com.example.Forum.Entity.Post;
import com.example.Forum.Entity.User;
import com.example.Forum.Exception.PostNotFoundException;
import com.example.Forum.Exception.UserCanNotBeCreateException;
import com.example.Forum.Exception.UserIsNotOwnerException;
import com.example.Forum.Exception.UserNotFoundException;
import com.example.Forum.Repository.PostRepository;
import com.example.Forum.Repository.TokenRepository;
import com.example.Forum.Repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;


class PostServiceTest {

    @Mock
    private UserService userService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PostRepository postRepository;
    @Captor
    private ArgumentCaptor<Post> argumentCaptor;
    private PostService underTest;

    @BeforeEach
    @Deprecated
    void setUp() {
        MockitoAnnotations.initMocks(this);
        underTest = new PostService(postRepository, userService);

    }

    @Test
    void itShouldSaveNewPost() {
        //given
        User user = new User.Builder()
                .nickName("nick")
                .login("login")
                .password("dupa")
                .email("email@gmail.com")
                .roles("ROLE_USER")
                .isEnabled(false)
                .build();

        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(user);

        Post post = new Post.Builder()
                .content("content")
                .user(user)
                .build();

        given(SecurityContextHolder.getContext().getAuthentication().getName()).willReturn(user.getNickName());
        given(userService.findByNickName("nick")).willReturn(Optional.of(user));

        //when
        underTest.save(post);

        //then
        then(postRepository).should().save(argumentCaptor.capture());
        Post argumentCaptorValue = argumentCaptor.getValue();
        assertThat(argumentCaptorValue.getContent()).isEqualTo("content");
        assertThat(argumentCaptorValue.getUser()).isEqualTo(user);
        assertThat(argumentCaptorValue.getCommentList()).isEmpty();
    }


    @Test
    void itShouldNotSaveNewPostBecauseUserDoNotExist() {
        //given
        User user = new User.Builder()
                .nickName("nick")
                .login("login")
                .password("dupa")
                .email("email@gmail.com")
                .roles("ROLE_USER")
                .isEnabled(false)
                .build();

        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(user);

        Post post = new Post.Builder()
                .content("content")
                .user(user)
                .build();

        //when
        when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn(user.getNickName());
        when(userService.findByNickName("nick")).thenReturn(Optional.empty());

        underTest.save(post);

        //then
        then(postRepository).shouldHaveNoInteractions();
    }

    @Test
    void itShouldReturnListOfAllPosts() {
        //given
        User user = new User.Builder()
                .nickName("nick")
                .login("login")
                .password("dupa")
                .email("email@gmail.com")
                .roles("ROLE_USER")
                .isEnabled(false)
                .build();


        Post post1 = new Post.Builder()
                .content("content")
                .user(user)
                .build();
        Post post2 = new Post.Builder()
                .content("content2")
                .user(user)
                .build();

        List<Post> postList = new ArrayList<>();
        postList.add(post1);
        postList.add(post2);

        //when
        when(postRepository.findAll()).thenReturn(postList);

        //then
        List<Post> postList1 = underTest.findAll();
        assertThat(postList1).isSameAs(postList);
    }

    @Test
    void itShouldReturnPostById() {
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

        given(postRepository.findById(post.getId_post())).willReturn(Optional.of(post));

        //when
        //then
        Optional<Post> postOptional = underTest.findById(post.getId_post());
        assertThat(postOptional.isPresent()).isTrue();
        assertThat(postOptional.get().getContent()).isEqualTo("content");
        assertThat(postOptional.get().getUser()).isEqualTo(user);
        assertThat(postOptional.get().getCommentList()).isEmpty();
    }

    @Test
    void itShouldNotReturnPostByIdBecauseThatPostDoNotExist() {
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


        given(postRepository.findById(post.getId_post())).willReturn(Optional.empty());

        //when
        //then
        assertThatThrownBy(() -> underTest.findById(post.getId_post()))
                .isExactlyInstanceOf(PostNotFoundException.class)
                .hasMessageContaining("We cant find post with that Id");
    }

    @Test
    void itShouldDeletePostOwnByUser() {
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

        user.getPostList().add(post);

        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(user);

        given(SecurityContextHolder.getContext().getAuthentication().getName()).willReturn(user.getNickName());

        //when
        when(userService.findByNickName(user.getNickName())).thenReturn(Optional.of(user));
        when(postRepository.findById(post.getId_post())).thenReturn(Optional.of(post));
        underTest.deleteOwnPostById(post.getId_post());

        //then
        assertFalse(user.getPostList().contains(post));
    }


    @Test
    void itShouldNotDeletePostOwnByUserBecauseThatUserIsNotOwner() {
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


        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(user);

        given(SecurityContextHolder.getContext().getAuthentication().getName()).willReturn(user.getNickName());

        //when
        when(userService.findByNickName(user.getNickName())).thenReturn(Optional.of(user));
        when(postRepository.findById(post.getId_post())).thenReturn(Optional.of(post));

        //then
        assertThatThrownBy(() -> underTest.deleteOwnPostById(post.getId_post()))
                .isExactlyInstanceOf(UserIsNotOwnerException.class)
                .hasMessageContaining("You can not delete not your own post");
    }

    @Test
    void itShouldNotDeletePostOwnByUserBecauseUserDoNotExist() {
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

        user.getPostList().add(post);

        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(user);

        given(SecurityContextHolder.getContext().getAuthentication().getName()).willReturn(user.getNickName());

        //when
        when(userService.findByNickName(user.getNickName())).thenReturn(Optional.empty());
        when(postRepository.findById(post.getId_post())).thenReturn(Optional.of(post));

        underTest.deleteOwnPostById(post.getId_post());

        //then
        then(postRepository).shouldHaveNoInteractions();
    }

    @Test
    void itShouldDeleteAllPostsOwnByUser() {
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

        Post post2 = new Post.Builder()
                .content("content2")
                .user(user)
                .build();

        user.getPostList().add(post);
        user.getPostList().add(post2);
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(user);

        given(SecurityContextHolder.getContext().getAuthentication().getName()).willReturn(user.getNickName());

        //when

        when(userService.findByNickName(user.getNickName())).thenReturn(Optional.of(user));
        when(postRepository.findById(post.getId_post())).thenReturn(Optional.of(post));

        //then
        underTest.deleteAllOwnPosts();
        assertTrue(user.getPostList().isEmpty());
    }

    @Test
    void itShouldNotDeleteAllPostsOwnByUserBecauseUserDoNotExist() {
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

        Post post2 = new Post.Builder()
                .content("content2")
                .user(user)
                .build();

        user.getPostList().add(post);
        user.getPostList().add(post2);
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(user);

        given(SecurityContextHolder.getContext().getAuthentication().getName()).willReturn(user.getNickName());

        //when

        when(userService.findByNickName(user.getNickName())).thenReturn(Optional.empty());
        when(postRepository.findById(post.getId_post())).thenReturn(Optional.of(post));

        underTest.deleteOwnPostById(post.getId_post());

        //then
        then(postRepository).shouldHaveNoInteractions();
    }

    @Test
    void itShouldDeletePostById() {
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

        user.getPostList().add(post);

        //when
        when(postRepository.findById(post.getId_post())).thenReturn(Optional.of(post));
        underTest.deletePostById(post.getId_post());

        //then
        verify(postRepository).deleteById(post.getId_post());
    }


    @Test
    void itShouldNotDeletePostByIdBecauseThatPostDoNotExist() {
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

        user.getPostList().add(post);

        //when
        when(postRepository.findById(post.getId_post())).thenReturn(Optional.empty());

        //then
        assertThatThrownBy(() -> underTest.deletePostById(post.getId_post()))
                .isExactlyInstanceOf(PostNotFoundException.class)
                .hasMessageContaining("We cant find post with that Id");
    }

    @Test
    void itShouldUpdateOwnPostById() {
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

        user.getPostList().add(post);

        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(user);

        given(SecurityContextHolder.getContext().getAuthentication().getName()).willReturn(user.getNickName());

        //when
        when(userService.findByNickName(user.getNickName())).thenReturn(Optional.of(user));
        when(postRepository.findById(post.getId_post())).thenReturn(Optional.of(post));
        underTest.updatePost("new content", post.getId_post());

        //then
        verify(postRepository).updateContent("new content", post.getId_post());
    }

    @Test
    void itShouldNotUpdateOwnPostByIdBecauseUserDoNotExist() {
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

        user.getPostList().add(post);

        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(user);

        given(SecurityContextHolder.getContext().getAuthentication().getName()).willReturn(user.getNickName());


        //when
        when(userService.findByNickName(user.getNickName())).thenReturn(Optional.empty());
        when(postRepository.findById(post.getId_post())).thenReturn(Optional.of(post));

        underTest.updatePost("new content", post.getId_post());

        //then
        verify(postRepository, times(1)).findById(post.getId_post());

    }


    @Test
    void itShouldNotUpdateOwnPostByIdBecauseThatPostDoNotExist() {
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

        user.getPostList().add(post);

        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(user);

        given(SecurityContextHolder.getContext().getAuthentication().getName()).willReturn(user.getNickName());


        //when
        when(userService.findByNickName(user.getNickName())).thenReturn(Optional.of(user));
        when(postRepository.findById(post.getId_post())).thenReturn(Optional.empty());

        //then
        assertThatThrownBy(() -> underTest.updatePost("new content", post.getId_post()))
                .isExactlyInstanceOf(PostNotFoundException.class)
                .hasMessageContaining("We cant find post with that Id");

    }

}