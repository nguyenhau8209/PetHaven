package com.yellowcat.backend.repository;

import com.yellowcat.backend.model.Hoadon;
import com.yellowcat.backend.model.Lichhen;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface HoadonRepository extends JpaRepository<Hoadon, Integer> {

    List<Hoadon> findByTrangthai(Integer trangthai);

    List<Hoadon> findByNguoithanhtoanAndPhuongthucthanhtoan(String email,String phuongThuc);

    Optional<Hoadon> findByIdlichhen_IdAndTrangthai(Integer idlichhen,int trangthai);

    Optional<Hoadon> findByMagiaodich(String idPayMent);
}