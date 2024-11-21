package com.yellowcat.backend.service;

import com.yellowcat.backend.model.Calichhen;
import com.yellowcat.backend.model.Lichhen;
import com.yellowcat.backend.repository.LichhenRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;


@Service
@EnableAsync
@EnableScheduling
public class LichHenService {

    private final LichhenRepository lichhenRepository;

    @Autowired
    CaLichHenService caLichHenService;

    @Autowired
    private EmailService emailService;

    public LichHenService(LichhenRepository lichhenRepository) {
        this.lichhenRepository = lichhenRepository;
    }

    public List<Lichhen> getAllLich(){
        return lichhenRepository.findAll();
    }
    public Page<Lichhen> getAllPageLichHen(Pageable pageable) {
        return lichhenRepository.findAll(pageable);
    }

    public Optional<Lichhen> getLichHenByDateandCa(LocalDate date,Integer idCa){
        return lichhenRepository.findByDateAndIdcalichhen_IdAndTrangthai(date,idCa,5);
    }

    public List<Lichhen> listLichHomNay(){
        return lichhenRepository.findByDate(LocalDate.now());
    }

    public Page<Lichhen> findByIdUser(Pageable pageable, String idUser){
        return lichhenRepository.findByIdkhachhang(idUser, pageable);
    }

    public Page<Lichhen> findByEmailNguoiDat(Pageable pageable, String userName){
        return lichhenRepository.findByEmailNguoiDat(userName, pageable);
    }

    // Kiểm tra ca đã có trong ngày chưa
    public boolean isCaTrungTrongNgay(LocalDate ngay, int idCaLichHen) {
        return lichhenRepository.existsByDateAndIdcalichhen_Id(ngay, idCaLichHen);
    }

    public Lichhen addOrUpdate(Lichhen lichhen){
        return lichhenRepository.save(lichhen);
    }

    public Lichhen findById(Integer id){
        return lichhenRepository.findById(id).orElse(null);
    }

    public Page<Lichhen> findAllLichWithTrangThai(Pageable pageable,boolean tt,LocalDate date){
        return lichhenRepository.findByTrangthaicaAndDateAfter(tt,date,pageable);
    }

    // Hàm tự động chạy khi ứng dụng khởi động
    @Scheduled(cron = "0 0 0 * * ?") // Chạy vào 0:00 mỗi ngày
    @Transactional
    public void taoLichHenRongMoiNgay() {
        taoLichHenRong();
        System.out.println("Đã tạo lịch hẹn rỗng mới cho 7 ngày tới vào lúc 0:00.");
    }

    @EventListener(ContextRefreshedEvent.class) // Chạy khi ứng dụng khởi động
    @Transactional
    public void taoLichHenRongKhiKhoiDong() {
        taoLichHenRong();
        System.out.println("Đã tạo lịch hẹn rỗng mới cho 7 ngày tới khi khởi động.");
    }

    private void taoLichHenRong() {
        // Lấy tất cả các ca từ bảng calichhen
        List<Calichhen> danhSachCa = caLichHenService.findAll();

        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(7); // Tạo cho 7 ngày tới

        while (!startDate.isAfter(endDate)) {
            for (Calichhen ca : danhSachCa) {
                // Kiểm tra xem lịch hẹn rỗng đã tồn tại chưa, tránh thêm trùng
                if (!lichhenRepository.existsByDateAndIdcalichhen_Id(startDate, ca.getId())) {
                    Lichhen lichHenRong = new Lichhen();
                    lichHenRong.setDate(startDate); // Chỉ thiết lập ngày
                    lichHenRong.setTrangthai(5); // Gán trạng thái là "Rỗng"
                    lichHenRong.setIdcalichhen(ca); // Gắn ID ca lịch hẹn để liên kết
                    lichHenRong.setEmailNguoiDat("default-email@example.com"); // Giá trị mặc định
                    lichHenRong.setIdkhachhang("demo");
                    lichHenRong.setTrangthaica(false);
                    lichHenRong.setSolanthaydoi(0);
                    lichHenRong.setSolannhacnho(0);
                    // Lưu vào bảng lichhen
                    lichhenRepository.save(lichHenRong);
                }
            }
            startDate = startDate.plusDays(1); // Chuyển sang ngày tiếp theo
        }
    }

    @Scheduled(cron = "0 59 23 * * ?") // Chạy vào 23:59 mỗi ngày
    @Transactional
    public void xoaLichHenRongCuoiNgay() {
        xoaLichHenRong();
        System.out.println("Đã xóa lịch hẹn rỗng vào cuối ngày.");
    }

    @EventListener(ContextRefreshedEvent.class) // Chạy khi ứng dụng khởi động
    @Transactional
    public void xoaLichHenRongKhiKhoiDong() {
        xoaLichHenRong();
        System.out.println("Đã xóa lịch hẹn rỗng khi khởi động.");
    }

