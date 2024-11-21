<script setup lang="ts">
import { useMauKhachDatDichVu } from '~/stores/MauKhachDatDichVu'
import { computed, onMounted } from "vue";

const { getTempData } = useMauKhachDatDichVu()
const tempData = computed(() => getTempData())
definePageMeta({
  middleware: ['auth']
})
onMounted(() => {
  console.log('tempData:', tempData.value)
})
</script>

<template>
  <div v-if="tempData" class="container my-5">
    <div class="card shadow-lg">
      <div class="card-body">
        <h2 class="card-title text-center mb-4 text">
          <i class="fas fa-check-circle me-2"></i>Xác nhận thanh toán thành công
        </h2>
        <div class="row">
          <div class="col-md-6 mb-4">
            <div class="card h-100">
              <div class="card-body">
                <h4 class="card-title mb-3"><i class="fas fa-paw me-2"></i>Thông tin thú cưng</h4>
                <ul class="list-unstyled">

                  <li><strong>Tên:</strong> {{ tempData.thucung.ten }}</li>
                  <li><strong>Cân nặng:</strong> {{ tempData.thucung.cannang }} kg</li>
                  <li><strong>Tuổi:</strong> {{ tempData.thucung.tuoi }} tuổi</li>
                  <li><strong>Giống:</strong> {{ tempData.thucung.giong }}</li>
                </ul>
              </div>
            </div>
          </div>
          <div class="col-md-6 mb-4">
            <div class="card h-100">
              <div class="card-body">
                <h4 class="card-title mb-3"><i class="fas fa-concierge-bell me-2"></i>Thông tin dịch vụ</h4>
                <ul class="list-unstyled">
                  <li><strong>Tên dịch vụ:</strong> {{ tempData.dichvu.tendichvu }}</li>
                  <li><strong>Mô tả:</strong> {{ tempData.dichvu.mota }}</li>
                  <li><strong>Giá tiền:</strong> {{ tempData.dichvu.giatien.toLocaleString() }} VND</li>
                </ul>
              </div>
            </div>
          </div>
        </div>
        <div class="row">
          <div class="col-md-6 mb-4">
            <div class="card h-100">
              <div class="card-body">
                <h4 class="card-title mb-3"><i class="fas fa-calendar-alt me-2"></i>Thông tin lịch hẹn</h4>
                <ul class="list-unstyled">
                  <li><strong>Ngày:</strong> {{ new Date(tempData.date).toLocaleDateString() }}</li>
                  <li><strong>Ca:</strong> {{ tempData.idcalichhen.tenca }}</li>
                  <li><strong>Thời gian:</strong> {{ tempData.idcalichhen.thoigianca }}</li>
                </ul>
              </div>
            </div>
          </div>
          <div class="col-md-6 mb-4">
            <div class="card h-100">
              <div class="card-body">
                <h4 class="card-title mb-3"><i class="fas fa-user me-2"></i>Thông tin khách hàng</h4>
                <ul class="list-unstyled">
                  <li><strong>ID:</strong> {{ tempData.idkhachhang }}</li>
                  <li><strong>Email:</strong> {{ tempData.emailNguoiDat }}</li>
                </ul>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.card {
  border-radius: 15px;
  transition: all 0.3s ease;
}
.card:hover {
  transform: translateY(-5px);
  box-shadow: 0 10px 20px rgba(0,0,0,0.1);
}
.card-title {
  color: #4a4a4a;
  font-weight: bold;
}
.list-unstyled li {
  margin-bottom: 10px;
}
.fas {
  color: #28a745;
}
</style>