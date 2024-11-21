<template>
  <div v-if="!accessToken || !viewRole" class="container my-5">
    <div class="alert alert-warning" role="alert">
      <i class="fas fa-exclamation-triangle me-2"></i>
      Vui lòng đăng nhập để sử dụng chức năng này!
    </div>
  </div>

  <div v-else class="container py-5">
<!--    <h2 class="mb-4"><i class="fas fa-home me-2"></i>Nhà Haven \ <span class="text-primary">Đặt lịch</span></h2>-->
    <div class="row">
      <div class="col-lg-6 mb-4">
        <div class="card shadow-sm h-100">
          <div class="card-body">
            <h4 class="card-title mb-4"><i class="fas fa-info-circle me-2"></i>Thông tin đặt lịch</h4>
            <div v-if="tempData">
              <div class="card border-primary mb-3">
                <div class="card-header bg-primary text-white">
                  <i class="fas fa-calendar-check me-2"></i>Chi tiết lịch hẹn
                </div>
                <div class="card-body">
                  <div class="row g-3">
                    <div v-if="tempData.idlichhen?.dichvu && tempData.idlichhen?.calichhen">
                      <div class="col mb-3">
                        <div class="row">
                          <div class="col">
                            <h5 class="text-muted"><i class="fas fa-clipboard-list text-primary me-2"></i>Dịch vụ</h5>
                            <p class="mb-0"><strong>Tên:</strong> {{ tempData.idlichhen.dichvu.tendichvu }}</p>
                            <p class="mb-0"><strong>Giá tiền:</strong> {{
                                formatCurrency(tempData.idlichhen.dichvu.giatien)
                              }}</p>
                            <div class="col">
                              <p class="mb-0"><strong>Mô tả:</strong> {{
                                  tempData.idlichhen.dichvu.mota
                                }}</p>
                            </div>
                          </div>
                          <div class="col">
                            <div v-if="tempData.idlichhen.dichvu.anh==null">
                              <img class="card-img-top" src="~/assets/image/cat2.jpg" alt="Anh Meo Meo">
                            </div>
                            <div v-else>
                              <img class="card-img-top" :src="tempData.idlichhen.dichvu.anh" alt="Anh Meo Meo">
                            </div>

                          </div>
                          <div class="col-12">
                            <strong>Thời gian : </strong>
                            <div class="row">
                              <div class="col">
                                Tên ca: {{ tempData.idlichhen.calichhen.tenca }}
                              </div>
                              <div class="col">
                                Giờ : {{ tempData.idlichhen.calichhen.thoigianca }}
                              </div>
                              <div class="col">
                                Ngày : {{ tempData.idlichhen.date.toLocaleDateString() }}
                              </div>
                            </div>
                          </div>
                        </div>
                        </div>
                    </div>
                    <div v-if="tempData.thucung" class="col-12">
                      <h5 class="text-muted mb-3"><i class="fas fa-paw text-primary me-2"></i>Thông tin thú cưng</h5>
                      <div class="row">
                        <div class="col-md-6 mb-2">
                          <p class="mb-0"><strong>Tên:</strong> {{ tempData.thucung.ten }}</p>
                        </div>
                        <div class="col-md-6 mb-2">
                          <p class="mb-0"><strong>Tuổi:</strong> {{ tempData.thucung.tuoi }}</p>
                        </div>
                        <div class="col-md-6 mb-2">
                          <p class="mb-0"><strong>Giống:</strong> {{ tempData.thucung.giong }}</p>
                        </div>
                        <div class="col-md-6 mb-2">
                          <p class="mb-0"><strong>Cân nặng:</strong> {{ tempData.thucung.cannang }} kg</p>
                        </div>
                        <div class="col-12">
                          <p class="mb-0"><strong>Ghi chú:</strong> {{ tempData.thucung.ghichu || 'Không có' }}</p>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
            <div v-else class="text-center">
              <img src="~/assets/image/cat3.jpg" alt="Chọn dịch vụ" class="img-fluid rounded mb-3"
                   style="max-height: 200px;">
              <p class="lead">Hãy lựa chọn dịch vụ và thời gian để bắt đầu.</p>
            </div>
          </div>
        </div>
      </div>
      <div class="col-lg-6">
        <div class="accordion" id="bookingAccordion">
          <div class="accordion-item">
            <h2 class="accordion-header">
              <button class="accordion-button" type="button" data-bs-toggle="collapse" data-bs-target="#collapseOne"
                      :aria-expanded="!isDateTimeSelected" :class="{ 'collapsed': isDateTimeSelected }">

              <i class="fas fa-calendar-alt me-2"></i>Chọn ngày và giờ hẹn
              </button>
            </h2>
            <div id="collapseOne" class="accordion-collapse collapse" :class="{ 'show': !isDateTimeSelected }" data-bs-parent="#bookingAccordion">
              <div class="accordion-body">
                <Calendar/>
              </div>
            </div>
          </div>
          <div class="accordion-item">
            <h2 class="accordion-header">
              <button class="accordion-button" type="button" data-bs-toggle="collapse"
                      data-bs-target="#collapseTwo"
                      :aria-expanded="isDateTimeSelected"
                      :class="{ 'collapsed': !isDateTimeSelected }">

              <i class="fas fa-paw me-2"></i>Thông tin thú cưng
              </button>
            </h2>
            <div id="collapseTwo" class="accordion-collapse collapse"
                 :class="{ 'show': isDateTimeSelected }"
                 data-bs-parent="#bookingAccordion">

            <div class="accordion-body">
                <Pet/>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div  v-if="isBookingComplete">
      <div class="col">
        <div class="card">
          <div class="card-body">
            <h4 class="card-title">Bạn có muốn xác nhận đặt lịch</h4>
            <button
                class="btn btn-secondary"
                @click="payAtCounter"
                :disabled="isLoading"
            >
              <span v-if="!isLoading">Xác nhận</span>
              <span v-else>
                  <i class="fas fa-spinner fa-spin me-2"></i>
                  Đang xử lý... ({{ elapsedTime }}s)
                </span>
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>


