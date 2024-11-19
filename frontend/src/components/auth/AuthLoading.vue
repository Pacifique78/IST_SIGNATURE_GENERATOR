<template>
  <div
    v-if="loading"
    class="fixed inset-0 bg-white bg-opacity-75 z-50 flex items-center justify-center"
  >
    <LoadingSpinner text="Loading..." />
  </div>
</template>

<script setup lang="ts">
import { storeToRefs } from 'pinia'
import { useAuthStore } from '../../stores/auth'
import LoadingSpinner from '../common/LoadingSpinner.vue'

const { loading } = storeToRefs(useAuthStore())
</script>
<script lang="ts">
export default {
  name: 'AuthLoading',
}
</script>
