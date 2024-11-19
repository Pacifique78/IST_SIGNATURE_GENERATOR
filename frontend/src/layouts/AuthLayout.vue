<template>
  <div class="min-h-screen bg-gray-50 flex flex-col justify-center py-12 sm:px-6 lg:px-8">
    <div class="sm:mx-auto sm:w-full sm:max-w-md">
      <router-link to="/" class="flex justify-center">
        <h2 class="text-center text-3xl font-bold tracking-tight text-gray-900">IST Signature</h2>
      </router-link>
    </div>

    <!-- Alert -->
    <div v-if="alert" class="mt-8 sm:mx-auto sm:w-full sm:max-w-md">
      <div
        class="rounded-md p-4 mx-4"
        :class="{
          'bg-green-50': alert.type === 'success',
          'bg-red-50': alert.type === 'error',
          'bg-blue-50': alert.type === 'info',
        }"
      >
        <div class="flex">
          <div class="flex-shrink-0">
            <CheckCircleIcon
              v-if="alert.type === 'success'"
              class="h-5 w-5 text-green-400"
              aria-hidden="true"
            />
            <XCircleIcon
              v-if="alert.type === 'error'"
              class="h-5 w-5 text-red-400"
              aria-hidden="true"
            />
            <InformationCircleIcon
              v-if="alert.type === 'info'"
              class="h-5 w-5 text-blue-400"
              aria-hidden="true"
            />
          </div>
          <div class="ml-3">
            <p
              class="text-sm font-medium"
              :class="{
                'text-green-800': alert.type === 'success',
                'text-red-800': alert.type === 'error',
                'text-blue-800': alert.type === 'info',
              }"
            >
              {{ alert.message }}
            </p>
          </div>
          <div class="ml-auto pl-3">
            <div class="-mx-1.5 -my-1.5">
              <button
                type="button"
                @click="clearAlert"
                class="inline-flex rounded-md p-1.5"
                :class="{
                  'bg-green-50 text-green-500 hover:bg-green-100': alert.type === 'success',
                  'bg-red-50 text-red-500 hover:bg-red-100': alert.type === 'error',
                  'bg-blue-50 text-blue-500 hover:bg-blue-100': alert.type === 'info',
                }"
              >
                <span class="sr-only">Dismiss</span>
                <XMarkIcon class="h-5 w-5" aria-hidden="true" />
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Main Content -->
    <div class="mt-8 sm:mx-auto sm:w-full sm:max-w-md">
      <div class="bg-white py-8 px-4 shadow sm:rounded-lg sm:px-10">
        <router-view></router-view>
      </div>
    </div>

    <!-- Links -->
    <div class="mt-6 sm:mx-auto sm:w-full sm:max-w-md text-center">
      <template v-if="route.name === 'login'">
        <p class="text-sm text-gray-600">
          Don't have an account?
          <router-link
            to="/auth/register"
            class="font-medium text-indigo-600 hover:text-indigo-500"
          >
            Register here
          </router-link>
        </p>
      </template>
      <template v-else-if="route.name === 'register'">
        <p class="text-sm text-gray-600">
          Already have an account?
          <router-link to="/auth/login" class="font-medium text-indigo-600 hover:text-indigo-500">
            Sign in
          </router-link>
        </p>
      </template>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, watch } from 'vue'
import { useRoute } from 'vue-router'
import {
  CheckCircleIcon,
  XCircleIcon,
  InformationCircleIcon,
  XMarkIcon,
} from '@heroicons/vue/24/solid'

const route = useRoute()

interface Alert {
  type: 'success' | 'error' | 'info'
  message: string
}

const alert = ref<Alert | null>(null)

const showAlert = (type: Alert['type'], message: string) => {
  alert.value = { type, message }
  // Auto dismiss after 5 seconds
  setTimeout(() => {
    if (alert.value?.message === message) {
      clearAlert()
    }
  }, 5000)
}

const clearAlert = () => {
  alert.value = null
}

watch(
  () => route.query,
  (query) => {
    if (query.message) {
      showAlert('info', query.message as string)
    }
    if (query.error) {
      showAlert('error', query.error as string)
    }
    if (query.success) {
      showAlert('success', query.success as string)
    }
  },
  { immediate: true },
)
</script>
