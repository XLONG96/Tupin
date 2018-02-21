package com.xlong.tupin.TupinRepository;

import com.xlong.tupin.Entity.TupinAlbum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TupinAlbumRepository extends PagingAndSortingRepository<TupinAlbum,Long>{

    Page<TupinAlbum> findAll(Pageable pageable);

    TupinAlbum saveAndFlush(TupinAlbum tupinAlbum);

    TupinAlbum findOne(Long id);

    void deleteTupinAlbumById(Long id);

    @Modifying
    @Query("update TupinAlbum t set t.likeNum=t.likeNum+1 where t.id= ?1")
    void increaseLikeNum(Long id);

    @Modifying
    @Query("update TupinAlbum t set t.likeNum=t.likeNum-1 where t.id= ?1")
    void decreaseLikeNum(Long id);
}
