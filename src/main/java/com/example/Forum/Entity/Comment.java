package com.example.Forum.Entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.security.core.parameters.P;

import javax.persistence.*;

@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int Id_comment;
    private String content;

    @ManyToOne(
            fetch = FetchType.EAGER,
            cascade = CascadeType.ALL

    )
    @JoinColumn(name="user_id",referencedColumnName = "Id_user")
    @JsonIgnoreProperties({"postList","commentSet"})
    private User user;

    @ManyToOne(
            fetch = FetchType.EAGER,
            cascade = CascadeType.ALL
    )
    @JoinColumn(name="post_id",referencedColumnName = "Id_post")
    @JsonIgnoreProperties({"user","commentList"})
    private Post post;


    public Comment() {
    }

    public int getId_comment() {
        return Id_comment;
    }

    public void setId_comment(int id_comment) {
        Id_comment = id_comment;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public static class Builder {
        private String content;
        private User user;
        private Post post;

        public Builder content(String content) {
            this.content = content;
            return this;
        }
        public Builder user(User user)
        {
            this.user=user;
            return this;
        }

        public Builder post(Post post)
        {
            this.post=post;
            return this;
        }

        public Comment build() {
            Comment comment = new Comment();
            comment.content = this.content;
            comment.user=this.user;
            comment.post=this.post;
            return comment;
        }
    }
}