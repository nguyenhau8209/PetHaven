<script setup lang="ts">
definePageMeta({
  middleware: ["auth"],
});
</script>

<template>
  <div class="container p-4">
    <div class="row">
      <div class="col">
        <div class="card" style="width: 18rem">
          <img
            src="~/assets/image/catservice.jpg"
            class="card-img-top p-4"
            alt="..."
          />
          <div class="card-body">
            <h5 class="card-title">Quản lý dịch vụ</h5>
            <p class="card-text">
              Quản lý các dịch vụ đang hoạt động của Pet Haven
            </p>
          </div>
          <ul class="list-group list-group-flush">
            <nuxt-link
              class="list-group-item"
              :to="`/admin/service/servicelist`"
              >Tổng quan dịch vụ</nuxt-link
            >
          </ul>
        </div>
      </div>
      <div class="col">
        <div class="card" style="width: 18rem">
          <img
            src="~/assets/image/cat2.jpg"
            class="card-img-top p-4"
            alt="..."
          />
          <div class="card-body">
            <h5 class="card-title">Quản lý đặt lịch</h5>
            <p class="card-text">
              Quản lý đặt lịch và lịch sử đặt lịch dịch vụ của Pet Haven
            </p>
          </div>
        </div>
      </div>
      <div class="col">
        <div class="card" style="width: 18rem">
          <img
            src="~/assets/image/cat1.jpg"
            class="card-img-top p-4"
            alt="..."
          />
          <div class="card-body">
            <h5 class="card-title">
              Quản lý tài khoản khách hàng và nhân viên
            </h5>
            <p class="card-text">
              Quản lý các thông tin tài khoản của khách hàng và nhân viên
            </p>
          </div>
          <ul class="list-group list-group-flush">
            <nuxt-link class="list-group-item" :to="`/admin/staff/stafflist`"
              >Quản lý nhân viên</nuxt-link
            >
          </ul>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped></style>
