import { defineStore } from 'pinia';
import { ref, computed } from 'vue';
import type { BookingData } from './MauKhachDatDichVu';

export const useQuanLyLichHenKhachHang = defineStore('quanLyLichHenKhachHang', () => {
  // State
  const appointments = ref<BookingData[]>([]);
  const currentPage = ref(0);
  const pageSize = ref(10);
  const isLoading = ref(false);
  const error = ref<string | null>(null);

  // Getters
  const sortedAppointments = computed(() => {
    return [...appointments.value].sort((a, b) => {
      return new Date(b.date).getTime() - new Date(a.date).getTime();
    });
  });

  const paginatedAppointments = computed(() => {
    const start = currentPage.value * pageSize.value;
    const end = start + pageSize.value;
    return sortedAppointments.value.slice(start, end);
  });

  const totalPages = computed(() => {
    return Math.ceil(appointments.value.length / pageSize.value);
  });

  // Actions
  async function fetchAppointments() {
    isLoading.value = true;
    try {
      const response = await fetch(`http://localhost:8080/api/lich-hen/findByIdUser`, {
        headers: {
          'Authorization': `Bearer ${localStorage.getItem('access_token')}`
        }
      });
      
      if (!response.ok) throw new Error('Lỗi khi tải dữ liệu');
      
      const data = await response.json();
      appointments.value = data.content;
      console.log('Dữ liệu tải thành công'+data.content);
    } catch (err) {
      error.value = 'Không thể tải lịch hẹn';
      console.error(err);
    } finally {
      isLoading.value = false;
    }
  }

  async function cancelAppointment(appointmentId: number) {
    isLoading.value = true;
    try {
      const response = await fetch(`http://localhost:8080/api/lich-hen/cancel/${appointmentId}`, {
        method: 'PUT',
        headers: {
          'Authorization': `Bearer ${localStorage.getItem('access_token')}`
        }
      });

      if (!response.ok) throw new Error('Lỗi khi hủy lịch hẹn');

      appointments.value = appointments.value.map(apt =>
        apt.id === appointmentId ? {...apt, trangthai: 2} : apt
      );
    } catch (err) {
      error.value = 'Không thể hủy lịch hẹn';
      console.error(err);
    } finally {
      isLoading.value = false;
    }
  }

  function nextPage() {
    if (currentPage.value < totalPages.value - 1) {
      currentPage.value++;
    }
  }

  function prevPage() {
    if (currentPage.value > 0) {
      currentPage.value--;
    }
  }

  function clearError() {
    error.value = null;
  }

  return {
    // State
    appointments,
    currentPage,
    pageSize,
    isLoading,
    error,

    // Getters
    sortedAppointments,
    paginatedAppointments,
    totalPages,

    // Actions
    fetchAppointments,
    cancelAppointment,
    nextPage,
    prevPage,
    clearError
  };
});

