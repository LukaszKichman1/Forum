package com.example.Forum.Service;

import com.example.Forum.Entity.Token;
import com.example.Forum.Entity.User;
import com.example.Forum.Exception.UserCanNotBeActivationException;
import com.example.Forum.Exception.UserCanNotBeCreateException;
import com.example.Forum.Exception.UserNotFoundException;
import com.example.Forum.Repository.TokenRepository;
import com.example.Forum.Repository.UserRepository;
import org.hibernate.internal.build.AllowPrintStacktrace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private TokenRepository tokenRepository;
    private MailService mailService;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, TokenRepository tokenRepository, MailService mailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenRepository = tokenRepository;
        this.mailService = mailService;
    }

    public User save(User user) {

        if (user.getLogin() == null
                || user.getEmail() == null
                || user.getPassword() == null
                || user.getNickName() == null) {
            throw new UserCanNotBeCreateException("user have empty fields");
        } else {

            System.out.println(user.getLogin() + user.getNickName());
            User userBuilder = new User.Builder()
                    .nickName(user.getNickName())
                    .login(user.getLogin())
                    .password(passwordEncoder.encode(user.getPassword()))
                    .email(user.getEmail())
                    .roles("ROLE_USER")
                    .isEnabled(false)
                    .build();
            Token token = new Token(1 + (int) (Math.random() * 200));

            userBuilder.setToken(token);
            tokenRepository.save(token);
            mailService.SenderMail(user.getEmail(), "link : http://localhost:8080/user/activation   Your token=" + token.getValue(), "activation of your account");
            return userRepository.save(userBuilder);
        }
    }

    public Optional<User>findByLogin(String login) {
        return userRepository.findByLogin(login);

    }

    @Transactional
    public void activationUser(String login, int valueOfToken) {

        Optional<User> optionalUser = userRepository.findByLogin(login);

        if (optionalUser.isPresent() && optionalUser
                .get().
                getToken().
                getValue() == valueOfToken) {
            optionalUser.get().setEnabled(true);
        } else {
            throw new UserCanNotBeActivationException("we cant find user with this id or invalid token value");
        }
    }

    public Optional<User> findById(int id)
    {
        Optional<User> userOptional=userRepository.findById(id);
        if(userOptional.isPresent())
        {
            return userOptional;
        }
        else
        {
            throw new UserNotFoundException("we cant find user with that Id");
        }
    }

    public Optional<User> findByNickName(String nickName)
    {
        Optional<User> userOptional=userRepository.findByNickName(nickName);
        if(userOptional.isPresent())
        {
            return userOptional;
        }
        else
        {
            throw new UserNotFoundException("we cant find user with that nickName");
        }
    }


    @Transactional
    public void deleteById(int id)
    {
        Optional<User> userOptional=userRepository.findById(id);
        if(userOptional.isPresent())
        {
            User user =userOptional.get();
            userRepository.deleteById(user.getId_user());
        }
        else
        {
            throw new UserNotFoundException("we cant find user with that Id");
        }
    }
}


