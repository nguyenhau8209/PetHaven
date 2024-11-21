package com.yellowcat.backend.controller;

import com.yellowcat.backend.model.Calichhen;
import com.yellowcat.backend.service.CaLichHenService;
import com.yellowcat.backend.service.LichHenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping(("/api/ca-lich-hen"))
public class CaLichHenController {

    @Autowired
    private CaLichHenService caLichHenService;


//-----------------------------------------------------------
    @PostMapping("/add")
    public ResponseEntity<?> addOne(Calichhen calichhen){
        caLichHenService.addOrUpdate(calichhen);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateOne(Calichhen calichhen,@RequestParam Integer id){
        calichhen.setId(id);
        caLichHenService.addOrUpdate(calichhen);
        return ResponseEntity.ok().build();
    }
//-----------------------------------------------------------

    @PreAuthorize("hasAnyRole('admin', 'manager')")
    @PutMapping("/cap-nhap-ngay-nghi")
    public ResponseEntity<Void> falseAllCa(@RequestParam("ngay") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate ngay) {
        caLichHenService.UpdateNgayNghi(ngay);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAnyRole('admin', 'manager')")
    @PutMapping("/cap-nhap-trang-thai-ca")
    public ResponseEntity<Void> capNhapTrangThaiCa(@RequestParam("idCaLichHen") int idCaLichHen,
                                                   @RequestParam("ngay") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate ngay,
                                                   @RequestParam("trangThai") boolean trangThai
    ) {
        caLichHenService.DoiTrangthaiCaTrongNgay(idCaLichHen, ngay, trangThai);
        return ResponseEntity.ok().build();
    }



}
