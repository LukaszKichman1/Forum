package com.example.Forum.Repository;

import com.example.Forum.Entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment,Integer> {

    @Modifying
    @Query(nativeQuery= true,value="DELETE FROM comments WHERE id_comment= ?1")
    void deleteOwnCommentById(int id);

    @Modifying
    @Query(nativeQuery= true,value="UPDATE comments SET content=?1 WHERE id_comment = ?2")
    void updateComment(String content,int id);

}
