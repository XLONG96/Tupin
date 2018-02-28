package com.xlong.tupin.TupinRepository;

import com.xlong.tupin.Entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CommentRepository extends PagingAndSortingRepository<Comment, Long> {
    Page<Comment> findAllByBlogId(Pageable pageable, Long blogId);

    Comment saveAndFlush(Comment comment);

    void deleteCommentById(Long id);
}
