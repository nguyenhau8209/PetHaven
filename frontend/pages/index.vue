<template>
  <div class="container">
    <div v-if="services.length === 0">Dịch vụ không khả dụng, vui lòng thử lại sau!</div>
    <div v-else>
      <h2>Dịch vụ của chúng tôi</h2>
     <div class="container">
       <div class="row">
         <div v-for="service in paginatedServices" :key="service.id" class="card col m-4">
           <h4 class="card-title">{{ service.tendichvu }}</h4>
           <div v-if="service.anh==null">
              <img src="~/assets/image/cat1.jpg" class="card-img-top p-1" alt="Ảnh mặc định nè">
           </div>
          <div v-else>
            <img :src="service.anh" class="card-img-top p-1" alt="...">
          </div>
           <div class="card-body">
             <div class="row">
               <div class="col">
                 <p class="card-text">{{ service.giatien.toLocaleString() }} USD</p>
                 <nuxt-link class="nav-link" :to="`/services/${service.id}`">Xem thêm</nuxt-link>
               </div>
             </div>
           </div>
         </div>
       </div>
     </div>

      <div class="pagination">
        <button @click="prevPage" :disabled="currentPage === 1">Trang trước</button>
        <span>Trang {{ currentPage }} trên {{ totalPages }}</span>
        <button @click="nextPage" :disabled="currentPage === totalPages">Trang sau</button>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { useServiceStore } from '~/stores/DichVuStores';
import { onMounted, computed, ref } from 'vue';
import DichVu from '~/models/DichVu';

export default {
  setup() {
    const serviceStore = useServiceStore();
    const itemsPerPage = 4; // Số lượng dịch vụ hiển thị trên mỗi trang
    const currentPage = ref(1); // Trang hiện tại

    onMounted(() => {
      serviceStore.fetchServices();
    });

    const services = computed((): DichVu[] => {
      return serviceStore.services.filter((service: DichVu) => service.trangthai);
    });
    const totalPages = computed(() => Math.ceil(services.value.length / itemsPerPage));

    // Tính toán các dịch vụ được hiển thị trên trang hiện tại
    const paginatedServices = computed(() => {
      const start = (currentPage.value - 1) * itemsPerPage;
      return services.value.slice(start, start + itemsPerPage);
    });

    const nextPage = () => {
      if (currentPage.value < totalPages.value) {
        currentPage.value++;
      }
    };

    const prevPage = () => {
      if (currentPage.value > 1) {
        currentPage.value--;
      }
    };

    return {
      services,
      paginatedServices,
      currentPage,
      totalPages,
      nextPage,
      prevPage,
    };
  },
};
</script>

<style scoped>
.card {
  background-color: transparent; /* Loại bỏ nền trắng */
  border: none; /* Nếu không muốn viền */
  box-shadow: none; /* Nếu không muốn bóng */
}

.card-title {
  color: #333; /* Điều chỉnh màu tiêu đề nếu cần */
}

.card-body {
  color: #666; /* Điều chỉnh màu văn bản nếu cần */
}

.pagination {
  display: flex;
  justify-content: center;
  margin-top: 20px;
}

.pagination button {
  margin: 0 10px;
}
</style>
