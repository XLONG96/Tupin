package com.xlong.tupin.TupinRepository;

import com.xlong.tupin.Entity.Tupin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TupinRepository extends PagingAndSortingRepository<Tupin,Long>{

    Page<Tupin> findAll(Pageable pageable);

    Tupin saveAndFlush(Tupin tupin);
}
