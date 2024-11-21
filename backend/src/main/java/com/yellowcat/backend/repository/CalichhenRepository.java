package com.yellowcat.backend.repository;

import com.yellowcat.backend.model.Calichhen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CalichhenRepository extends JpaRepository<Calichhen, Integer> {
    @Modifying
    @Query("UPDATE Calichhen c SET c.trangthai = :trangThai")
    void updateTrangThaiAll(@Param("trangThai") boolean trangThai);

    @Modifying
    @Query("UPDATE Calichhen c SET c.trangthai = TRUE WHERE c.id IN " +
            "(SELECT lh.idcalichhen.id FROM Lichhen lh WHERE lh.date = :ngay AND lh.trangthai = 0)")
    void updateTrangThaiCaDatThanhCong(@Param("ngay") LocalDate ngay);

    @Modifying
    @Query("UPDATE Calichhen c SET c.trangthai = true WHERE c.id IN " +
            "(SELECT lh.idcalichhen.id FROM Lichhen lh WHERE lh.date = :ngay)")
    void updateNgayNghi(@Param("ngay") LocalDate ngay);

    @Modifying
    @Query("UPDATE Calichhen c SET c.trangthai = :trangThai WHERE c.id = :idCaLichHen AND EXISTS " +
            "(SELECT lh FROM Lichhen lh WHERE lh.idcalichhen.id = :idCaLichHen AND lh.date = :ngay)")
    void updateTrangThaiCaTrongNgay(@Param("idCaLichHen") int idCaLichHen,
                                    @Param("ngay") LocalDate ngay,
                                    @Param("trangThai") boolean trangThai);

    // Truy vấn để lấy tất cả các trạng thái của các ca trong một ngày
    @Query("SELECT c FROM Calichhen c JOIN Lichhen l ON c.id = l.idcalichhen.id " +
            "WHERE l.date = :ngay AND l.trangthaica = false " +
            "AND (l.date <> CURRENT_DATE OR c.thoigianca >= CURRENT_TIME)")
    List<Calichhen> findAllCaAndStatusByDateaAndTrangthaiFalse(@Param("ngay") LocalDate ngay);


//    Đổi trang thai ca khi dat lich thanh cong
//    @Modifying
//    @Query("UPDATE Calichhen c SET c.trangthai = TRUE WHERE c.id = :caId AND EXISTS (SELECT lh FROM Lichhen lh WHERE lh.idcalichhen.id = c.id AND lh.date = :ngay AND lh.trangthai = 0)")
//    void updateTrangThaiCaDatThanhCongSauThanhToan(@Param("caId") int caId, @Param("ngay") LocalDate ngay);

}