<template>
  <div class="max-w-2xl mx-auto p-6 bg-white rounded-lg shadow">
    <h2 class="text-2xl font-bold mb-6">Profile Information</h2>

    <div v-if="loading" class="flex justify-center">
      <div class="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-500"></div>
    </div>

    <form v-else @submit.prevent="updateProfile" class="space-y-6">
      <div>
        <label class="form-label">Name</label>
        <p class="input-field bg-gray-50">{{ user?.name }}</p>
      </div>

      <div>
        <label class="form-label">Email</label>
        <p class="input-field bg-gray-50">{{ user?.email }}</p>
        <span v-if="user?.verified" class="text-green-600 text-sm flex items-center mt-1">
          <CheckCircleIcon class="h-4 w-4 mr-1" />
          Verified
        </span>
      </div>

      <div>
        <label for="phoneNumber" class="form-label">Phone Number</label>
        <input
          id="phoneNumber"
          v-model="phoneNumber"
          type="tel"
          class="input-field"
          :disabled="updating"
        />
        <p v-if="errors.phoneNumber" class="error-text">{{ errors.phoneNumber }}</p>
      </div>

      <div>
        <label class="form-label">Job Title</label>
        <p class="input-field bg-gray-50">{{ user?.title || 'Not set' }}</p>
        <p class="text-sm text-gray-500 mt-1">Contact admin to update your title</p>
      </div>

      <div class="flex justify-end">
        <button type="submit" class="btn-primary" :disabled="updating">
          {{ updating ? 'Saving...' : 'Save Changes' }}
        </button>
      </div>

      <p v-if="error" class="error-text text-center">{{ error }}</p>
      <p v-if="success" class="text-green-600 text-center">{{ success }}</p>
    </form>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { CheckCircleIcon } from '@heroicons/vue/24/solid'
import { useAuthStore } from '../../stores/auth'
import { userService } from '../../services/userService'

const authStore = useAuthStore()
const { user } = authStore

const loading = ref(true)
const updating = ref(false)
const phoneNumber = ref(user?.phoneNumber || '')
const error = ref('')
const success = ref('')
const errors = ref({ phoneNumber: '' })

const updateProfile = async () => {
  updating.value = true
  error.value = ''
  success.value = ''
  errors.value = { phoneNumber: '' }

  try {
    await userService.updatePhoneNumber(phoneNumber.value)
    await authStore.fetchUserProfile() // Refresh user data
    success.value = 'Profile updated successfully'
  } catch (err: any) {
    if (err.response?.data?.validationErrors) {
      errors.value = err.response.data.validationErrors
    } else {
      error.value = err.response?.data?.message || 'Failed to update profile'
    }
  } finally {
    updating.value = false
  }
}

onMounted(async () => {
  try {
    await authStore.fetchUserProfile()
    phoneNumber.value = user?.phoneNumber || ''
  } catch (err) {
    error.value = 'Failed to load profile'
  } finally {
    loading.value = false
  }
})
</script>
<script lang="ts">
export default {
  name: 'UserProfile',
}
</script>
