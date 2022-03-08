package com.example.Forum.Entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name="users")
public class User {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int Id_user;

    @Column(unique = true)
    private String nickName;

    @Column(unique = true)
    private String login;

    private String password;

    private String email;

    private String roles;

    private boolean isEnabled;

    @OneToMany(
            mappedBy = "user",
            fetch = FetchType.EAGER,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Post> postList = new ArrayList<>();

    @OneToMany(
            mappedBy = "user",
            fetch = FetchType.EAGER,
            cascade = CascadeType.ALL
    )
    private Set<Comment> commentSet=new HashSet<>();

    @OneToOne
    @JoinColumn(name = "id_token", referencedColumnName = "Id_token")
    private Token token;

    public User() {
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    public void setPostList(List<Post> postList) {
        this.postList = postList;
    }

    public void setCommentSet(Set<Comment> commentSet) {
        this.commentSet = commentSet;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    public int getId_user() {
        return Id_user;
    }

    public String getNickName() {
        return nickName;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getRoles() {
        return roles;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public List<Post> getPostList() {
        return postList;
    }

    public Set<Comment> getCommentSet() {
        return commentSet;
    }

    public Token getToken() {
        return token;
    }

    public static class Builder {
        private String nickName;
        private String login;
        private String password;
        private String email;
        private String roles;
        private boolean isEnabled;
        private List<Post> postList;
        private Set<Comment> commentSet;
        private Token token;


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