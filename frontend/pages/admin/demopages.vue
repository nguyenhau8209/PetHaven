<script setup lang="ts">
import { useUserStore } from '~/stores/user'
import { computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'

const userStore = useUserStore()
const router = useRouter()
definePageMeta({
  middleware: ['auth']
})
const userInfo = computed(() => userStore.userInfo)
// Đây là trang demo
// Xử lí khi chưa đăng nhập và có đường dấn vào đây và với vai trò không phù hợp
onMounted(() => {
  if (!userStore.isLoggedIn) {
    router.push('/')
  }
})
</script>

<template>
  <div class="container">
    <div v-if="userInfo===null">
      <p>Vui lòng đăng nhập để xem thông tin người dùng.</p>
    </div>
    <div v-else>
      <h1>Thông tin người dùng</h1>
      <p>Họ tên: {{ userInfo.name }}</p>
      <p>Roles: {{ userInfo.role }}</p>
    </div>
  </div>
</template>

<style scoped>
/* Styles remain unchanged */
</style>