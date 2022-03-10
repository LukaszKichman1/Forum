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

    @OneToMany(
            mappedBy = "post",
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER,
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


    public List<Comment> getCommentList() {
        return commentList;
    }

    public static class Builder {
        private String content;

        public Builder content(String content) {
            this.content = content;
            return this;
        }

        public Post build() {
            Post post = new Post();
            post.content = this.content;
            return post;
        }

    }


}