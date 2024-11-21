package com.yellowcat.backend.controller;

import com.yellowcat.backend.DTO.DatLichDTO;
import com.yellowcat.backend.DTO.DoiLichDTO;
import com.yellowcat.backend.model.*;
import com.yellowcat.backend.service.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

@RestController
@RequestMapping("/api/dat-lich")
public class DatLichController {
    @Autowired
    public LichHenService lichHenService;

    @Autowired
    private CaLichHenService caLichHenService;

    @Autowired
    private DichVuService dichVuService;

    @Autowired
    private ThuCungService thuCungService;

    @Autowired
    private HoaDonService hoaDonService;

    @GetMapping("/dat-lich-info")
    public ResponseEntity<Map<String, Object>> getDatLichInfo(@RequestParam("ngay") LocalDate ngay) {
        Map<String, Object> response = new HashMap<>();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String idUser = auth.getName();

        // Lấy danh sách dịch vụ
        List<Dichvu> danhSachDichVu = dichVuService.getListTrangThaiTrue();
        response.put("dichVu", danhSachDichVu);

        // Lấy các ca có thể đặt
        List<Calichhen> CaLichHen = caLichHenService.getAllByDate(ngay);
        response.put("CaLichHen", CaLichHen);

        List<Thucung> listThuCung = thuCungService.findListThuCungByidChu(idUser);
        response.put("ListThuCung", listThuCung);

        return ResponseEntity.ok(response);
    }

    // API tạo lịch hẹn khi khách hàng ấn nút xác nhận
    @PutMapping("/xac-nhan-dat")
    public ResponseEntity<Lichhen> createLichhen(
           @Valid @RequestBody DatLichDTO datLichDTO ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) authentication.getPrincipal();

        String idUser = authentication.getName(); // Đây là idUser
        String email = jwt.getClaimAsString("email");
        Optional<Lichhen> lichhenOptional = lichHenService.getLichHenByDateandCa(LocalDate.parse(datLichDTO.getDate()),datLichDTO.getIdcalichhen());

        Thucung thucung = datLichDTO.getIdThuCung();
        thucung.setIdtaikhoan(idUser);
        thuCungService.saveOrUpdate(thucung);

        Optional<Dichvu> dichvuOptional = dichVuService.findById(datLichDTO.getIdDichVu());
        Dichvu dichvu = dichvuOptional.get();

        if (!lichhenOptional.isPresent()) {
            System.out.println("lịch không tồn tại");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
//        Check xem có lịch đã được đặt chưa
        if (lichhenOptional.get().getTrangthai()!=5){
            System.out.println("Lịch đã được đặt trước rồi , vui lòng chọn thời gian khác");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
//        Check không cho đặt ca trong quá khứ
        if (!caLichHenService.isCaAvailable(datLichDTO.getIdcalichhen(),LocalDate.parse(datLichDTO.getDate()))){
            System.out.println("Không được đặt ca trong quá khứ");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }


        Lichhen lichhen = lichhenOptional.get();
        System.out.println(lichhen);
        lichhen.setIdkhachhang(idUser);
        lichhen.setEmailNguoiDat(email);
        lichhen.setTrangthai(4);
        lichhen.setThucung(thucung);
        lichhen.setDate(LocalDate.parse(datLichDTO.getDate()));
        lichhen.setDichvu(dichvu);
        if(!lichhen.getTrangthaica()){
            lichhen.setTrangthaica(true);
        }else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }



        Hoadon hoadon = new Hoadon();
        hoadon.setDate(LocalDateTime.now());
        hoadon.setIdlichhen(lichhen);
        hoadon.setPhuongthucthanhtoan("Offline");
        hoadon.setTrangthai(1);
        Double SoTien = hoaDonService.TinhGiaTien(datLichDTO.getIdDichVu(),hoadon);
        hoadon.setSotien(SoTien);
        hoadon.setMagiaodich(hoaDonService.MaGiaoDichRandom());
        hoaDonService.addOrUpdate(hoadon);

        Lichhen createLich = lichHenService.addOrUpdate(lichhen);

        lichHenService.sendEmailWithActions(createLich);

        lichHenService.scheduleTrangThaiChange(createLich.getId());

        return new ResponseEntity<>(createLich, HttpStatus.CREATED);
    }

