<template>
    <div class="container">
        <h1>Quản lý hóa đơn</h1>
        <button @click="fetchHoaDon" class="btn btn-primary">Tải danh sách hóa đơn</button>
        <table class="table table-striped mt-3">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Ngày</th>
                    <th>Phương thức thanh toán</th>
                    <th>Số tiền</th>
                    <th>Trạng thái</th>
                    <th>Thao tác</th>
                </tr>
            </thead>
            <tbody>
                <tr v-for="hoaDon in hoaDonList" :key="hoaDon.id">
                    <td>{{ hoaDon.idlichhen.id }}</td>
                    <td>{{ formatDate(hoaDon.date) }}</td>
                    <td>{{ hoaDon.phuongthucthanhtoan }}</td>
                    <td>{{ hoaDon.sotien }} USD</td>
                    <td>{{ getTrangThai(hoaDon.trangthai) }}</td>
                    <td>
                        <button @click="viewHoaDon(hoaDon.id)" class="btn btn-primary">Chi tiết</button>
                    </td>
                </tr>
            </tbody>
        </table>
    </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import { useQuanLyHoaDonStore } from '~/stores/QuanLyHoaDon';

const store = useQuanLyHoaDonStore();
const hoaDonList = ref([]);
definePageMeta({
  middleware: ['auth']
})
const fetchHoaDon = async () => {
    await store.fetchListHoaDon();
    hoaDonList.value = store.ListHoaDon;
};
const formatDate = (dateString: string) => {
    const date = new Date(dateString);
    const day = String(date.getDate()).padStart(2, '0');
    const month = String(date.getMonth() + 1).padStart(2, '0'); // Tháng bắt đầu từ 0
    const year = date.getFullYear();
    const hours = String(date.getHours()).padStart(2, '0');
    const minutes = String(date.getMinutes()).padStart(2, '0');

    return `${day}/${month}/${year} ${hours}:${minutes}`;
};
const getTrangThai = (trangthai: number) => {
    switch (trangthai) {
        case 1: return 'Chờ thanh toán';
        case 2: return 'Thành công';
        case 3: return 'Thất bại';
        default: return 'Không xác định';
    }
};
const viewHoaDon = (id: number) => {
    navigateTo(`/admin/chitiethoadon/${id}`);
};

</script>

<style scoped>
.table {
    width: 100%;
    margin-top: 20px;
}
</style>