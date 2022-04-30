package com.example.Forum.Entity;

import com.example.Forum.Controller.CommentController;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int Id_post;
    private String content;

    @ManyToOne(
            fetch = FetchType.EAGER,
            cascade = CascadeType.ALL
    )
    @JoinColumn(name="user_id",referencedColumnName = "Id_user")
    @JsonIgnoreProperties({"postList","commentList"})
    private User user;

    @OneToMany(
            mappedBy = "post",
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER,
            orphanRemoval = true
    )
    @JsonIgnoreProperties({"postList", "user"})
    private List<Comment> commentList=new ArrayList<>();


    public Post() {
    }

    public int getId_post() {
        return Id_post;
    }

    public void setId_post(int id_post) {
        Id_post = id_post;
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

    public List<Comment> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<Comment> commentList) {
        this.commentList = commentList;
    }

    public void addComment(Comment comment)
    {
        this.commentList.add(comment);
    }



    public static class Builder {
        private String content;
        private User user;

        public Builder content(String content) {
            this.content = content;
            return this;
        }
        public Builder user(User user) {
            this.user=user;
            return this;
        }


        public Post build() {
            Post post = new Post();
            post.content = this.content;
            post.user=this.user;
            return post;
        }

    }


}