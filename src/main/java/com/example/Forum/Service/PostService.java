package com.example.Forum.Service;

import com.example.Forum.Entity.Post;
import com.example.Forum.Entity.User;
import com.example.Forum.Exception.PostNotFoundException;
import com.example.Forum.Exception.UserIsNotOwnerException;
import com.example.Forum.Exception.UserNotFoundException;
import com.example.Forum.Repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    private PostRepository postRepository;
    private UserService userService;

    @Autowired
    public PostService(PostRepository postRepository, UserService userService) {
        this.postRepository = postRepository;
        this.userService = userService;
    }

    @Transactional
    public Post save(Post post) {
        String loggeduser = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> userOptional = userService.findByNickName(loggeduser);
        if (userOptional.isPresent()) {
            Post postBuilder = new Post.Builder()
                    .content(post.getContent())
                    .user(userOptional.get())
                    .build();

            userOptional.get().addPost(postBuilder);

            return postRepository.save(postBuilder);
        } else {
            throw new UserNotFoundException("we cant find user with that Id");
        }
    }

    public List<Post> findAll() {
        return postRepository.findAll();
    }

    public Optional<Post> findById(int id) {
        Optional<Post> optionalPost = postRepository.findById(id);
        if (optionalPost.isPresent()) {
            return optionalPost;
        } else {
            throw new PostNotFoundException("we cant find post with that Id");
        }
    }

    @Transactional
    public void deleteOwnPostById(int id) {
        String loggeduser = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> userOptional = userService.findByNickName(loggeduser);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            List<Post> postList = user.getPostList();

            Optional<Post> optionalPost = findById(id);
            if (optionalPost.isPresent() && postList.contains(optionalPost.get())) {
                postList.remove(optionalPost.get());
            } else {
                throw new UserIsNotOwnerException("You can not delete not your own post");
            }

        }
    }

    @Transactional
    public void deleteAllOwnPosts() {
        String loggeduser = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> userOptional = userService.findByNickName(loggeduser);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            List<Post> postList = user.getPostList();
            postList.clear();
        }
    }

    @Transactional
    public void deletePostById(int id) {
        Optional<Post> optionalPost = findById(id);
        if (optionalPost.isPresent()) {
            postRepository.deleteById(id);
        } else {
            throw new PostNotFoundException("we can not find post with that id");
        }
    }

    @Transactional
    public void updatePost(String content,int id)
    {
        String loggeduser = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> userOptional = userService.findByNickName(loggeduser);
        Optional<Post> postOptional=findById(id);
        if (userOptional.isPresent() && postOptional.isPresent())
        {
            List<Post> postList=userOptional.get().getPostList();
            if(postList.contains(postOptional.get()))
            {
               postRepository.updateContent(content,id);
            }
        }else {
            throw new PostNotFoundException("we can not find post with that id");
        }


    }

}



