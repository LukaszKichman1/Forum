package com.example.Forum.Entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.security.core.parameters.P;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int Id_user;

    private String nickName;

    private String login;

    private String password;

    private String email;

    private String roles;

    private boolean isEnabled;

    @OneToMany(
            mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JsonIgnoreProperties({"user", "commentList"})
    private List<Post> postList = new ArrayList<>();

    @OneToMany(
            mappedBy = "user",
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER,
            orphanRemoval = true
    )
    @JsonIgnoreProperties("user")
    private List<Comment> commentList = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "id_token", referencedColumnName = "Id_token")
    private Token token;

    public User() {
        super();
    }

    public String getNickName() {
        return nickName;
    }

    public int getId_user() {
        return Id_user;
    }

    public void addPost(Post post) {
        this.postList.add(post);
    }

    public void setId_user(int id_user) {
        Id_user = id_user;
    }

    public void addComment(Comment comment) {
        this.commentList.add(comment);
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    public List<Post> getPostList() {
        return postList;
    }

    public void setPostList(List<Post> postList) {
        this.postList = postList;
    }

    public List<Comment> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<Comment> commentList) {
        this.commentList = commentList;
    }

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    public static class Builder {
        private String nickName;
        private String login;
        private String password;
        private String email;
        private String roles;
        private boolean isEnabled;


        public Builder nickName(String nickName) {
            this.nickName = nickName;
            return this;
        }

        public Builder login(String login) {
            this.login = login;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder roles(String roles) {
            this.roles = roles;
            return this;
        }

        public Builder isEnabled(boolean isEnabled) {
            this.isEnabled = isEnabled;
            return this;
        }


        public User build() {
            User user = new User();
            user.nickName = this.nickName;
            user.login = this.login;
            user.password = this.password;
            user.email = this.email;
            user.roles = this.roles;
            user.isEnabled = this.isEnabled;
            return user;
        }

    }


}