package com.yellowcat.backend.controller;

import com.yellowcat.backend.DTO.DatLichDTO;
import com.yellowcat.backend.DTO.DoiLichDTO;
import com.yellowcat.backend.model.Hoadon;
import com.yellowcat.backend.model.Lichhen;
import com.yellowcat.backend.service.HoaDonService;
import com.yellowcat.backend.service.LichHenService;
import com.yellowcat.backend.service.PdfExportService;
import jakarta.validation.Valid;
import org.hibernate.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/lich-hen")
public class LichHenController {
    @Autowired
    public LichHenService lichHenService;

    @Autowired
    private HoaDonService hoaDonService;

    @Autowired
    private PdfExportService pdfExportService;

    @PreAuthorize("hasRole('admin')")
    @GetMapping("/all")
    public Page<Lichhen> getAllLichHen(@RequestParam(defaultValue = "0") int page) {
        Pageable pageable = PageRequest.of(page, 10); // 10 items per page
        return lichHenService.getAllPageLichHen(pageable);
    }

    @PreAuthorize("hasRole('user')")
    @GetMapping("/findByIdUser")
    public Page<Lichhen> findByIdUser(@RequestParam(defaultValue = "0") int page) {
        Pageable pageable = PageRequest.of(page, 10); // 10 items per page

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String idUser = authentication.getName(); // Đây là idUser

        return lichHenService.findByIdUser(pageable,idUser);
    }

    @PreAuthorize("hasAnyRole('admin', 'manager')")
    @GetMapping("/findByUserEmail")
    public Page<Lichhen> findByUserEmail(@RequestParam(defaultValue = "0") int page,@RequestParam("email") String Email) {
        Pageable pageable = PageRequest.of(page, 10);

        return lichHenService.findByEmailNguoiDat(pageable, Email);
    }

    @PreAuthorize("hasAnyRole('admin', 'manager')")
    @GetMapping("/getListDoiTrangThai")
    public Page<Lichhen> listDoi(@RequestParam(defaultValue = "0") int page){
        Pageable pageable = PageRequest.of(page, 10);
        Page<Lichhen> ListDoi = lichHenService.findAllLichWithTrangThai(pageable,true, LocalDate.now());
        return ListDoi;
    }

    @PreAuthorize("hasAnyRole('admin', 'manager')")
    @PutMapping("/updateTrangThai/{id}/{idTT}")
    public ResponseEntity<Lichhen> updateMore(@PathVariable int id, @PathVariable int idTT) {

        // Tìm lịch hẹn
        Lichhen datLaiLich = lichHenService.findById(id);
        if (datLaiLich == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Trả về 404 nếu không tìm thấy lịch hẹn
        }

        if (lichHenService.isCaTrungTrongNgay(datLaiLich.getDate(), datLaiLich.getIdcalichhen().getId())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // Trả về lỗi nếu trùng ca
        }

        // Cập nhật trạng thái
        datLaiLich.setTrangthai(idTT);
        Lichhen updateLich = lichHenService.addOrUpdate(datLaiLich);

        return new ResponseEntity<>(updateLich, HttpStatus.OK); // Trả về 200 OK
    }

