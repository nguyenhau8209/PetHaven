package com.yellowcat.backend.service;

import com.yellowcat.backend.model.Calichhen;
import com.yellowcat.backend.repository.CalichhenRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;


@Service
public class CaLichHenService {
    @Autowired
    private CalichhenRepository caLichHenRepository;

    public void UpdateNgayNghi(LocalDate ngay){
        caLichHenRepository.updateNgayNghi(ngay);
    }

    public void addOrUpdate(Calichhen calichhen){
        caLichHenRepository.save(calichhen);
    }

    public Optional<Calichhen> findById (Integer id){
        return caLichHenRepository.findById(id)
    ;}

    public void DoiTrangthaiCaTrongNgay(int idCaLichHen, LocalDate ngay, boolean trangThai){
        caLichHenRepository.updateTrangThaiCaTrongNgay(idCaLichHen, ngay, trangThai);
    }

    public List<Calichhen> getAllByDate(LocalDate date){
        return caLichHenRepository.findAllCaAndStatusByDateaAndTrangthaiFalse(date);
    }

    public List<Calichhen> findAll(){
        return caLichHenRepository.findAll();
    }

    public boolean isCaAvailable(Integer caId,LocalDate date) {
        Optional<Calichhen> caOptional = caLichHenRepository.findById(caId);
        if (caOptional.isPresent()) {
            Calichhen ca = caOptional.get();

            // Lấy ngày hiện tại và thời gian hiện tại
            LocalDate today = LocalDate.now();
            LocalTime now = LocalTime.now();

            // Kiểm tra nếu ca thuộc ngày hôm nay và thời gian của ca là trước giờ hiện tại thì không cho đặt
            if (date.isEqual(today) && ca.getThoigianca().isBefore(now)) {
                return false;
            }

            // Nếu ca không thuộc ngày hôm nay hoặc thời gian của ca là sau giờ hiện tại thì cho phép đặt
            return true;
        }

        // Nếu ca không tồn tại thì trả về false
        return false;
    }
}
