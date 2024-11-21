<script setup lang="ts">
import { useUserStore } from '~/stores/user'
import { useQuanLyLichHenKhachHang } from '~/stores/QuanLyLichHenKhachHang'
import { onMounted } from 'vue'
import {useThayDoiLichHenStore } from '~/stores/ThayDoiLichHen'
const {thayDoiLichHenStore,huyLichHen} = useThayDoiLichHenStore()
const userStore = useUserStore()
const lichHenStore = useQuanLyLichHenKhachHang()
const isLoading = ref(false);
const countdown = ref(10);
const showOverlay = ref(false);
definePageMeta({
  middleware: ['auth']
})
onMounted(async () => {
     await lichHenStore.fetchAppointments();
})

function handleHuyLichHen(id: String) {
  huyLichHen(id);
}

function thayDoiLichHen(id: String) {
  return navigateTo(`/chi-tiet-lich/${id}`);
}
function chiTietLichHen(id: String) {
  
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

const token = localStorage.getItem('access_token');
function thanhToan(id: String) {
  const { getTempData } = useMauKhachDatDichVu()
  const tempData = computed(() => getTempData())

  startCountdown(); // Start the countdown

  fetch('http://localhost:8080/api/payPal/payment/create', {
    method: 'POST',
    headers: {
      'Authorization': `Bearer ${token}`,
      'idLichHen': id
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

// Hàm format trạng thái
const getTrangThaiText = (trangthai: number) => {
  const trangThaiMap: Record<number, string> = {
    0: 'Thành công',
    1: 'Thất bại',
    2: 'Đã hủy',
    3: 'Chờ thanh toán',
    4: 'Chờ xác nhận',
    5: 'Rỗng',
    6: 'Thanh toán thành công'
  }
  return trangThaiMap[trangthai] || 'Không xác định'
}

// Hàm format class theo trạng thái
const getTrangThaiClass = (trangthai: number) => {
  const classMap: Record<number, string> = {
    0: 'text-success',
    1: 'text-danger',
    2: 'text-danger',
    3: 'text-info',
    4: 'text-warning',
    5: 'text-secondary',
    6: 'text-success'
  }
  return classMap[trangthai] || ''
}
</script>

<template>
  <div class="container py-4">
    <div class="card shadow-sm">
      <div class="card-header bg-primary text-white py-3">
        <h4 class="mb-0">Quản lý lịch hẹn của {{ userStore.userInfo?.name }}</h4>
      </div>

      <div class="card-body">
        <!-- Loading state -->
        <div v-if="lichHenStore.isLoading" class="text-center py-5">
          <div class="spinner-border text-primary" role="status">
            <span class="visually-hidden">Đang tải...</span>
          </div>
        </div>

        <!-- Error state -->
        <div v-else-if="lichHenStore.error" class="alert alert-danger">
          {{ lichHenStore.error }}
          <button class="btn btn-link" @click="lichHenStore.clearError">Đóng</button>
        </div>

        <!-- Data display -->
        <div v-else>
          <div class="table-responsive">
            <table class="table table-hover">
              <thead class="table-light">
                <tr>
                  <th>STT</th>
                  <th>Dịch vụ</th>
                  <th>Thú cưng</th>
                  <th>Ngày hẹn</th>
                  <th>Ca</th>
                  <th>Trạng thái</th>
                  <th>Thao tác</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="(item, index) in lichHenStore.paginatedAppointments"
                    :key="item.id"
                    v-memo="[item.trangthai]">
                  <td>{{ lichHenStore.currentPage * lichHenStore.pageSize + index + 1 }} : {{item.id}}</td>
                  <td>
                    <div>{{ item.dichvu.tendichvu }}</div>
                    <small class="text-muted">{{ item.dichvu.giatien.toLocaleString() }} USD</small>
                  </td>
                  <td>
                    <div>{{ item.thucung.ten }}</div>
                    <small class="text-muted">{{ item.thucung.giong }}</small>
                  </td>
                  <td>{{ new Date(item.date).toLocaleDateString('vi-VN') }}</td>
                  <td>
                    {{ item.idcalichhen.tenca }}
                    <div><small>{{ item.idcalichhen.thoigianca }}</small></div>
                  </td>
                  <td>
                    <span :class="getTrangThaiClass(item.trangthai)">
                      {{ getTrangThaiText(item.trangthai) }}
                    </span>
                  </td>
                  <td>
                    <div class="row">
                        <div class="col">
                          <button v-if="item.trangthai === 4 || item.trangthai === 6"
                            class="custom-button"
                            @click="thayDoiLichHen(item.id)">
                      Hủy hoặc đổi lịch hẹn
                    </button>
                        </div>
                        <div class="col">
                          <button v-if="item.trangthai === 0 || item.trangthai === 6"
                            class="custom-button"
                            @click="chiTietLichHen(item.id)">
                      Chi tiết
                    </button>
                        </div>
                        <div class="col">
                          <button v-if="item.trangthai === 3"
                            class="custom-button"
                            @click="thanhToan(item.id)">
                      Thanh toán
                    </button>
                        </div>
                    </div>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>

          <!-- Pagination -->
          <div class="d-flex justify-content-between align-items-center mt-3">
            <div>
              Hiển thị {{ lichHenStore.paginatedAppointments.length }} / {{ lichHenStore.appointments.length }} lịch hẹn
            </div>
            <div class="btn-group">
              <button class="btn btn-outline-primary" 
                      :disabled="lichHenStore.currentPage === 0"
                      @click="lichHenStore.prevPage">
                <i class="fas fa-chevron-left"></i>
              </button>
              <button class="btn btn-outline-primary" 
                      :disabled="lichHenStore.currentPage >= lichHenStore.totalPages - 1"
                      @click="lichHenStore.nextPage">
                <i class="fas fa-chevron-right"></i>
              </button>
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
.table th {
  font-weight: 600;
}
.table td {
  vertical-align: middle;
}

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
</style>