    @PreAuthorize("hasAnyRole('admin', 'manager')")
    @PutMapping("/update-time/{id}")
    public ResponseEntity<?> doiTimeQuyenAdmin(@PathVariable Integer id,@Valid @RequestBody DoiLichDTO doiLichDTO) {
        Lichhen lichhen = lichHenService.findById(id);
        Lichhen lichhenNew = new Lichhen();
        Optional<Hoadon> hoadonOptional = hoaDonService.finHoadonByIdLich(id);
        if (lichhen == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Lịch hẹn không tồn tại.");
        }

        if (lichhen != null && lichhen.getTrangthai() == 4) {
//          Thay đổi thời gian và ca lịch
            Optional<Lichhen> lichhenDoiOptional = lichHenService.getLichHenByDateandCa(LocalDate.parse(doiLichDTO.getDate()),Integer.parseInt(doiLichDTO.getIdcalichhen()));
            if (!lichhenDoiOptional.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Lịch lỗi.");
            }

            Lichhen lichDoi = lichhenDoiOptional.get();
            lichDoi.setEmailNguoiDat(lichhen.getEmailNguoiDat());
            lichDoi.setIdkhachhang(lichhen.getIdkhachhang());
            lichDoi.setTrangthai(3); // Đặt trạng thái là "Chờ thanh toán"
            lichDoi.setThoigianthaydoi(LocalDateTime.now());
            lichDoi.setThucung(lichhen.getThucung());
            lichDoi.setDichvu(lichhen.getDichvu());
            lichDoi.setTrangthaica(true);
            lichDoi.setSolanthaydoi(lichhen.getSolanthaydoi());
            lichHenService.addOrUpdate(lichDoi);

            Hoadon hoadon = hoadonOptional.get();
            hoadon.setIdlichhen(lichDoi);
            hoaDonService.addOrUpdate(hoadon);

//            Cập nhập số lần thay đổi
            lichhen.setSolanthaydoi(lichhen.getSolanthaydoi()+1);

//            Tạo bản ghi lưu trừ lịch đổi
            lichhenNew.setEmailNguoiDat(lichhen.getEmailNguoiDat());
            lichhenNew.setIdkhachhang(lichhen.getIdkhachhang());
            lichhenNew.setTrangthai(1); // Đặt trạng thái là "Thất bại"
            lichhenNew.setIdcalichhen(lichhen.getIdcalichhen());
            lichhenNew.setThoigianthaydoi(LocalDateTime.now());
            lichhenNew.setThucung(lichhen.getThucung());
            lichhenNew.setDichvu(lichhen.getDichvu());
            lichhenNew.setDate(lichhen.getDate());
            lichhenNew.setTrangthaica(true);
            lichhenNew.setSolanthaydoi(lichhen.getSolanthaydoi());
            lichHenService.addOrUpdate(lichhenNew);

            lichhen.setTrangthai(5);
            lichhen.setEmailNguoiDat("default-email@example.com");
            if (lichhen.getTrangthaica()){
                lichhen.setTrangthaica(false);
            }else {
                return ResponseEntity.ok("Lỗi ca");
            }
            lichHenService.addOrUpdate(lichhen);
            return ResponseEntity.ok("Thời gian của lịch hẹn đã được cập nhật.");
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/findById/{id}")
    public ResponseEntity<?> findById(@PathVariable Integer id) {
        Lichhen lichhen = lichHenService.findById(id);
        return ResponseEntity.ok(lichhen);
    }

    @PreAuthorize("hasAnyRole('admin', 'manager')")
    @GetMapping("/lich-hom-nay")
    public ResponseEntity<?> lichHomNay() {
        List<Lichhen> listHomNay = lichHenService.listLichHomNay();
        if (listHomNay.isEmpty()) {
            return  ResponseEntity.status(HttpStatus.OK).body("Nay được nghỉ à");
        }
        return ResponseEntity.ok(listHomNay);
    }

    @PreAuthorize("hasAnyRole('admin', 'manager')")
    @GetMapping("/thanh-toan/{id}")
    public ResponseEntity<?> ThanhToanHoaDon(@PathVariable Integer id){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) authentication.getPrincipal();
        String Email = jwt.getClaimAsString("email");

        Lichhen lichhen = lichHenService.findById(id);
        Optional<Hoadon> hoadonOptional = hoaDonService.finHoadonByIdLich(id);
        if (hoadonOptional.isPresent()) {
            Hoadon hoadon = hoadonOptional.get();

            if (lichhen == null) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            lichhen.setTrangthai(0);
            lichHenService.addOrUpdate(lichhen);

            hoadon.setNgaythanhtoan(LocalDateTime.now());
            hoadon.setTrangthai(2);
            hoadon.setNguoithanhtoan(Email);
            hoaDonService.addOrUpdate(hoadon);

            // Tạo file PDF hóa đơn
            String thoiGian = hoadon.getIdlichhen().getDate().toString()+ ' ' + hoadon.getIdlichhen().getIdcalichhen().getThoigianca();
            byte[] pdfBytes = pdfExportService.generateInvoice(hoadon.getNgaythanhtoan().toString(),hoadon.getMagiaodich(),hoadon.getPhuongthucthanhtoan(),hoadon.getIdlichhen().getDichvu().getTendichvu(),hoadon.getSotien(),thoiGian);

            hoaDonService.sendHoaDonSauThanhToan(lichhen,pdfBytes);

            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

}
