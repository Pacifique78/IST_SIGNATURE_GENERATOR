<template>
  <form @submit.prevent="handleSubmit" class="space-y-6">
    <div>
      <label for="email" class="form-label">Email address</label>
      <input
        id="email"
        v-model="email"
        type="email"
        required
        class="input-field"
        :disabled="loading"
      />
      <p v-if="errors.email" class="error-text">{{ errors.email }}</p>
    </div>

    <div>
      <label for="password" class="form-label">Password</label>
      <input
        id="password"
        v-model="password"
        type="password"
        required
        class="input-field"
        :disabled="loading"
      />
      <p v-if="errors.password" class="error-text">{{ errors.password }}</p>
    </div>

    <div>
      <button type="submit" class="btn-primary w-full" :disabled="loading">
        {{ loading ? 'Signing in...' : 'Sign in' }}
      </button>
    </div>

    <p v-if="error" class="error-text text-center">{{ error }}</p>
  </form>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '../../stores'

const router = useRouter()
const authStore = useAuthStore()

const email = ref('')
const password = ref('')
const loading = ref(false)
const error = ref('')
const errors = ref({
  email: '',
  password: '',
})

const handleSubmit = async () => {
  // Reset errors
  error.value = ''
  errors.value = { email: '', password: '' }

  try {
    loading.value = true
    await authStore.login(email.value, password.value)
    router.push({ name: 'generator' })
  } catch (err: any) {
    if (err.response?.data?.validationErrors) {
      errors.value = err.response.data.validationErrors
    } else {
      error.value = err.response?.data?.message || 'An error occurred during login'
    }
  } finally {
    loading.value = false
  }
}
</script>
<script lang="ts">
export default {
  name: 'LoginForm',
}
</script>