    private void xoaLichHenRong() {
        LocalDate today = LocalDate.now();
        // Tìm tất cả các bản ghi có email là "default-email@example.com" và ngày nhỏ hơn ngày hôm nay
        List<Lichhen> danhSachLichHenRong = lichhenRepository.findByEmailNguoiDatAndDateBefore("default-email@example.com", today);

        // Xóa tất cả các bản ghi tìm được
        if (!danhSachLichHenRong.isEmpty()) {
            lichhenRepository.deleteAll(danhSachLichHenRong);
            System.out.println("Đã xóa " + danhSachLichHenRong.size() + " lịch hẹn rỗng.");
        } else {
            System.out.println("Không có lịch hẹn rỗng nào để xóa.");
        }
    }

    @Async
    public void sendEmailWithActions(Lichhen lichhen) {
        try {
            String Url = "http://localhost:3000/chi-tiet-lich/" + lichhen.getId();

            String message = "Chào bạn,\n\n"
                    + "Cảm ơn bạn đã đặt lịch."
                    + "Hẹn gặp bạn vào: " + lichhen.getIdcalichhen().getThoigianca()+ " Ngày: " +lichhen.getDate()
                    + " Bạn có thể sử dụng các liên kết dưới đây để quản lý lịch hẹn của mình:\n\n"
                    + "Chi tiết lịch hẹn: " + Url + "\n\n"
                    + "Trân trọng,\n"
                    + "Đội ngũ hỗ trợ";

            emailService.sendEmail(lichhen.getEmailNguoiDat(),"Hủy hoặc đổi thời gian lịch",message);
            System.out.println("Email đã được gửi thành công.");
        } catch (Exception e) {
            System.err.println("Gửi email thất bại: " + e.getMessage());
        }
    }

    private AtomicBoolean isCancelled = new AtomicBoolean(false);  // Biến flag để kiểm tra

    public void cancelScheduleChange() {
        isCancelled.set(true);  // Đặt flag hủy thành true
    }

    // Sau 20p tự động đổi trạng thái thành chờ thanh toán
    @PersistenceContext
    private EntityManager entityManager;

    @Async
    public void scheduleTrangThaiChange(Integer lichhenId) {
        try {
            if (isCancelled.get()) {
                System.out.println("Tiến trình bị hủy.");
                return;  // Nếu tiến trình bị hủy thì kết thúc
            }
            Thread.sleep( 2 * 60 * 1000); // Đợi
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return; // Ngừng xử lý nếu bị gián đoạn
        }

        // Xóa cache Hibernate để chắc chắn lấy dữ liệu từ DB
        entityManager.clear();

        Optional<Lichhen> lichhenOptional = lichhenRepository.findById(lichhenId);
        Lichhen lichhen = lichhenOptional.get();
        if (lichhen == null) {
            return;
        }

        if (lichhen.getTrangthai() == 4) {
            lichhen.setTrangthai(3); // Cập nhật sang trạng thái 3 (Chờ thanh toán)
            lichhenRepository.save(lichhen);
            System.out.println("Đã cập nhật trạng thái của lịch hẹn ID: " + lichhenId + " thành 3 (Chờ thanh toán)");
        } else {
            System.out.println("Lịch hẹn"+ lichhenId +  " đã bị hủy hoặc thay đổi trạng thái, không cập nhật nữa.");
        }
    }

    @Scheduled(fixedRate = 60000)  // Mỗi phút
    public void checkAppointments() {
        LocalDate currentDate = LocalDate.now();
        LocalTime currentTime = LocalTime.now();

        // Truy vấn các ca lịch hẹn trong cùng ngày và với giờ cụ thể
        List<Lichhen> lichhens = lichhenRepository.findByDate(currentDate);

        for (Lichhen lichhen : lichhens) {
            LocalTime appointmentTime = lichhen.getIdcalichhen().getThoigianca();

            // Tính toán sự chênh lệch giữa thời gian hiện tại và thời gian ca lịch hẹn
            Duration duration = Duration.between(currentTime, appointmentTime);

            //gửi thông báo
            if (duration.toMinutes() <= 60 && duration.toMinutes() > 0 && lichhen.getSolannhacnho()<1) {
                sendEmailNhacNho(lichhen,String.valueOf(duration.toMinutes()));
                lichhen.setSolannhacnho(lichhen.getSolannhacnho()+1);
                lichhenRepository.save(lichhen);
            }
        }
    }

    @Async
    public void sendEmailNhacNho(Lichhen lichhen,String gio) {
        try {
            String message = "Chào bạn,\n\n"
                    + "Cảm ơn bạn đã đặt lịch."
                    + "Còn" + gio + " phút nữa là đến lịch hẹn của bạn "
                    + "Xin hãy sắp xếp thời gian , lịch hẹn của bạn sẽ bắt đầu lúc: " + lichhen.getDate() + ' ' + lichhen.getIdcalichhen().getThoigianca();

            emailService.sendEmail(lichhen.getEmailNguoiDat(),"Nhắc nhở lịch hẹn",message);
            System.out.println("Email nhắc nhở đã được gửi thành công.");
        } catch (Exception e) {
            System.err.println("Gửi email thất bại: " + e.getMessage());
        }
    }
}
