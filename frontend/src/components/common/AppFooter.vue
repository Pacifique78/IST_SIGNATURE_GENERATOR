<template>
  <footer class="bg-white">
    <div class="mx-auto max-w-7xl overflow-hidden px-6 py-8 sm:py-12 lg:px-8">
      <p class="mt-4 text-center text-xs leading-5 text-gray-500">
        &copy; {{ currentYear }} IST Africa. All rights reserved.
      </p>
    </div>
  </footer>
</template>

<script setup lang="ts">
const currentYear = new Date().getFullYear()
</script>
<script lang="ts">
export default {
  name: 'AppFooter'
}
</script>