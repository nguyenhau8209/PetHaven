package com.yellowcat.backend.service;

import com.yellowcat.backend.model.Dichvu;
import com.yellowcat.backend.model.Giamgia;
import com.yellowcat.backend.repository.GiamgiaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class GiamGiaService {
    @Autowired
    GiamgiaRepository giamgiaRepository;

    public GiamGiaService(GiamgiaRepository giamgiaRepository) {
        this.giamgiaRepository = giamgiaRepository;
    }



    public Page<Giamgia> getGiamgia(Pageable pageable) {
        return giamgiaRepository.findAll(pageable);
    }

    public Giamgia addOrUpdate(Giamgia giamgia){
        return giamgiaRepository.save(giamgia);
    }

    public String removeGiamgia(Integer id){
        giamgiaRepository.deleteById(id);
        return "ok";
    }

    public Optional<Giamgia> findGiamGiaTheoNgayHienTai(){
        return giamgiaRepository.findByNgaybatdauLessThanEqualAndNgayketthucGreaterThanEqualAndTrangthai(LocalDate.now(),LocalDate.now(),true);
    }

    public Optional<Giamgia> findById(Integer id){
        return giamgiaRepository.findById(id);
    }

}
