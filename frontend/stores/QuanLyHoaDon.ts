import { defineStore } from 'pinia';
import HoaDonKhachHang from '~/models/CaLichHenKhachDat';
interface QuanLyHoaDonState {
    ListHoaDon: HoaDonKhachHang[];
}

export const useQuanLyHoaDonStore = defineStore('quanLyHoaDonStore', {
    state: (): QuanLyHoaDonState => ({
        ListHoaDon: [],
    }),
    actions: {
        async fetchListHoaDon() {
            const token = localStorage.getItem('access_token');
            try {
                const response = await fetch(`http://localhost:8080/api/hoa-don/all`, {
                    method: 'GET',
                    headers: {
                        'Authorization': `Bearer ${token}`,
                    },
                });
                const data = await response.json();
                this.ListHoaDon = data;
                console.log(data);
            } catch (error) {
                console.error('Error fetching list hoa don:', error);
            }
        },
        async inHoaDon(id: String) {
            const token = localStorage.getItem('access_token');
            try {
                const response = await fetch(`http://localhost:8080/api/hoa-don/in-hoa-don?id=${id}`, {
                    method: 'GET',
                    headers: {
                        'Authorization': `Bearer ${token}`,
                    },
                });

                if (!response.ok) {
                    throw new Error(`HTTP error! status: ${response.status}`);
                }

                // Đọc file PDF dưới dạng Blob
                const blob = await response.blob();

                // Tạo URL tạm cho file Blob
                const url = window.URL.createObjectURL(blob);

                // Tạo thẻ <a> để tải file
                const a = document.createElement('a');
                a.href = url;
                a.download = `hoa-don-${id}.pdf`; // Tên file tải xuống
                document.body.appendChild(a);
                a.click(); // Kích hoạt hành động tải file

                // Xóa thẻ <a> sau khi hoàn tất
                a.remove();
                window.URL.revokeObjectURL(url); // Giải phóng bộ nhớ

            } catch (error) {
                console.error('Error fetching file hoa don:', error);
            }
        }
    }
});
