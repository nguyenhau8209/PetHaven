<script setup lang="ts">
definePageMeta({
  middleware: ["auth"],
});

const staff = ref([]);

// Thêm composable để xử lý thông báo
const { $toast } = useNuxtApp();

const baseUrl = "http://localhost:8080";
// Import composable
const { makeRequest } = useApi();

// Fetch danh sách nhân viên khi component được tạo
const fetchStaff = async () => {
  try {
    const response = await makeRequest("http://localhost:8080/api/users");
    console.log("response ", response);
    staff.value = response.result.map((member) => ({
      ...member,
      roles: Array.isArray(member.roles)
        ? member.roles.join(", ")
        : member.roles, // Xử lý trường role
    }));
    console.log("staff ", staff);
  } catch (error) {
    console.error("Error:", error);
  }
};

// Xóa nhân viên
const deleteStaff = async (id: string) => {
  if (!confirm("Bạn có chắc chắn muốn xóa nhân viên này?")) return;

  try {
    await $fetch(`/api/staff/${id}`, {
      method: "DELETE",
    });
    await fetchStaff(); // Tải lại danh sách sau khi xóa
    $toast.success("Đã xóa nhân viên thành công");
  } catch (error) {
    $toast.error("Không thể xóa nhân viên");
    console.error(error);
  }
};

// Gọi API lấy danh sách ngay khi component được tạo
onMounted(() => {
  fetchStaff();
});
</script>

<template>
  <div class="container p-4">
    <div class="d-flex justify-content-between mb-4">
      <h2>Danh sách nhân viên</h2>
      <nuxt-link to="/admin/staff/add" class="btn btn-primary">
        Thêm nhân viên mới
      </nuxt-link>
    </div>

    <table class="table">
      <thead>
        <tr>
          <th>ID</th>
          <th>Username</th>
          <th>Email</th>
          <th>First Name</th>
          <th>Last Name</th>
          <th>Role</th>
          <!-- Thêm trường Role -->
          <th>Action</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="member in staff" :key="member.id">
          <td>{{ member.id }}</td>
          <td>{{ member.username }}</td>
          <td>{{ member.email }}</td>
          <td>{{ member.firstName }}</td>
          <td>{{ member.lastName }}</td>
          <td>{{ member.role }}</td>
          <!-- Hiển thị trường Role -->
          <td>
            <nuxt-link
              :to="`/admin/staff/edit/${member.id}`"
              class="btn btn-sm btn-warning me-2"
            >
              Sửa
            </nuxt-link>
            <button
              @click="deleteStaff(member.id)"
              class="btn btn-sm btn-danger"
            >
              Xóa
            </button>
          </td>
        </tr>
      </tbody>
    </table>
  </div>
</template>
