<template>
  <div>
    <h1>Lịch Sử Đặt Lịch</h1>
    <table>
      <thead>
      <tr>
        <th>stt</th>
        <th>Tên dịch vụ</th>
        <th>Thời gian</th>
        <th>Trạng thái</th>
        <th>Hành động</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="lichhen in lichhenList" :key="lichhen.id">
        <td>{{ lichhen.index + 1 }}</td>
        <td>{{ lichhen.constructor.name }}</td>
<!--        <td>{{ lichhen.thucung?.tenThucung || "N/A" }}</td>-->
<!--        <td>{{ lichhen.dichvu?.tenDichVu || "N/A" }}</td>-->
<!--        <td>{{ lichhen.emailNguoiDat }}</td>-->
<!--        <td>{{ lichhen.date }}</td>-->
      </tr>
      </tbody>
    </table>
  </div>
</template>

<script>
import axios from 'axios';
import API_CONFIG from '../../apiconfig/ApiConfig';
import {computed} from "vue";
import {useUserStore} from "~/stores/user.ts";
definePageMeta({
  middleware: ['auth']
})
const userStore = useUserStore()

const userInfo = computed(() => userStore.userInfo)

function login1() {
  if (process.client) {
    window.location.href = 'http://localhost:8080/oauth2/authorization/keycloak';
  }
}

if (userInfo.value === null) {
  login1();
}

export default {
  data() {
    return {
      lichhenList: [], // Khởi tạo danh sách rỗng để lưu trữ dữ liệu lịch hẹn
    };
  },
  mounted() {
    this.fetchLichHen();
  },
  methods: {
    async fetchLichHen() {
      try {
        const response = await axios.get(API_CONFIG.API_ENDPOINTS.lichSuDatLich.getAll);
        this.lichhenList = response.data; // Gán dữ liệu API trả về vào `lichhenList`
      } catch (error) {
        console.error('Lỗi khi lấy dữ liệu lịch sử đặt lịch:', error);
      }
    },
  },
};
</script>


<style scoped>

</style>