<script setup lang="ts">
import { ref, computed } from 'vue';
import { useMauKhachDatDichVu } from "~/stores/MauKhachDatDichVu";
const token = localStorage.getItem('access_token');
const isLoading = ref(false);
const countdown = ref(10);
const showOverlay = ref(false);

definePageMeta({
  middleware: ['auth']
})

function thanhToanTaiQuay() {
  return navigateTo('/customer/success');
}

function startCountdown() {
  showOverlay.value = true;
  isLoading.value = true;
  countdown.value = 10;
  const timer = setInterval(() => {
    countdown.value--;
    if (countdown.value <= 0) {
      clearInterval(timer);
      isLoading.value = false;
      showOverlay.value = false;
    }
  }, 1000);
}

function thanhToanOnline() {
  const { getTempData } = useMauKhachDatDichVu()
  const tempData = computed(() => getTempData())

  startCountdown(); // Start the countdown

  fetch('http://localhost:8080/api/payPal/payment/create', {
    method: 'POST',
    headers: {
      'Authorization': `Bearer ${token}`,
      'idLichHen': tempData.value.id
    }
  })
  .then(response => response.text())
  .then(url => {
    if (url) {
      window.location.href = url;
    } else {
      console.error("Không thể tạo thanh toán");
      isLoading.value = false;
      showOverlay.value = false;
    }
  })
  .catch(error => {
    console.error("Lỗi:", error);
    isLoading.value = false;
    showOverlay.value = false;
  });
}
</script>

<template>
  <div class="container">
    
    <div class="card no-border">
      <div class="col-12">
            <div class="text text-center">
              <div>Chúc mừng bạn đã đặt lịch hẹn thành công </div><p>Một email đã gửi về hộp thư của bạn</p>
              <p>Bạn có 20 phút để thay đổi và hủy lịch hẹn</p>
            </div>
      </div>
      <div class="card-body">
        <div class="row">
          <div class="col">
            <div class="card no-border">
              <button type="button" class="custom-button">
                <img class="card-img-top" src="~/assets/image/PayPal-logo.png" alt="PayPal logo">
              </button>
              <div class="card-body">
                <button type="button" class="custom-button" @click="thanhToanOnline">Thanh toán với PayPal</button>
              </div>
            </div>
          </div>
          <div class="col">
            <div class="card no-border">
              <button type="button" @click="thanhToanTaiQuay" class="custom-button">
                    <img class="card-img-top" src="~/assets/image/tratienmat.png" alt="Trả tiền mặt">  
              </button>
              <div class="card-body">
                <button type="button" @click="thanhToanTaiQuay" class="custom-button">
                 Tiếp tục thanh toán tại quầy</button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Overlay and countdown -->
    <div v-if="showOverlay" class="overlay">
      <div class="overlay-content">
        <p>Đang chuyển hướng đến PayPal...</p>
        <p>Vui lòng chờ {{ countdown }} giây</p>
        <div class="spinner-border text-primary" role="status">
          <span class="visually-hidden">Loading...</span>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.no-border {
  border: none !important;
  box-shadow: none !important;
}

.card-img-top {
  width: 100%;
  height: auto;
  max-height: 350px;
  object-fit: contain;
}

.overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.7);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.overlay-content {
  background-color: white;
  padding: 20px;
  border-radius: 5px;
  text-align: center;
}
.text{
  font-size: 1.5rem;
  padding-top: 10px;
}
</style>