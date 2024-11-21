<script setup lang="ts">
import {useUserStore} from '~/stores/user';
import {computed} from "vue";

const userStore = useUserStore();

const userInfo = computed(() => userStore.userInfo);
definePageMeta({
  middleware: ['auth']
})
function login1() {
  if (process.client) {
    window.location.href = 'http://localhost:8080/oauth2/authorization/keycloak';
  }
}

if (userInfo.value === null) {
  login1();
}


</script>

<template>
  <div class="container p-2">
    <div class="card">
      <div class="card-body">
        <div class="form-group">
          <div class="row">
            <div class="col-4">
              <img class="card-img-top" src="../../assets/image/cat3.jpg" alt="">
            </div>
            <div class="col-8">
              <div v-if="Array.isArray(userInfo.role) && userInfo.role.includes('admin') || userInfo.role.includes('manager')">
                <div class="p-4 text fs-4">
                  <div v-if="userInfo.role.includes('admin')">
                    Bạn đang đăng nhập với tài khoản có quyền Admin
                  </div>
                  <div v-else-if="userInfo.role.includes('manager')">
                    Bạn đang đăng nhập với tài khoản có quyền nhân viên
                  </div>
                  <div v-else>
                    Chào mừng khách hàng cảm ơn bạn đã sử dụng dịch vụ !
                  </div>
                </div>
              </div>
              <label for="">Tên người dùng</label>
              <input type="text"
                     class="form-control" name="" id="name" aria-describedby="helpId" placeholder="" v-model="userInfo.name">
            </div>
          </div>
          <div class="container p-4">
            <div v-if="userInfo?.listThuCung==null">
              Bạn chưa lưu thông tin thú cưng nào !
            </div>
            <div v-else>
              <div class="text fs-4">
                Danh sách thú cưng của của chủ nhân :
              </div>
              <div v-for="thuCung in userInfo?.listThuCung" :key="userInfo?.listThuCung.id" class="card m-4">
                <div class="card-body">
                  <table class="table">
                    <thead>
                    <tr>
                      <th>Tên</th>
                      <th>Cân nặng</th>
                      <th>Giống</th>
                      <th>Tuổi</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr>
                      <td>{{thuCung.ten}}</td>
                      <td>{{thuCung.cannang}}</td>
                      <td>{{thuCung.giong}}</td>
                      <td>{{thuCung.tuoi}}</td>
                    </tr>
                    </tbody>
                  </table>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>

</style>