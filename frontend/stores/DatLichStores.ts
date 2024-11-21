import { defineStore } from 'pinia';
import DichVu from '../models/DichVu';
import CaHen from '../models/CaHen';
import ThuCungKhachHang from "~/models/ThuCungKhachHang";
import { useMauKhachDatDichVu } from '~/stores/MauKhachDatDichVu';

interface DatLichState {
    DichVu: DichVu[];
    CaLichHen: CaHen[];
    ListThuCung : ThuCungKhachHang[];
    date: Date;
}

export const useDatLichStore = defineStore('datLichStore', {
    state: (): DatLichState => ({
        DichVu: [],
        CaLichHen: [],
        ListThuCung : [],
        date: new Date()
    }),
    actions: {
        async fetchCaHen(date: Date = new Date()) {
            const token = localStorage.getItem('access_token');
            this.date = date;
            try {
                const response = await fetch(`http://localhost:8080/api/dat-lich/dat-lich-info?ngay=${date}`, {
                    method: 'GET',
                    headers: {
                        'Authorization': `Bearer ${token}`,
                    },
                });
                const data = await response.json();
                this.DichVu = data.dichVu;
                this.CaLichHen = data.CaLichHen;
                this.ListThuCung = data.ListThuCung;
            } catch (error) {
                console.error('Error fetching services:', error);
            }
        },
        async updateDatLichInfo(date: Date) {
            const token = localStorage.getItem('access_token');
            this.date = date;
            try {
                const response = await fetch(`http://localhost:8080/api/dat-lich/dat-lich-info?ngay=${date}`, {
                    method: 'GET',
                    headers: {
                        'Authorization': `Bearer ${token}`,
                    },
                });
                const data = await response.json();
                console.log('Updated data:', data);
                this.DichVu = data.dichVu;
                this.CaLichHen = data.CaLichHen;
                this.ListThuCung = data.ListThuCung;
            } catch (error) {
                console.error('Error updating Dat Lich info:', error);
            }
        },
        async xacNhanDatLich() {
            const { getTempData,updateDataAfterBooking  } = useMauKhachDatDichVu();
            const token = localStorage.getItem('access_token');
            const dichVuVaThuCungKhachHangDat = getTempData();


            if (!dichVuVaThuCungKhachHangDat || !dichVuVaThuCungKhachHangDat.idlichhen) {
                console.error('Không có dữ liệu đặt lịch tạm thời hoặc dữ liệu không hợp lệ');
                return;
            }
        
            try {
                const formatDate = (date: string | number | Date) => {
                    if (date instanceof Date) {
                        return date.toISOString().split('T')[0];
                    }
                    const d = new Date(date);
                    if (isNaN(d.getTime())) {
                        throw new Error('Ngày không hợp lệ');
                    }
                    return d.toISOString().split('T')[0];
                };
        
                // Log dữ liệu trước khi gửi để kiểm tra
                console.log('Dữ liệu trước khi gửi:', dichVuVaThuCungKhachHangDat);
        
                // Kiểm tra và xử lý ngày
                let formattedDate;
                try {
                    formattedDate = formatDate(dichVuVaThuCungKhachHangDat.idlichhen.date);
                } catch (error) {
                    console.error('Lỗi khi định dạng ngày:', error);
                    throw new Error('Ngày đặt lịch không hợp lệ');
                }
        
                const response = await fetch('http://localhost:8080/api/dat-lich/xac-nhan-dat', {
                    method: 'PUT',
                    headers: {
                        'Authorization': `Bearer ${token}`,
                        'Content-Type': 'application/json',
                    },
                    body: JSON.stringify({
                        idThuCung: dichVuVaThuCungKhachHangDat.thucung,
                        date: formattedDate,
                        idcalichhen: dichVuVaThuCungKhachHangDat.idlichhen.calichhen.id,
                        idDichVu: dichVuVaThuCungKhachHangDat.idlichhen.dichvu.id
                    })
                });
        
                if (!response.ok) {
                    const errorText = await response.text();
                    console.error('Response Error:', errorText);
                    console.error('Date:', formattedDate);
                    throw new Error(`Lỗi khi xác nhận đặt lịch: ${response.status} ${response.statusText}. ${errorText}`);
                }
        
                const result = await response.json();
                updateDataAfterBooking(result);
                console.log('Đặt lịch thành công:', result);
            } catch (error: unknown) {
                if (error instanceof Error) {
                    console.error('Lỗi khi xác nhận đặt lịch:', error.message);
                } else {
                    console.error('Lỗi không xác định khi xác nhận đặt lịch');
                }
                throw error;
            }
        }
    }
});