package com.example.Forum.Service;

import com.example.Forum.Entity.Token;
import com.example.Forum.Entity.User;
import com.example.Forum.Exception.UserCanNotBeActivationException;
import com.example.Forum.Exception.UserCanNotBeCreateException;
import com.example.Forum.Exception.UserNotFoundException;
import com.example.Forum.Repository.TokenRepository;
import com.example.Forum.Repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import static net.bytebuddy.matcher.ElementMatchers.is;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.hamcrest.beans.SamePropertyValuesAs.samePropertyValuesAs;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UserServiceTest {


    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private TokenRepository tokenRepository;
    @Mock
    private MailService mailService;
    @Captor
    private ArgumentCaptor<User> argumentCaptor;
    private UserService underTest;



    @BeforeEach
    @Deprecated
    void setUp() {
        MockitoAnnotations.initMocks(this);
        underTest=new UserService(userRepository,passwordEncoder,tokenRepository,mailService);
    }

    //zapisuje uzytkownika
    @Test
    @Deprecated
    void itShouldSaveNewUser(){
        //given
        User user = new User.Builder()
                .nickName("nick")
                .login("login")
                .password("dupa")
                .email("email@gmail.com")
                .roles("ROLE_USER")
                .isEnabled(false)
                .build();


        given(userRepository.findByNickName("nick")).willReturn(Optional.empty());

        //when
        underTest.save(user);

        //then
        then(userRepository).should().save(argumentCaptor.capture());
        User argumentCaptorValue = argumentCaptor.getValue();
        assertThat(argumentCaptorValue.getNickName()).isEqualTo("nick");
        assertThat(argumentCaptorValue.getLogin()).isEqualTo("login");
        assertThat(argumentCaptorValue.getEmail()).isEqualTo("email@gmail.com");
        assertThat(argumentCaptorValue.getRoles()).isEqualTo("ROLE_USER");
        assertThat(argumentCaptorValue.isEnabled()).isFalse();
    }

    //nie zapisuje uzytkownika  bo taki uzytkownik juz istnieje
    @Test
    void itShouldNotSaveNewUserBecauseThatUserAlreadyExist(){
        //given
        User user = new User.Builder()
                .nickName("nick")
                .login("login")
                .password("dupa")
                .email("email@gmail.com")
                .roles("ROLE_USER")
                .isEnabled(false)
                .build();


        given(userRepository.findByNickName("nick")).willReturn(Optional.of(user));
        //when
        //then
        assertThatThrownBy(() -> underTest.save(user))
                .isExactlyInstanceOf(UserCanNotBeCreateException.class)
                .hasMessageContaining("user have empty fields or user with that nickname already exist");
    }

    //nie zapisuje uzytkownika bo puste pola
    @Test
    void itShouldNotSaveNewUserBecauseThatUserHaveSomeEmptyFields()
    {
        //given
        User user = new User.Builder()
                .nickName("nick")
                .email("email@gmail.com")
                .roles("ROLE_USER")
                .isEnabled(false)
                .build();


        given(userRepository.findByNickName("nick")).willReturn(Optional.empty());
        //when
        //then
        assertThatThrownBy(() -> underTest.save(user))
                .isExactlyInstanceOf(UserCanNotBeCreateException.class)
                .hasMessageContaining("user have empty fields or user with that nickname already exist");
    }

    //znajduje uzytkownika
    @Test
    @Deprecated
    void itShouldReturnUserByLogin(){
        //given
        User user = new User.Builder()
                .nickName("nick")
                .login("login")
                .password("dupa")
                .email("email@gmail.com")
                .roles("ROLE_USER")
                .isEnabled(false)
                .build();

        given(userRepository.findByLogin(user.getLogin())).willReturn(Optional.of(user));
        //when
        //then
        Optional<User> userOptional = underTest.findByLogin(user.getLogin());
        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(c -> {
                    assertThat(c).isEqualToComparingFieldByField(user);
                });
    }

    //aktywuje uzytkownika
    @Test
    void itShouldActivateUser(){
        //given
        User user = new User.Builder()
                .nickName("nick")
                .login("login")
                .password("dupa")
                .email("email@gmail.com")
                .roles("ROLE_USER")
                .isEnabled(false)
                .build();

        Token token = new Token(20);

        user.setToken(token);
        tokenRepository.save(token);

        given(userRepository.findByLogin(user.getLogin())).willReturn(Optional.of(user));

        //when
        underTest.activationUser(user.getLogin(),user.getToken().getValue());

        //then
        assertThat(user.isEnabled()).isTrue();
    }

    //nie aktywuje uzytkownika bo taki uzytkownik nie istnieje
    @Test
    void itShouldNotActivateUserBecauseThatUserDoNotExist(){
        //given
        User user = new User.Builder()
                .nickName("nick")
                .login("login")
                .password("dupa")
                .email("email@gmail.com")
                .roles("ROLE_USER")
                .isEnabled(false)
                .build();

        Token token = new Token(20);

        user.setToken(token);
        tokenRepository.save(token);

        given(userRepository.findByLogin(user.getLogin())).willReturn(Optional.empty());
        //when
        //then
        assertThatThrownBy(() -> underTest.activationUser(user.getLogin(),user.getToken().getValue()))
                .isExactlyInstanceOf(UserCanNotBeActivationException.class)
                .hasMessageContaining("we cant find user with this id or invalid token value");
    }

    //nie zwraca uzytkownka po id bo nie ma takiego uzytkownika
    @Test
    void itShouldeNotReturnUserByIdBecauseThatUserDoNotExist(){
        //given
        User user = new User.Builder()
                .nickName("nick")
                .login("login")
                .password("dupa")
                .email("email@gmail.com")
                .roles("ROLE_USER")
                .isEnabled(false)
                .build();

        given(userRepository.findById(user.getId_user())).willReturn(Optional.empty());

        //when
        //then
        assertThatThrownBy(() -> underTest.findById(user.getId_user()))
                .isExactlyInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("we cant find user with that Id");

    }

    //zwraca uzytkownia po id
    @Test
    void itShouldeReturnUserById(){
        //given
        User user = new User.Builder()
                .nickName("nick")
                .login("login")
                .password("dupa")
                .email("email@gmail.com")
                .roles("ROLE_USER")
                .isEnabled(false)
                .build();

        given(userRepository.findById(user.getId_user())).willReturn(Optional.of(user));

        //when
        //then
        Optional<User> userOptional=underTest.findById(user.getId_user());
        assertThat(userOptional.isPresent()).isTrue();
        assertThat(userOptional.get().getNickName()).isEqualTo("nick");
        assertThat(userOptional.get().getLogin()).isEqualTo("login");
        assertThat(userOptional.get().getEmail()).isEqualTo("email@gmail.com");

    }

    //zwraca uzytkownika po nicku
    @Test
    void itShouldeReturnUserByNickname(){
        //given
        User user = new User.Builder()
                .nickName("nick")
                .login("login")
                .password("dupa")
                .email("email@gmail.com")
                .roles("ROLE_USER")
                .isEnabled(false)
                .build();

        given(userRepository.findByNickName(user.getNickName())).willReturn(Optional.of(user));

        //when
        //then
        Optional<User> userOptional=underTest.findByNickName(user.getNickName());
        assertThat(userOptional.isPresent()).isTrue();
        assertThat(userOptional.get().getNickName()).isEqualTo("nick");
        assertThat(userOptional.get().getLogin()).isEqualTo("login");
        assertThat(userOptional.get().getEmail()).isEqualTo("email@gmail.com");
    }

    //nie zwraca uzytkownika po nicku bo taki uzytkonik nie istenije
    @Test
    void itShouldeNotReturnUserByNicknameBecauseThatUserDoNotExist(){
        //given
        User user = new User.Builder()
                .nickName("nick")
                .login("login")
                .password("dupa")
                .email("email@gmail.com")
                .roles("ROLE_USER")
                .isEnabled(false)
                .build();

        given(userRepository.findByNickName(user.getNickName())).willReturn(Optional.empty());

        //when
        //then
        assertThatThrownBy(() -> underTest.findByNickName(user.getNickName()))
                .isExactlyInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("we cant find user with that nickName");
    }

    @Test
    void itShouldeDeleteUserById(){
        //given
        User user = new User.Builder()
                .nickName("nick")
                .login("login")
                .password("dupa")
                .email("email@gmail.com")
                .roles("ROLE_USER")
                .isEnabled(false)
                .build();

        //when
        when(userRepository.findById(user.getId_user())).thenReturn(Optional.of(user));
        underTest.deleteById(user.getId_user());

        //then
        verify(userRepository).deleteById(user.getId_user());
    }

    @Test
    void itShouldeNotDeleteUserByIdBecauseThatUserDoNotExist(){
        //given
        User user = new User.Builder()
                .nickName("nick")
                .login("login")
                .password("dupa")
                .email("email@gmail.com")
                .roles("ROLE_USER")
                .isEnabled(false)
                .build();

        given(userRepository.findById(user.getId_user())).willReturn(Optional.empty());

        //when
        //then
        assertThatThrownBy(() -> underTest.deleteById(user.getId_user()))
                .isExactlyInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("we cant find user with that Id");
    }


    @Test
    void itShouldeReturnListOfAllUser(){
        //given
        User user1 = new User.Builder()
                .nickName("nick")
                .login("login")
                .password("dupa")
                .email("email@gmail.com")
                .roles("ROLE_USER")
                .isEnabled(false)
                .build();

        User user2 = new User.Builder()
                .nickName("nick2")
                .login("login2")
                .password("dupa2")
                .email("email2@gmail.com")
                .roles("ROLE_USER")
                .isEnabled(false)
                .build();

        List<User> userList = new ArrayList<>();
        userList.add(user1);
        userList.add(user2);

        //when
        when(userRepository.findAll()).thenReturn(userList);

        //then
        List<User> userList1 = underTest.findAll();
        assertThat(userList1).isSameAs(userList);
    }
}