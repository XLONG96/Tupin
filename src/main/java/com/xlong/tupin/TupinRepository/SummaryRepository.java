package com.xlong.tupin.TupinRepository;

import com.xlong.tupin.Entity.Summary;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface SummaryRepository extends ElasticsearchRepository<Summary, Long> {
}
