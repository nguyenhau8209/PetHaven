<template>
  <div class="container py-5">
    <div class="row">
      <div class="col-lg-6 mb-4">
        <div class="card shadow-sm h-100">
          <div class="card-body">
            <div v-if="thayDoiLichHenStore.lichHenDetails">
              <div class="card border-primary mb-3">
                <div class="card-header bg-primary text-white">
                  <i class="fas fa-calendar-check me-2"></i>Chi tiết lịch hẹn
                   <div :class="getTrangThaiClass(thayDoiLichHenStore.lichHenDetails.trangthai)">
                    <span class="badge bg-success"> {{ getTrangThaiText(thayDoiLichHenStore.lichHenDetails.trangthai) }}</span>
                   </div>
                </div>
                <div class="card-body">
                  <div class="row g-3">
                    <div v-if="thayDoiLichHenStore.getDichVu && thayDoiLichHenStore.getCaLichHen">
                      <div class="col mb-3">
                        <div class="row">
                          <div class="col">
                            <h5 class="text-muted"><i class="fas fa-clipboard-list text-primary me-2"></i>Dịch vụ</h5>
                            <p class="mb-0"><strong>Tên:</strong> {{ thayDoiLichHenStore.getDichVu.tendichvu }}</p>
                            <p class="mb-0"><strong>Giá tiền:</strong>
                              {{ formatCurrency(thayDoiLichHenStore.getDichVu.giatien) }}</p>
                            <div class="col">
                              <p class="mb-0"><strong>Mô tả:</strong> {{ thayDoiLichHenStore.getDichVu.mota }}</p>
                            </div>
                          </div>
                          <div class="col">
                            <img class="card-img-top" src="/assets/image/cat2.jpg" alt="VN Pay">
                          </div>
                          <div class="col-12">
                            <strong>Thời gian : </strong>
                            <div class="row">
                              <div class="col">
                                Tên ca: {{ thayDoiLichHenStore.getCaLichHen.tenca }}
                              </div>
                              <div class="col">
                                Giờ : {{ thayDoiLichHenStore.getCaLichHen.thoigianca }}
                              </div>
                              <div class="col">
                                Ngày : {{ new Date(thayDoiLichHenStore.getDate).toLocaleDateString() }}
                              </div>
                            </div>
                          </div>
                        </div>
                      </div>
                    </div>
                    <div v-if="thayDoiLichHenStore.getThuCung" class="col-12">
                      <h5 class="text-muted mb-3"><i class="fas fa-paw text-primary me-2"></i>Thông tin thú cưng</h5>
                      <div class="row">
                        <div class="col-md-6 mb-2">
                          <p class="mb-0"><strong>Tên:</strong> {{ thayDoiLichHenStore.getThuCung.ten }}</p>
                        </div>
                        <div class="col-md-6 mb-2">
                          <p class="mb-0"><strong>Tuổi:</strong> {{ thayDoiLichHenStore.getThuCung.tuoi }}</p>
                        </div>
                        <div class="col-md-6 mb-2">
                          <p class="mb-0"><strong>Giống:</strong> {{ thayDoiLichHenStore.getThuCung.giong }}</p>
                        </div>
                        <div class="col-md-6 mb-2">
                          <p class="mb-0"><strong>Cân nặng:</strong> {{ thayDoiLichHenStore.getThuCung.cannang }} kg</p>
                        </div>
                        <div class="col-12">
                          <p class="mb-0"><strong>Ghi chú:</strong>
                            {{ thayDoiLichHenStore.getThuCung.ghichu || 'Không có' }}</p>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
            <div v-else class="text-center">
              <img src="/assets/image/cat3.jpg" alt="Chọn dịch vụ" class="img-fluid rounded mb-3"
                   style="max-height: 200px;">
              <p class="lead">Đang tải thông tin lịch hẹn...</p>
            </div>
          </div>
        </div>
      </div>
      <div v-if="thayDoiLichHenStore.lichHenDetails && (thayDoiLichHenStore.lichHenDetails.trangthai == 4 || thayDoiLichHenStore.lichHenDetails.trangthai == 6)" class="col-lg-6">
        <div class="card-body">
          <Calendar :id="String(id)"/>
        </div>
      </div>
      <div v-else class="col-lg-6">
        <div class="card p-4 ">
          <p class="text">Bạn đã hết thời gian đổi lịch hẹn và hủy lịch hẹn !</p>
          <img src="/assets/image/cat3.jpg" alt=""/>
          <div class="card-body">
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import {useRoute} from 'vue-router'
import Calendar from '~/components/CalendarChange.vue'
import Pet from '~/components/Pet.vue'
import {useServiceStore} from '~/stores/DichVuStores';
import {useMauKhachDatDichVu} from '~/stores/MauKhachDatDichVu'
import DichVu from "~/models/DichVu";
import {useToast} from 'vue-toastification'
import {useDatLichStore} from '~/stores/DatLichStores'
import {ref, computed, onMounted} from 'vue';
import {useThayDoiLichHenStore} from '~/stores/ThayDoiLichHen'

const thayDoiLichHenStore = useThayDoiLichHenStore()
const accessToken = sessionStorage.getItem('access_token');
const viewRole = sessionStorage.getItem('viewRole');
const serviceStore = useServiceStore();
const {getTempData} = useMauKhachDatDichVu()
const tempData = computed(() => getTempData())
const services = computed((): DichVu[] => {
  return serviceStore.services.filter((service: DichVu) => service.trangthai);
});

const route = useRoute()
const id = Array.isArray(route.params.id) ? route.params.id[0] : route.params.id;
onMounted(async () => {
  await thayDoiLichHenStore.fetchLichHenDetails(Number(id))
})
definePageMeta({
  middleware: ['auth']
})

const formatCurrency = (value: number) => {
  console.log('Thông tin đầy đủ của thayDoiLichHenStore:', JSON.stringify(thayDoiLichHenStore, null, 2));
  return new Intl.NumberFormat('vi-VN', {style: 'currency', currency: 'VND'}).format(value);
};
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

<style scoped>

.card {
  transition: all 0.3s ease;
}

.card:hover {
  transform: translateY(-5px);
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
}
</style>