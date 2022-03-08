package com.example.Forum.Entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int Id_post;
    private String content;

    @ManyToOne()
    @JoinColumn(
            name = "user_id",
            referencedColumnName = "Id_user"
    )
    private User user;

    @OneToMany(
            mappedBy = "post",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Comment> commentList = new ArrayList<>();

    public Post() {
    }

    public int getId_post() {
        return Id_post;
    }

    public String getContent() {
        return content;
    }

    public User getUser() {
        return user;
    }

    public List<Comment> getCommentList() {
        return commentList;
    }

    public static class Builder {
        private String content;
        private User user;


        public Builder content(String content) {
            this.content = content;
            return this;
        }

        public Builder user(User user) {
            this.user = user;
            return this;
        }


        public Post Build() {
            Post post = new Post();
            post.content = this.content;
            post.user = this.user;
            return post;
        }

    }


}