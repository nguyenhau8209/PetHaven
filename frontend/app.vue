<template>
  <ClientOnly>
    <CustomerHome/>
  </ClientOnly>
</template>

<script setup lang="ts">
import CustomerHome from "~/layouts/CustomerHome.vue";

useHead({
  title: 'Pet Haven',
  meta: [
    {
      name: 'description',
      content: 'Pet Haven - Dịch vụ chăm sóc thú cưng tốt nhất'
    }
  ]
});
</script>

<style scoped>

</style>