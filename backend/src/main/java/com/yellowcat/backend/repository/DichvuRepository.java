package com.yellowcat.backend.repository;


import com.yellowcat.backend.model.Dichvu;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface DichvuRepository extends JpaRepository<Dichvu, Long> {

    Page<Dichvu> findByTendichvuContainingIgnoreCase(String tendichvu, Pageable pageable);

    List<Dichvu> findByTrangthaiTrue();
}