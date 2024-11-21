package com.yellowcat.backend;

import com.yellowcat.backend.model.Calichhen;
import com.yellowcat.backend.repository.CalichhenRepository;
import com.yellowcat.backend.repository.LichhenRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Transactional
public class LichHenRepositoryTest {

    @Autowired
    private LichhenRepository lichHenRepository;

    @Autowired
    private CalichhenRepository calichhenRepository;


    @Test
    public void testFindAllCaAndStatusByDate() {
        // Giả sử bạn đã có dữ liệu trong cơ sở dữ liệu
        LocalDate testDate = LocalDate.of(2024, 11, 2);

        List<Calichhen> results = calichhenRepository.findAllCaAndStatusByDate(testDate);

        // Kiểm tra kết quả
        assertNotNull(results);
        assertFalse(results.isEmpty(), "Kết quả không được rỗng");

        // Bạn có thể thêm kiểm tra cho nội dung cụ thể
        for (Calichhen caLichHen : results) {
            // Ví dụ kiểm tra từng thuộc tính
            assertNotNull(caLichHen.getId(), "ID không được null");
            // Thêm các kiểm tra khác cho các thuộc tính của caLichHen nếu cần
        }
        System.out.println("Danh sách các ca và trạng thái cho ngày " + ":");
        for (Calichhen ca : results) {
            System.out.println(ca.getTenca().toString());  // Bạn có thể điều chỉnh phương thức toString() để in ra thông tin cần thiết
            System.out.println(ca.getThoigianca().toString());
            System.out.println(ca.getTrangthai().toString());
        }
    }


}
