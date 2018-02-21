package com.xlong.tupin.TupinRepository;

import com.xlong.tupin.Entity.Tupin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TupinRepository extends PagingAndSortingRepository<Tupin,Long>{

    Page<Tupin> findAll(Pageable pageable);

    Tupin saveAndFlush(Tupin tupin);

    Tupin findOne(Long id);

    void deleteTupinById(Long id);

    @Modifying
    @Query("update Tupin t set t.likeNum=t.likeNum+1 where t.id= ?1")
    void increaseLikeNum(Long id);

    @Modifying
    @Query("update Tupin t set t.likeNum=t.likeNum-1 where t.id= ?1")
    void decreaseLikeNum(Long id);
}
