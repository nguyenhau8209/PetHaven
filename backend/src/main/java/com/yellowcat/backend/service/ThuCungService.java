package com.yellowcat.backend.service;

import com.yellowcat.backend.model.Thucung;
import com.yellowcat.backend.repository.ThucungRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ThuCungService {
    @Autowired
    private ThucungRepository thucungRepository;

    public Thucung saveOrUpdate(Thucung thucung) {
        return thucungRepository.save(thucung);
    }

    public List<Thucung> findListThuCungByidChu(String idTK) {return thucungRepository.findByIdtaikhoan(idTK);}
}
