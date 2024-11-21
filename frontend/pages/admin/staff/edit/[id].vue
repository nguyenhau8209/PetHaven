<script setup lang="ts">
definePageMeta({
  middleware: ["auth"],
});

const route = useRoute();
const staffId = route.params.id;

const staffForm = ref({
  fullName: "",
  email: "",
  phone: "",
  role: "",
});

const fetchStaffDetails = async () => {
  // Lấy thông tin nhân viên theo ID
};

const updateStaff = async () => {
  // Xử lý cập nhật thông tin nhân viên
};

onMounted(() => {
  fetchStaffDetails();
});
</script>

<template>
  <div class="container p-4">
    <h2 class="mb-4">Chỉnh sửa thông tin nhân viên</h2>

    <form @submit.prevent="updateStaff">
      <div class="mb-3">
        <label class="form-label">Họ tên</label>
        <input
          v-model="staffForm.fullName"
          type="text"
          class="form-control"
          required
        />
      </div>

      <div class="mb-3">
        <label class="form-label">Email</label>
        <input
          v-model="staffForm.email"
          type="email"
          class="form-control"
          required
        />
      </div>

      <div class="mb-3">
        <label class="form-label">Số điện thoại</label>
        <input
          v-model="staffForm.phone"
          type="tel"
          class="form-control"
          required
        />
      </div>

      <div class="mb-3">
        <label class="form-label">Chức vụ</label>
        <select v-model="staffForm.role" class="form-control" required>
          <option value="staff">Nhân viên</option>
          <option value="admin">Quản trị viên</option>
        </select>
      </div>

      <button type="submit" class="btn btn-primary">Cập nhật</button>
    </form>
  </div>
</template>
