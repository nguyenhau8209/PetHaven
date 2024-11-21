<template>
  <div>
    <label :for="name">{{ label }}</label>
    <!-- Sử dụng internalValue thay vì v-model -->
    <input :id="name" v-bind="$attrs" :value="internalValue" @input="onInput" @blur="onBlur" />
    <span v-if="errors && errors.length">{{ errors }}</span>
  </div>
</template>

<script>
import { useField } from 'vee-validate';
import * as yup from 'yup';
import { computed } from 'vue'; // Đảm bảo import computed

export default {
  name: 'InputField',
  props: {
    name: String,
    label: String,
    rules: String,
    modelValue: String // Thêm modelValue để đồng bộ với v-model từ bên ngoài
  },
  setup(props, { emit }) {
    const {value: fieldValue, errorMessage: errors, handleBlur: onBlur} = useField(
        props.name,
        yup.string().required('Trường này không được để trống')
    );

    const internalValue = computed({
      get() {
        return props.modelValue;
      },
      set(value) {
        emit('update:modelValue', value);
        fieldValue.value = value; // Cập nhật giá trị của useField
      }
    });

    const onInput = (event) => {
      internalValue.value = event.target.value;
    };

    return {
      internalValue,
      errors,
      onBlur,
      onInput
    };
  }
};
</script>

<style scoped>
span {
  color: red;
}
</style>
