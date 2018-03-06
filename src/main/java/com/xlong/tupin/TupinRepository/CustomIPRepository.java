package com.xlong.tupin.TupinRepository;

import com.xlong.tupin.Entity.CustomIP;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomIPRepository extends JpaRepository<CustomIP, Long> {
    CustomIP saveAndFlush(CustomIP customIP);

    CustomIP findByIpAddr(String ipAddr);
}
