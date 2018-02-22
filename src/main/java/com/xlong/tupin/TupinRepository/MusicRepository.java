package com.xlong.tupin.TupinRepository;

import com.xlong.tupin.Entity.Music;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MusicRepository extends JpaRepository<Music, Long>{

    List<Music> findAll();

    Music saveAndFlush(Music music);
}
