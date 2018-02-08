package com.xlong.tupin.TupinRepository;

import com.xlong.tupin.Entity.Blog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlogRepository extends PagingAndSortingRepository<Blog,Long>{

    Page<Blog> findAll(Pageable pageable);

    Blog saveAndFlush(Blog blog);

    Blog findOne(Long id);
}
