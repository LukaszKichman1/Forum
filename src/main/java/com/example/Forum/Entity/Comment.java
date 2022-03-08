package com.example.Forum.Entity;

import javax.persistence.*;

@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int Id_comment;
    private String content;

    @ManyToOne
    @JoinColumn(
            name = "user_id",
            referencedColumnName = "Id_user"
    )
    private User user;

    @ManyToOne
    @JoinColumn(
            name = "post_id",
            referencedColumnName = "Id_post"
    )
    private Post post;

    public Comment() {
    }

    public int getId_comment() {
        return Id_comment;
    }

    public String getContent() {
        return content;
    }

    public User getUser() {
        return user;
    }

    public Post getPost() {
        return post;
    }

    public static class Builder {
        private int Id_comment;
        private String content;
        private User user;
        private Post post;

        public Builder Id_comment(int Id_comment) {
            this.Id_comment = Id_comment;
            return this;
        }

        public Builder content(String content) {
            this.content = content;
            return this;
        }

        public Builder user(User user) {
            this.user = user;
            return this;
        }

        public Builder post(Post post) {
            this.post = post;
            return this;
        }

        public Comment build() {
            Comment comment = new Comment();
            comment.content = this.content;
            comment.user = this.user;
            comment.post = this.post;
            return comment;

        }
    }
}