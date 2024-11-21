<template>
  <div class="calendar-container">
    <FullCalendar
      :options="calendarOptions"
      class="calendar"
    />
    <div class="booking-details card m-4">
      <div class="card-body">
        <div class="row">
          <div class="col-12 mb-3">
            <p class="selected-date">Bạn đã chọn ngày: <strong>{{ formattedSelectedDate }}</strong></p>
          </div>
          <div v-if="services && services.length && lichhens && lichhens.length">
            <div class="row">
              <div class="col-md-6 mb-3">
                <label for="service" class="form-label">Dịch vụ:</label>
                <select id="service" v-model="selectedService" class="form-select">
                  <option value="" disabled>Chọn dịch vụ</option>
                  <option v-for="service in services" :key="service.id" :value="service.id">
                    {{ service.tendichvu }}
                  </option>
                </select>
              </div>
              <div class="col-md-6 mb-3">
                <label for="time" class="form-label">Thời gian:</label>
                <select id="time" v-model="selectedTime" class="form-select">
                  <option value="" disabled>Chọn thời gian</option>
                  <option v-for="lichhen in lichhens" :key="lichhen.id" :value="lichhen.id">
                    {{ lichhen.tenca }}: {{ lichhen.thoigianca }}
                  </option>
                </select>
              </div>
            </div>

            <div class="row">
              <div class="col-12">
                <button @click="confirmBooking" class="btn btn-primary mt-3" :disabled="!isFormValid">
                  Tiếp tục
                </button>
              </div>
            </div>
          </div>
          <div v-else class="col-12">
            <p class="text-danger">Ngày bạn đã chọn không khả dụng. Vui lòng chọn lại ngày khác.</p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, watch } from 'vue'
import FullCalendar from '@fullcalendar/vue3'
import dayGridPlugin from '@fullcalendar/daygrid'
import timeGridPlugin from '@fullcalendar/timegrid'
import interactionPlugin from '@fullcalendar/interaction'
import viLocale from '@fullcalendar/core/locales/vi'
import { useToast } from 'vue-toastification'
import DichVu from "~/models/DichVu"
import CaHen from "~/models/CaHen"
import { useDatLichStore } from '~/stores/DatLichStores'
import { useMauKhachDatDichVu } from '~/stores/MauKhachDatDichVu'

import DichVuKhachDat from "~/models/DichVuKhachDat";

const { saveTempData, getTempData, clearTempData } = useMauKhachDatDichVu()


const datLichStore = useDatLichStore()
const toast = useToast()

const today = new Date()
const endDate = new Date(today)
endDate.setDate(today.getDate() + 6)

const selectedDate = ref(new Date())
const selectedService = ref('')
const selectedTime = ref('')

const formattedSelectedDate = computed(() => {
  return new Intl.DateTimeFormat('vi-VN', { weekday: 'long', year: 'numeric', month: 'long', day: 'numeric' }).format(selectedDate.value)
})

const fetchCaHen = datLichStore.fetchCaHen


const services = computed((): DichVu[] => datLichStore.DichVu)
const lichhens = computed((): CaHen[] => datLichStore.CaLichHen)


const isFormValid = computed(() => {
  return selectedDate.value && selectedService.value && selectedTime.value
})
function formatDate(date) {
  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, '0');
  const day = String(date.getDate()).padStart(2, '0');
  return `${year}-${month}-${day}`;
}
onMounted(() => {
  const today = new Date();
  const formattedDate = formatDate(today);
  fetchCaHen(formattedDate);
  handleDateClick({ dateStr: formattedDate });
})

watch(selectedDate, (newDate) => {
  fetchCaHen(newDate)
})

const calendarOptions = computed(() => ({
  plugins: [dayGridPlugin, timeGridPlugin, interactionPlugin],
  initialView: 'dayGridWeek',
  headerToolbar: {
    left: 'prev,next',
    center: 'title',
    right: 'dayGridWeek,today'
  },
  events: events.value,
  eventClick: handleEventClick,
  dateClick: handleDateClick,
  locale: viLocale,
  buttonText: {
    today: 'Hôm nay',
    week: '7 ngày',
    dayGridWeek: 'Tuần'
  },
  validRange: {
    start: today,
    end: endDate
  },
  visibleRange: {
    start: today,
    end: endDate
  },
  duration: { days: 7 },
  dayCount: 7,
  height: 'auto',
  contentHeight: 'auto',
  aspectRatio: 1.8,
  expandRows: true,
  slotMinTime: '07:00:00',
  slotMaxTime: '19:00:00',
  allDaySlot: false,
  stickyHeaderDates: true,
  nowIndicator: true
}))