    @Transactional
    @PutMapping("/huy-lich/{id}")
    public ResponseEntity<?> huyLichHen(@PathVariable Integer id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String idUser = authentication.getName();
        Optional<Hoadon> hoadonOptional = hoaDonService.finHoadonByIdLich(id);
        Lichhen lichhen = lichHenService.findById(id);
        if (lichhen == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Lịch hẹn không tồn tại.");
        }

        // Check xem có phải chủ lịch không
        if (!lichhen.getIdkhachhang().equalsIgnoreCase(idUser)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Bạn không phải là chủ lịch hẹn này.");
        }

        if (lichhen.getTrangthai() == 4 || lichhen.getTrangthai() == 6 ) {
            // Tạo bản ghi lưu lại lịch sử đặt với trạng thái hủy
            lichhen.setSolanthaydoi(lichhen.getSolanthaydoi()+1);

            Lichhen lichhenNew = new Lichhen();
            lichhenNew.setSolanthaydoi(lichhen.getSolanthaydoi());
            lichhenNew.setEmailNguoiDat(lichhen.getEmailNguoiDat());
            lichhenNew.setIdkhachhang(lichhen.getIdkhachhang());
            lichhenNew.setTrangthai(2); // Đặt trạng thái là "Đã hủy"
            lichhenNew.setIdcalichhen(lichhen.getIdcalichhen());
            lichhenNew.setThoigianhuy(LocalDateTime.now());
            lichhenNew.setThucung(lichhen.getThucung());
            lichhenNew.setDichvu(lichhen.getDichvu());
            lichhenNew.setDate(lichhen.getDate());
            lichhenNew.setTrangthaica(true);
            lichHenService.addOrUpdate(lichhenNew);

//           Hủy hóa đơn chờ
            Hoadon hoadonNew = hoadonOptional.get();
            hoaDonService.deleteHoadonById(hoadonNew.getId());

            // Cập nhật lịch gốc với trạng thái đã hủy
            lichhen.setIdkhachhang("demo");
            lichhen.setTrangthai(5);
            lichhen.setEmailNguoiDat("default-email@example.com");
            if (lichhen.getTrangthaica()) {
                lichhen.setTrangthaica(false);
            } else {
                return ResponseEntity.ok("Lỗi ca");
            }
            lichHenService.addOrUpdate(lichhen);
            lichHenService.cancelScheduleChange();
            return ResponseEntity.ok("Lịch hẹn đã được hủy thành công.");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Lịch hẹn không thể hủy vì trạng thái không hợp lệ.");
    }

    @Transactional
    @PutMapping("/thay-doi-thoi-gian/{id}")
    public ResponseEntity<?> thayDoiThoiGian(@PathVariable Integer id,@Valid @RequestBody DoiLichDTO doiLichDTO) {
        Lichhen lichhen = lichHenService.findById(id);
        Lichhen lichhenNew = new Lichhen();
        Optional<Hoadon> hoadonOptional = hoaDonService.finHoadonByIdLich(id);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String idUser = authentication.getName();

        if (lichhen == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Lịch hẹn không tồn tại.");
        }

        if (!hoadonOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Hóa đơn ko hợp lệ");
        }

        // Check xem có phải chủ lịch không
        if (!lichhen.getIdkhachhang().equalsIgnoreCase(idUser)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Bạn không phải là chủ lịch hẹn này.");
        }

        if (lichhen != null && lichhen.getTrangthai() == 4 || lichhen.getTrangthai() == 6) {

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

//            Cập nhập lại hóa đơn chờ
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
            lichHenService.cancelScheduleChange();
            lichHenService.addOrUpdate(lichhen);
            return new ResponseEntity<>(lichDoi, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


}
