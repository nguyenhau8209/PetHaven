<template>
  <div class="container py-5">
        <form @submit.prevent="saveData">
          <div class="row mb-3">
            <div class="col-md-6">
              <div class="form-group">
                <label for="pet-select" class="form-label">Chọn loại thú cưng</label>
                <select v-model="selectedPet" id="pet-select" class="form-select">
                  <option value="new">Thú cưng mới</option>
                  <option v-if="listThuCung?.length > 0" value="existing">Thú cưng hiện có</option>
                </select>
              </div>
            </div>
            <div class="col-md-6" v-if="selectedPet === 'existing'">
              <div class="form-group">
                <label for="existing-pet-select" class="form-label">Chọn thú cưng của bạn</label>
                <select v-model="selectedExistingPet" id="existing-pet-select" class="form-select">
                  <option v-for="pet in listThuCung" :key="pet.id" :value="pet.id">
                    {{ pet.ten }} ({{ pet.giong }})
                  </option>
                </select>
              </div>
            </div>
          </div>
          <div class="row mb-3">
            <div class="col-md-6">
              <div class="form-group">
                <label for="pet-ten" class="form-label">Tên thú cưng</label>
                <input v-model="petForm.ten" type="text" id="pet-ten" class="form-control" :class="{ 'is-invalid': errors.ten }" placeholder="Nhập tên thú cưng">
                <div v-if="errors.ten" class="invalid-feedback">{{ errors.ten }}</div>
              </div>
            </div>
            <div class="col-md-6">
              <div class="form-group">
                <label for="pet-tuoi" class="form-label">Tuổi</label>
                <input v-model="petForm.tuoi" type="number" id="pet-tuoi" class="form-control" :class="{ 'is-invalid': errors.tuoi }" placeholder="Nhập tuổi">
                <div v-if="errors.tuoi" class="invalid-feedback">{{ errors.tuoi }}</div>
              </div>
            </div>
          </div>
          <div class="row mb-3">
            <div class="col-md-6">
              <div class="form-group">
                <label for="pet-giong" class="form-label">Giống</label>
                <input v-model="petForm.giong" type="text" id="pet-giong" class="form-control" :class="{ 'is-invalid': errors.giong }" placeholder="Nhập giống">
                <div v-if="errors.giong" class="invalid-feedback">{{ errors.giong }}</div>
              </div>
            </div>
            <div class="col-md-6">
              <div class="form-group">
                <label for="pet-cannang" class="form-label">Cân nặng (kg)</label>
                <input v-model="petForm.cannang" type="number" id="pet-cannang" class="form-control" :class="{ 'is-invalid': errors.cannang }" placeholder="Nhập cân nặng">
                <div v-if="errors.cannang" class="invalid-feedback">{{ errors.cannang }}</div>
              </div>
            </div>
          </div>
          <div class="mb-3">
            <div class="form-group">
              <label for="pet-notes" class="form-label">Ghi chú</label>
              <textarea v-model="petForm.ghichu" id="pet-notes" rows="3" class="form-control" placeholder="Nhập ghi chú"></textarea>
            </div>
          </div>
          <div class="text-end">
            <button type="submit" class="btn btn-primary">Lưu thông tin</button>
          </div>
        </form>
      </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, watch } from 'vue';
import { useMauKhachDatDichVu } from '~/stores/MauKhachDatDichVu';
import { useDatLichStore } from '~/stores/DatLichStores'
import ThuCungKhachHang from "~/models/ThuCungKhachHang";

const datLichStore = useDatLichStore()
const { saveTempData, getTempData ,updateDataAfterBooking} = useMauKhachDatDichVu();
const listThuCung = computed((): ThuCungKhachHang[] => datLichStore.ListThuCung || []);

const selectedPet = ref('new');
const selectedExistingPet = ref(null);
onMounted(() => {
  if (listThuCung.value.length > 0) {
    selectedPet.value = 'existing';
    selectedExistingPet.value = listThuCung.value[0].id;
    loadExistingPet();
  }
});
const petForm = reactive({
  ten: '',
  tuoi: '',
  giong: '',
  cannang: '',
  ghichu: ''
});

const errors = ref({});

const validateForm = () => {
  errors.value = {};
  if (!petForm.ten) errors.value.ten = 'Vui lòng nhập tên thú cưng';
  if (!petForm.tuoi) {
    errors.value.tuoi = 'Vui lòng nhập tuổi';
  } else if (isNaN(petForm.tuoi) || Number(petForm.tuoi) <= 0) {
    errors.value.tuoi = 'Tuổi phải là số dương';
  }
  if (!petForm.giong) errors.value.giong = 'Vui lòng nhập giống';
  if (!petForm.cannang) {
    errors.value.cannang = 'Vui lòng nhập cân nặng';
  } else if (isNaN(petForm.cannang) || Number(petForm.cannang) <= 0) {
    errors.value.cannang = 'Cân nặng phải là số dương';
  }
  return Object.keys(errors.value).length === 0;
};

const saveData = () => {
  if (validateForm()) {
    const currentData = getTempData();
    const updatedData = {
      ...currentData,
      thucung: {
        ...currentData.thucung,
        ...petForm,
        id: selectedPet.value === 'existing' ? selectedExistingPet.value : null // Thêm dòng này
      }
    };
    updateDataAfterBooking(updatedData);
  }
};

const loadExistingPet = () => {
  if (selectedPet.value === 'existing' && selectedExistingPet.value) {
    const pet = listThuCung.value.find(p => p.id === selectedExistingPet.value);
    if (pet) {
      petForm.id = pet.id;
      petForm.ten = pet.ten;
      petForm.tuoi = pet.tuoi;
      petForm.giong = pet.giong;
      petForm.cannang = pet.cannang;
      petForm.ghichu = pet.ghichu || '';
    }
  } else {
    petForm.id = null;
    Object.keys(petForm).forEach(key => petForm[key] = '');
  }
};

watch(selectedPet, (newValue) => {
  if (newValue === 'new') {
    selectedExistingPet.value = null;
    Object.keys(petForm).forEach(key => petForm[key] = '');
  } else if (newValue === 'existing' && listThuCung.value.length > 0) {
    selectedExistingPet.value = listThuCung.value[0].id;
    loadExistingPet();
  }
});

watch(selectedExistingPet, loadExistingPet);
</script>

<style scoped>
.card {
  transition: box-shadow 0.3s ease-in-out;
}

.card:hover {
  box-shadow: 0 0.5rem 1rem rgba(0, 0, 0, 0.15) !important;
}

.form-control:focus, .form-select:focus {
  border-color: #80bdff;
  box-shadow: 0 0 0 0.2rem rgba(0, 123, 255, 0.25);
}
</style>