function handleEventClick(info) {
  // toast.info(`Bạn đã chọn: ${info.event.title}`, { timeout: 2000 })
}

const events = ref([
  {
    title: 'Chọn',
    start: new Date(),
    color: '#3788d8',
    allDay: true
  }
])
function handleDateClick(info) {
  selectedDate.value = new Date(info.dateStr)
  datLichStore.updateDatLichInfo(info.dateStr)
  events.value = [{ title: 'Chọn', start: info.dateStr, color: '#3788d8' }]
}

function confirmBooking() {
  if (isFormValid.value) {
    const selectedDichVu = services.value.find(service => service.id === Number(selectedService.value));
    const selectedCaLichHen = lichhens.value.find(ca => ca.id === Number(selectedTime.value));

    if (!selectedDichVu || !selectedCaLichHen) {
      toast.error('Không tìm thấy dịch vụ hoặc ca lịch hẹn đã chọn', { timeout: 3000 });
      return;
    }

    const dichVuKhachDat = new DichVuKhachDat({
      dichvu: selectedDichVu,
      calichhen: selectedCaLichHen,
      date: selectedDate.value
    });

    saveTempData(dichVuKhachDat);
    console.log('Dữ liệu đã lưu:', getTempData());
  } else {
    toast.error('Vui lòng điền đầy đủ thông tin đặt lịch', { timeout: 3000 });
  }
}
</script>

<style scoped>
.calendar-container {
  max-width: 1000px;
  margin: 0 auto;
  padding: 30px;
  box-shadow: 0 10px 30px rgba(0,0,0,0.1);
  border-radius: 12px;
  background-color: #ffffff;
}

.calendar-title {
  text-align: center;
  color: #333;
  margin-bottom: 30px;
  font-size: 28px;
  font-weight: 600;
}

.calendar {
  font-family: 'Arial', sans-serif;
  margin-bottom: 30px;
}

.booking-details {
  background-color: #f8f9fa;
  border-radius: 8px;
  transition: all 0.3s ease;
}

.booking-details:hover {
  box-shadow: 0 5px 15px rgba(0,0,0,0.1);
}

.selected-date {
  font-size: 1.1em;
  color: #007bff;
}

.form-select {
  border-radius: 20px;
  border: 1px solid #ced4da;
  padding: 10px 15px;
  transition: all 0.3s ease;
}

.form-select:focus {
  border-color: #80bdff;
  box-shadow: 0 0 0 0.2rem rgba(0,123,255,.25);
}

.btn-primary {
  border-radius: 20px;
  padding: 10px 20px;
  font-weight: bold;
  transition: all 0.3s ease;
}

.btn-primary:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 8px rgba(0,123,255,0.3);
}

:deep(.fc) {
  --fc-border-color: #e0e0e0;
  --fc-button-bg-color: #007bff;
  --fc-button-border-color: #007bff;
  --fc-button-hover-bg-color: #0056b3;
  --fc-button-hover-border-color: #0056b3;
  --fc-button-active-bg-color: #0056b3;
  --fc-button-active-border-color: #0056b3;
  --fc-event-bg-color: #007bff;
  --fc-event-border-color: #007bff;
  --fc-today-bg-color: #e6f2ff;
}

:deep(.fc-button-primary) {
  border-radius: 20px;
  padding: 8px 16px;
  text-transform: uppercase;
  font-weight: bold;
  transition: all 0.3s ease;
}

:deep(.fc-button-primary:not(:disabled):hover) {
  background-color: var(--fc-button-hover-bg-color);
  border-color: var(--fc-button-hover-border-color);
  transform: translateY(-2px);
  box-shadow: 0 4px 8px rgba(0,123,255,0.3);
}

:deep(.fc-day-today) {
  background-color: var(--fc-today-bg-color) !important;
}

:deep(.fc-event) {
  border: none;
  border-radius: 4px;
  padding: 3px 8px;
  font-size: 0.85em;
  font-weight: bold;
}

:deep(.fc-timegrid-slot) {
  height: 50px !important;
}

:deep(.fc-timegrid-axis) {
  padding-right: 10px;
}

:deep(.fc-col-header-cell) {
  padding: 10px 0;
  font-weight: bold;
}

:deep(.fc-day-grid-event) {
  margin: 2px 0;
}

:deep(.fc-toolbar-title) {
  font-size: 20px !important;
  font-weight: 600;
}
</style>