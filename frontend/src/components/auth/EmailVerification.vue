<template>
  <div class="text-center">
    <div v-if="loading" class="space-y-4">
      <div class="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-500 mx-auto"></div>
      <p class="text-gray-600">Verifying your email...</p>
    </div>

    <div v-else-if="verified" class="space-y-4">
      <div class="text-green-500">
        <CheckCircleIcon class="h-12 w-12 mx-auto" />
      </div>
      <h2 class="text-2xl font-bold text-gray-900">Email Verified!</h2>
      <p class="text-gray-600">Your email has been successfully verified.</p>
      <router-link :to="{ name: 'login' }" class="btn-primary inline-block">
        Proceed to Login
      </router-link>
    </div>

    <div v-else class="space-y-4">
      <div class="text-red-500">
        <XCircleIcon class="h-12 w-12 mx-auto" />
      </div>
      <h2 class="text-2xl font-bold text-gray-900">Verification Failed</h2>
      <p class="text-gray-600">{{ error }}</p>
      <button @click="resendVerification" class="btn-primary" :disabled="loading">
        Resend Verification Email
      </button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { CheckCircleIcon, XCircleIcon } from '@heroicons/vue/24/solid'
import { authService } from '../../services/authService'

const route = useRoute()
const loading = ref(true)
const verified = ref(false)
const error = ref('')
const email = ref(route.query.email as string) // Get email from query params

const verifyEmail = async (token: string) => {
  try {
    await authService.verifyEmail(token)
    verified.value = true
  } catch (err: unknown) {
    if (err && typeof err === 'object' && 'response' in err) {
      const apiError = err as { response?: { data?: { message?: string } } }
      error.value = apiError.response?.data?.message || 'Verification failed'
    } else {
      error.value = 'Verification failed'
    }
  } finally {
    loading.value = false
  }
}

const resendVerification = async () => {
  if (!email.value) {
    error.value = 'Email address is required for resending verification'
    return
  }

  try {
    loading.value = true
    await authService.resendVerification(email.value)
    error.value = 'A new verification email has been sent'
  } catch (err: unknown) {
    if (err && typeof err === 'object' && 'response' in err) {
      const apiError = err as { response?: { data?: { message?: string } } }
      error.value = apiError.response?.data?.message || 'Failed to resend verification email'
    } else {
      error.value = 'Failed to resend verification email'
    }
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  const token = route.query.token as string
  if (token) {
    verifyEmail(token)
  } else {
    loading.value = false
    error.value = 'Invalid verification link'
  }
})
</script>
<script lang="ts">
export default {
  name: 'EmailVerification',
}
</script>
