import {defineStore} from 'pinia';
import DichVu from '../models/DichVu';
import Pageable from '../models/Pageable';
import API_ENDPOINTS from '../apiconfig/ApiConfig';

export const useServiceStore = defineStore('serviceStore', {
    state: () => ({
        services: [] as DichVu[],
        pageable: {} as Pageable,
    })
    ,
    actions: {
        async fetchServices() {
            try {
                const response = await fetch(API_ENDPOINTS.API_ENDPOINTS.dichVu.getAll);
                if (!response.ok) {
                    throw new Error(`HTTP error! Status: ${response.status}`);
                }
                const data = await response.json();
                this.services = data.content;
                this.pageable = data.page;
            } catch (error) {
                console.error('Error fetching services:', error);
            }
        },
        async addDichVu(service: DichVu) {
            const token = localStorage.getItem('access_token');
            const formData = new FormData();
            formData.append('tenDichVu', service.tendichvu);
            formData.append('moTa', service.mota);
            formData.append('giaTien', service.giatien);
            formData.append('trangThai', service.trangthai);

            const fileInput = document.querySelector('#fileInput') as HTMLInputElement;
                if (fileInput && fileInput.files && fileInput.files.length > 0) {
                    formData.append('file', fileInput.files[0]);
                }
            try {
                const response = await fetch(API_ENDPOINTS.API_ENDPOINTS.dichVu.addDichVu, {
                    method: 'POST',
                    headers: {
                        'Authorization': `Bearer ${token}`
                    },
                    body: formData
                });
        
                if (!response.ok) {
                    return { success: false, message: response.status };
                }
                return { success: true, message: 'Thêm dịch vụ thành công' };
            } catch (error) {
                return { success: false, message: 'Lỗi thêm dịch vụ' };
            }
        },
        async updateDichVu(service: DichVu) {
            const updateDichVuUrl = API_ENDPOINTS.API_ENDPOINTS.dichVu.updateDichVu + service.id;
            const token = localStorage.getItem('access_token');
            const formDataUpdate = new FormData();

            formDataUpdate.append('tenDichVu', service.tendichvu);
            formDataUpdate.append('moTa', service.mota);
            formDataUpdate.append('giaTien', service.giatien);
            formDataUpdate.append('trangThai', service.trangthai);

            const fileInput = document.querySelector('#fileUpdate') as HTMLInputElement;
            if (fileInput && fileInput.files && fileInput.files.length > 0) {
                formDataUpdate.append('file', fileInput.files[0]);
            }
            try {
                const response = await fetch(updateDichVuUrl, {
                    method: 'PUT',
                    headers: {
                        'Authorization': `Bearer ${token}`,
                    },
                    body: formDataUpdate,
                });

                if (!response.ok) {
                    throw new Error(`Đã có lỗi xảy ra: ${response.status}`);
                }

                const data = await response.json();
                console.log("Dữ liệu nhận được từ server:", data); // In ra dữ liệu nhận được
                this.services = data.content;
                this.pageable = data.page;
                return { success: true, message: 'Lưu thành công' };
            } catch (error) {
                return { success: false, message: `Đã có lỗi xảy ra: ` + error.message };
            }
        }
        ,
        async deleteDichVu(serviceId: number) {
            const updateDichVuUrl = API_ENDPOINTS.API_ENDPOINTS.dichVu.deleteDichVu + serviceId;
            const token = localStorage.getItem('access_token');
            try {
                const response = await fetch(updateDichVuUrl, {
                    method: 'DELETE',
                    headers: { // Chú ý là phần headers phải nằm trong đối tượng này
                        'Content-Type': 'application/json',
                        'Authorization': `Bearer ${token}`
                    }
                });

                if (!response.ok) {
                    throw new Error(`Failed to delete service. Status: ${response.status}`);
                    return false;
                }

                this.services = this.services.filter(service => service.id !== serviceId);
                return {success: true};
            } catch (error) {
                console.error('Error deleting service:', error);
                return {success: false, message: response.status};
            }
        },
        async getDichVuByName(name: string) {
            const updateDichVuUrl = `${API_ENDPOINTS.API_ENDPOINTS.dichVu.getDichVuByName}?namedv=${encodeURIComponent(name)}`;
            const token = localStorage.getItem('access_token');

            if (!token) {
                return { status: false, message: 'Vui lòng đăng nhập lại để sử dụng dịch vụ' };
            }
            try {
                const response = await fetch(updateDichVuUrl, {
                    method: 'GET',
                    headers: {
                        'Content-Type': 'application/json',
                        'Authorization': `Bearer ${token}`
                    }
                });

                if (response.status === 200) {
                    const data = await response.json();
                    this.services = data.content;
                    this.pageable = data.page;
                    console.log("Dữ liệu trả về: ", data.content);
                    return { status: true, message: 'Tìm kiếm hoạt động ổn' };
                } else if (response.status === 404) {
                    return { status: false, message: 'Không tìm thấy gì' };
                } else if (response.status === 500) {
                    return { status: false, message: 'Lỗi máy chủ vui lòng thử lại' };
                } else {
                    return { status: false, message: `Lỗi không xác định mã ${response.status}` };
                }
            } catch (error) {
                return { status: false, message: 'Đã có lỗi máy chủ xảy ra vui lòng thử lại' };
            }
        }
        ,


        // Cập nhật Trạng thái dịch vụ
        async updateTTDV(serviceId: number) {
            const updateTTDichVuUrl = API_ENDPOINTS.API_ENDPOINTS.dichVu.updateTTDichVu + serviceId;
            const token = localStorage.getItem('access_token');

            try {
                const response = await fetch(updateTTDichVuUrl, {
                    method: 'PUT',
                    headers: {
                        'Content-Type': 'application/json',
                        'Authorization': `Bearer ${token}` // Gửi token trong header
                    },

                });

                if (!response.ok) {
                    throw new Error(`Error: ${response.statusText}`);
                }

                const data = await response.json();
                return {success: true};
            } catch (error) {
                console.error('Lỗi khi cập nhật dịch vụ:', error);
                throw error;
            }
        }
        ,

        // Hàm làm sạch payload
        cleanPayload(payload: any) {
            return JSON.parse(JSON.stringify(payload, (key, value) => {
                if (typeof value === 'function') {
                    return undefined;
                }
                return value;
            }));
        }
    },
});
