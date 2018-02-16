package com.xlong.tupin.TupinRepository;

import com.xlong.tupin.Entity.Blog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlogRepository extends PagingAndSortingRepository<Blog,Long>{

    Page<Blog> findAll(Pageable pageable);

    Blog saveAndFlush(Blog blog);

    Blog findOne(Long id);

    void deleteById(Long id);

    @Modifying
    @Query("update Blog b set b.visitNum=b.visitNum+1 where b.id= ?1")
    int increaseVisitNum(Long id);
}
