package com.xlong.tupin.TupinRepository;

import com.xlong.tupin.Entity.TupinAlbum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TupinAlbumRepository extends PagingAndSortingRepository<TupinAlbum,Long>{

    Page<TupinAlbum> findAll(Pageable pageable);

    TupinAlbum saveAndFlush(TupinAlbum tupinAlbum);

    void deleteById(Long id);
}