</template>

<script setup lang="ts">
import Calendar from '~/components/Calendar.vue'
import Pet from '~/components/Pet.vue'
import {useServiceStore} from '~/stores/DichVuStores';
import {useMauKhachDatDichVu} from '~/stores/MauKhachDatDichVu'
import DichVu from "~/models/DichVu";
import { useToast } from 'vue-toastification'
import { useDatLichStore } from '~/stores/DatLichStores'
import { ref, computed } from 'vue';
import Swal from 'sweetalert2';
const accessToken = localStorage.getItem('access_token');
const viewRole = localStorage.getItem('viewRole');
const serviceStore = useServiceStore();
const {getTempData} = useMauKhachDatDichVu()
const tempData = computed(() => getTempData())
const services = computed((): DichVu[] => {
  return serviceStore.services.filter((service: DichVu) => service.trangthai);
});
definePageMeta({
  middleware: ['auth']
})

const datLichStore = useDatLichStore();

const isDateTimeSelected = computed(() => {
  return tempData.value?.idlichhen?.dichvu && tempData.value?.idlichhen?.calichhen;
})

const formatCurrency = (value: number) => {
  return new Intl.NumberFormat('vi-VN', {style: 'currency', currency: 'VND'}).format(value);
};
const isBookingComplete = computed(() => {
  return tempData.value &&
      tempData.value.idlichhen?.dichvu &&
      tempData.value.idlichhen?.calichhen &&
      tempData.value.thucung;
});

function payWithVNPay() {
  useToast().success('Tính năng đang phát triển ?', {})
}

const isLoading = ref(false);
const elapsedTime = ref(0);
const toast = useToast();


async function payAtCounter() {
  isLoading.value = true;
  elapsedTime.value = 0;
  const timer = setInterval(() => {
    elapsedTime.value++;
  }, 1000);
  const result = await Swal.fire({
      title: 'Xác nhận',
      text: "Bạn có đặt lịch hẹn không?",
      icon: 'success',
      showCancelButton: true,
      confirmButtonColor: '#3085d6',
      cancelButtonColor: '#d33',
      confirmButtonText: 'Có',
      cancelButtonText: 'Không'
    });
  if(result.isConfirmed) {
  try {
    await datLichStore.xacNhanDatLich();
    clearInterval(timer);
    isLoading.value = false;
    return navigateTo('/customer/pay/payinfo');
  } catch (error) {
    clearInterval(timer);
    isLoading.value = false;
    toast.error('Xảy ra lỗi khi xác nhận đặt lịch');
  }
}
}


</script>

<style scoped>
.accordion-button:not(.collapsed) {
  background-color: #e7f1ff;
  color: #0c63e4;
}

.accordion-button:focus {
  box-shadow: none;
  border-color: rgba(0, 0, 0, .125);
}

.card {
  transition: all 0.3s ease;
}

.card:hover {
  transform: translateY(-5px);
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
}
</style>