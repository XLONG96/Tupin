package com.xlong.tupin.TupinRepository;

import com.xlong.tupin.Entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByBlogId(Long blogId);

    Comment saveAndFlush(Comment comment);

    void deleteById(Long id);
}
