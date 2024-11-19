<template>
  <div>
    <form v-show="!isSuccess" @submit.prevent="handleSubmit" class="space-y-6">
      <div>
        <label for="name" class="form-label">Full Name</label>
        <input
          id="name"
          v-model="formData.name"
          type="text"
          required
          class="input-field"
          :disabled="loading"
        />
        <p v-if="errors.name" class="error-text">{{ errors.name }}</p>
      </div>

      <div>
        <label for="email" class="form-label">Email address</label>
        <input
          id="email"
          v-model="formData.email"
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
          v-model="formData.password"
          type="password"
          required
          class="input-field"
          :disabled="loading"
        />
        <p v-if="errors.password" class="error-text">{{ errors.password }}</p>
      </div>

      <div>
        <label for="phoneNumber" class="form-label">Phone Number (Optional)</label>
        <input
          id="phoneNumber"
          v-model="formData.phoneNumber"
          type="tel"
          class="input-field"
          :disabled="loading"
        />
        <p v-if="errors.phoneNumber" class="error-text">{{ errors.phoneNumber }}</p>
      </div>

      <div>
        <button type="submit" class="btn-primary w-full" :disabled="loading">
          {{ loading ? 'Creating account...' : 'Create account' }}
        </button>
      </div>

      <p v-if="error" class="error-text text-center">{{ error }}</p>
    </form>
    <form v-show="isSuccess" @submit.prevent="navigateToLogin" class="flex flex-col items-center space-y-6">
      <p class="text-center">Account created successfuly, Please check your email to verify your account!!</p>
    </form>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '../../stores'

const router = useRouter()
const authStore = useAuthStore()

const formData = reactive({
  name: '',
  email: '',
  password: '',
  phoneNumber: '',
})

const isSuccess = ref(false)
const loading = ref(false)
const error = ref('')
const errors = ref({
  name: '',
  email: '',
  password: '',
  phoneNumber: '',
})

const handleSubmit = async () => {
  error.value = ''
  errors.value = { name: '', email: '', password: '', phoneNumber: '' }

  try {
    loading.value = true
    await authStore.register(formData)
    isSuccess.value = true
  } catch (err: any) {
    if (err.response?.data?.validationErrors) {
      errors.value = err.response.data.validationErrors
    } else {
      error.value = err.response?.data?.message || 'An error occurred during registration'
    }
  } finally {
    loading.value = false
  }
}
const navigateToLogin = () => {
  router.push({
    name: 'login',
    query: { message: 'Please check your email to verify your account' },
  })
}
</script>
<script lang="ts">
export default {
  name: 'RegisterForm',
}
</script>
