<script setup>
import { onMounted } from "vue";
const doInit = () => {
  console.log("hellow world");
};
onMounted(() => {
  doInit();
});
</script>
<template>
  <router-view />
</template>
<style></style>
