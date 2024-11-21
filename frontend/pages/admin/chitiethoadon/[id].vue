<template>
    <div class="container">

        <div v-if="loading">Đang tải thông tin hóa đơn...</div>

      <div v-else>
        <h1>Chi tiết hóa đơn #{{ hoaDon.id }}</h1>
        <div class="row">
            <div class="col">
              <div class="card p-4">
                <h3>Thông tin khách hàng</h3>
                <div class="card-body">
                  <p><strong>Email:</strong> {{ hoaDon.idlichhen.emailNguoiDat }}</p>
                  <p><strong>Ngày đặt:</strong> {{ formatDate(hoaDon.date) }}</p>
                  <p><strong>Phương thức thanh toán:</strong> {{ hoaDon.phuongthucthanhtoan }}</p>
                  <p><strong>Số tiền:</strong> {{ hoaDon.sotien }} USD</p>
                  <p><strong>Trạng thái:</strong> {{ getTrangThai(hoaDon.trangthai) }}</p>
                </div>
              </div>
            </div>
            <div class="col">
              <div class="card p-4">
                <h3>Thông tin thú cưng</h3>
                <div class="card-body">
                  <p><strong>Tên thú cưng:</strong> {{ hoaDon.idlichhen.thucung.ten }}</p>
                  <p><strong>Giống:</strong> {{ hoaDon.idlichhen.thucung.giong }}</p>
                  <p><strong>Cân nặng:</strong> {{ hoaDon.idlichhen.thucung.cannang }} kg</p>
                  <p><strong>Tuổi:</strong> {{ hoaDon.idlichhen.thucung.tuoi }} năm</p>
                </div>
              </div>
            </div>
            <div class="col">
              <div class="card p-4">
                <h3>Thông tin dịch vụ</h3>
                <div class="card-body">
                  <p><strong>Tên dịch vụ:</strong> {{ hoaDon.idlichhen.dichvu.tendichvu }}</p>
                  <p><strong>Mô tả:</strong> {{ hoaDon.idlichhen.dichvu.mota }}</p>
                </div>
              </div>
            </div>
          </div>
        <div class="row">
          <div class="col">
            <button @click="inHoaDon(hoaDon.id)" class="custom-button">In hóa đơn</button>
          </div>
          <div class="col">
            <button @click="goBack" class="custom-button">Quay lại</button>
          </div>
        </div>
        </div>


    </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { useQuanLyHoaDonStore } from '~/stores/QuanLyHoaDon';

const route = useRoute();
const router = useRouter();
const store = useQuanLyHoaDonStore();
const hoaDon = ref(null);
const loading = ref(true);

const fetchHoaDon = async (id: number) => {
    await store.fetchListHoaDon();
    hoaDon.value = store.ListHoaDon.find(item => item.id === id);
    loading.value = false;
};

const formatDate = (dateString: string) => {
    const date = new Date(dateString);
    const day = String(date.getDate()).padStart(2, '0');
    const month = String(date.getMonth() + 1).padStart(2, '0');
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

const goBack = () => {
    router.push('/admin/quanlyhoadon');
};

onMounted(() => {
    const id = Number(route.params.id);
    fetchHoaDon(id);
});

function inHoaDon(id : String) {
  store.inHoaDon(id);
}
</script>

<style scoped>
.container {
    margin-top: 20px;
}
</style>