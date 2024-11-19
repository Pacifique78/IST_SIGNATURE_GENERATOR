<template>
  <div class="flex justify-center items-center">
    <div
      class="animate-spin rounded-full h-8 w-8 border-b-2 border-gray-900"
      :class="{ 'border-white': light }"
    ></div>
    <span v-if="text" class="ml-3" :class="{ 'text-white': light }">
      {{ text }}
    </span>
  </div>
</template>

<script setup lang="ts">
defineProps<{
  text?: string
  light?: boolean
}>()
</script>
<script lang="ts">
export default {
  name: 'LoadingSpinner'
}
</script>