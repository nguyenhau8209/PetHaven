import {defineStore} from 'pinia';
import DichVu from '../models/DichVu';
import CaHen from '../models/CaHen';
import ThuCungKhachHang from "~/models/ThuCungKhachHang";
import {useMauKhachDatDichVu} from '~/stores/MauKhachDatDichVu';

interface LichHenDetails {
    id: number;
    idkhachhang: string;
    thucung: ThuCungKhachHang;
    dichvu: DichVu;
    emailNguoiDat: string;
    date: string;
    trangthai: number;
    idcalichhen: CaHen;
    trangthaica: boolean;
    thoigianhuy: string | null;
    thoigianthaydoi: string | null;
    solanthaydoi: number;
}

interface ThayDoiLichHenState {
    lichHenDetails: LichHenDetails | null;
}

export const useThayDoiLichHenStore = defineStore('thayDoiLichHenStore', {
    state: (): ThayDoiLichHenState => ({
        lichHenDetails: null,
    }),
    actions: {
        async fetchLichHenDetails(id: number) {
            const {updateDataAfterBooking} = useMauKhachDatDichVu();
            const token = localStorage.getItem('access_token');
            try {
                const response = await fetch(`http://localhost:8080/api/lich-hen/findById/${id}`, {
                    method: 'GET',
                    headers: {
                        'Authorization': `Bearer ${token}`,
                    },
                });
                const data = await response.json();
                this.lichHenDetails = data;
                updateDataAfterBooking(data);
            } catch (error) {
                console.error('Error fetching lich hen details:', error);
            }
        },
        async thayDoiLichHenStore(id: string, date: string, idCalichHen: string) {
            const token = localStorage.getItem('access_token');
            if (!token) {
                console.error('No access token found');
                throw new Error('Authentication required');
            }

            try {
                const response = await fetch(`http://localhost:8080/api/dat-lich/thay-doi-thoi-gian/${id}`, {
                    method: 'PUT',
                    headers: {
                        'Authorization': `Bearer ${token}`,
                        'Content-Type': 'application/json',
                    },
                    body: JSON.stringify({
                        date: date,
                        idcalichhen: idCalichHen
                    })
                });

                const contentType = response.headers.get("content-type");
                if (contentType && contentType.indexOf("application/json") !== -1) {
                    const data = await response.json();
                    if (!response.ok) {
                        console.error('Error response:', data);
                        throw new Error(`Failed to change appointment. Status: ${response.status}` + `id=` +id  +`date=:`+ date +`idcalichhen=`+ idCalichHen);
                    }
                    console.log('Appointment changed successfully:', data);
                    return data;
                } else {
                    const text = await response.text();
                    if (!response.ok) {
                        console.error('Error response:', text);
                        throw new Error(`Failed to change appointment. Status: ${response.status}` + `id=` +id  +`date=:`+ date +`idcalichhen=`+ idCalichHen);
                    }
                    console.log('Appointment changed successfully:', text);
                    return text;
                }
            } catch (error) {
                console.error('Error changing appointment time:', error);
                throw error;
            }
        },
        async huyLichHen(id: String) {
            const {updateDataAfterBooking} = useMauKhachDatDichVu();
            const token = localStorage.getItem('access_token');
            try {
                const response = await fetch(`http://localhost:8080/api/dat-lich/huy-lich/${id}`, {
                    method: 'PUT',
                    headers: {
                        'Authorization': `Bearer ${token}`,
                    },
                });
                const data = await response.json();
            } catch (error) {
                console.error('Error fetching lich hen details:', error);
            }
        },
    },
    getters: {
        getDichVu: (state) => state.lichHenDetails?.dichvu,
        getCaLichHen: (state) => state.lichHenDetails?.idcalichhen,
        getThuCung: (state) => state.lichHenDetails?.thucung,
        getDate: (state) => state.lichHenDetails?.date,
    }
});