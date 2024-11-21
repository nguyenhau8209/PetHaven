<script setup lang="ts">
</script>

<template>
  <div class="container d-flex justify-content-center align-items-center min-vh-100">
    <div class="card shadow-lg text-center" style="max-width: 500px; border-radius: 15px;">
      <img class="card-img-top p-4 rounded-circle mx-auto"
           style="width: 150px; height: 150px; object-fit: cover;"
           src="~/assets/image/meochan.jpg" alt="Mèo buồn">
      <div class="card-body">
        <h4 class="card-title text-danger fw-bold">Truy cập bị từ chối</h4>
        <p class="card-text text-muted">
          Bạn không có quyền truy cập vào trang này! Nếu bạn nghĩ đây là lỗi, vui lòng liên hệ quản trị viên.
        </p>
        <NuxtLink to="/" class="btn btn-primary mt-3">
          Quay lại trang chủ
        </NuxtLink>
      </div>
    </div>
  </div>
</template>

<style scoped>
/* Định nghĩa bố cục trung tâm */
.container {
  background-color: #f8f9fa;
  padding: 2rem;
}

/* Hiệu ứng bóng và viền */
.card {
  border: none;
  background-color: #ffffff;
}

/* Tùy chỉnh cho nút */
.btn-primary {
  background-color: #007bff;
  border-color: #007bff;
  transition: background-color 0.3s ease, transform 0.2s ease;
}

.btn-primary:hover {
  background-color: #0056b3;
  transform: scale(1.05);
}
</style>
