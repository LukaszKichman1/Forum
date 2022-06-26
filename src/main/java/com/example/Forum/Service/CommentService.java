package com.example.Forum.Service;

import com.example.Forum.Entity.Comment;
import com.example.Forum.Entity.Post;
import com.example.Forum.Entity.User;
import com.example.Forum.Exception.CommentNotFoundException;
import com.example.Forum.Exception.PostNotFoundException;
import com.example.Forum.Exception.UserIsNotOwnerException;
import com.example.Forum.Exception.UserNotFoundException;
import com.example.Forum.Repository.CommentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.persistence.Table;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class CommentService {

    private CommentRepository commentRepository;
    private PostService postService;
    private UserService userService;
    Logger logger = LoggerFactory.getLogger(CommentService.class);

    @Autowired
    public CommentService(CommentRepository commentRepository, PostService postService, UserService userService) {
        this.commentRepository = commentRepository;
        this.postService = postService;
        this.userService = userService;
    }

    @Transactional
    public Comment save(int id, String content) {
        String loggeduser = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> userOptional = userService.findByNickName(loggeduser);
        Optional<Post> optionalPost = postService.findById(id);
        if (userOptional.isPresent() && optionalPost.isPresent()) {
            Comment commentBuilder = new Comment.Builder()
                    .content(content)
                    .user(userOptional.get())
                    .post(optionalPost.get())
                    .build();
            userOptional.get().addComment(commentBuilder);
            optionalPost.get().addComment(commentBuilder);
            logger.trace("User  " + userOptional.get().getNickName() + "create comment with id  " + commentBuilder.getId_comment());
            return commentRepository.save(commentBuilder);
        }
        return null;
    }


    public Optional<Comment> findById(int id) {
        Optional<Comment> optionalComment = commentRepository.findById(id);
        if (optionalComment.isPresent()) {
            return optionalComment;
        } else {
            throw new CommentNotFoundException("We cant find comment with that id");
        }
    }

    @Transactional
    public void deleteOwnCommentById(int id) {
        String loggeduser = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> userOptional = userService.findByNickName(loggeduser);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            List<Comment> commentList = user.getCommentList();

            Optional<Comment> optionalComment = findById(id);
            if (optionalComment.isPresent() && commentList.contains(optionalComment.get())) {
                logger.trace("User" + userOptional.get().getNickName() + "delete his own comment with id  " + optionalComment.get().getId_comment());
                commentRepository.deleteOwnCommentById(id);
            } else {
                throw new UserIsNotOwnerException("You can not delete not your own comment");
            }

        }
    }

    @Transactional
    public void deleteCommentById(int id) {
        Optional<Comment> optionalComment = findById(id);
        if (optionalComment.isPresent()) {
            commentRepository.deleteById(id);
        }
    }

    @Transactional
    public void updateComment(String content, int id) {
        String loggeduser = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> userOptional = userService.findByNickName(loggeduser);
        Optional<Comment> commentOptional = findById(id);
        if (userOptional.isPresent() && commentOptional.isPresent()) {
            List<Comment> commentList = userOptional.get().getCommentList();
            if (commentList.contains(commentOptional.get())) {
                logger.trace("User" + userOptional.get().getNickName() + "update his own comment with id  " + commentOptional.get().getId_comment());
                commentRepository.updateComment(content, id);
            }
        } else {
            throw new CommentNotFoundException("We cant find comment or user with that id");
        }
    }

}


