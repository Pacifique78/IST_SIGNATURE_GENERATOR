<template>
  <AuthLoading />
  <RouterView v-if="!loading" />
</template>

<script setup lang="ts">
import { onMounted } from 'vue'
import { storeToRefs } from 'pinia'
import { RouterView } from 'vue-router'
import { useAuthStore } from './stores/auth'
import AuthLoading from './components/auth/AuthLoading.vue'

const authStore = useAuthStore()
const { loading } = storeToRefs(authStore)

onMounted(async () => {
  await authStore.initializeAuth()
})
</script>
