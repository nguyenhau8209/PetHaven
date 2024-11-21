<script setup lang="ts">
definePageMeta({
  middleware: ["auth"],
});

const staffForm = ref({
  fullName: "",
  email: "",
  phone: "",
  role: "",
  password: "",
});

const addStaff = async () => {
  // Xử lý thêm nhân viên mới
};
</script>

<template>
  <div class="container p-4">
    <h2 class="mb-4">Thêm nhân viên mới</h2>

    <form @submit.prevent="addStaff">
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

      <div class="mb-3">
        <label class="form-label">Mật khẩu</label>
        <input
          v-model="staffForm.password"
          type="password"
          class="form-control"
          required
        />
      </div>

      <button type="submit" class="btn btn-primary">Thêm nhân viên</button>
    </form>
  </div>
</template>
