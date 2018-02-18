package com.xlong.tupin.TupinRepository;

import com.xlong.tupin.Entity.Admin;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends PagingAndSortingRepository<Admin, Long>{

    Admin findAdminByName(String name);
}
