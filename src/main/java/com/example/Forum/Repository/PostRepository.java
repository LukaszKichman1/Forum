package com.example.Forum.Repository;

import com.example.Forum.Entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {

    @Modifying
    @Query(nativeQuery = true, value = "UPDATE posts SET content=?1 where id_post = ?2")
    void updateContent(String content, int id);
}
