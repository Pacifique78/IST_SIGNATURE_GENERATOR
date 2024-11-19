<template>
  <div class="max-w-4xl mx-auto p-6">
    <div v-if="loading" class="flex justify-center">
      <div class="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-500"></div>
    </div>

    <div v-else class="space-y-8">
      <!-- Header -->
      <div>
        <h1 class="text-2xl font-bold text-gray-900">Email Signature Generator</h1>
        <p class="mt-2 text-gray-600">
          Create your professional email signature based on your profile information.
        </p>
      </div>

      <!-- User Information Summary -->
      <div class="bg-white rounded-lg shadow p-6">
        <h2 class="text-lg font-medium text-gray-900 mb-4">Your Information</h2>
        <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
          <div>
            <label class="form-label">Name</label>
            <p class="text-gray-900">{{ user?.name }}</p>
          </div>
          <div>
            <label class="form-label">Title</label>
            <p class="text-gray-900">{{ user?.title || 'Not set' }}</p>
            <p v-if="!user?.title" class="text-sm text-red-600 mt-1">
              Contact admin to set your title
            </p>
          </div>
          <div>
            <label class="form-label">Email</label>
            <p class="text-gray-900">{{ user?.email }}</p>
          </div>
          <div>
            <label class="form-label">Phone Number</label>
            <p class="text-gray-900">{{ user?.phoneNumber || 'Not set' }}</p>
          </div>
        </div>

        <!-- Company Information -->
        <div class="mt-6 pt-6 border-t">
          <h3 class="text-lg font-medium text-gray-900 mb-4">Company Information</h3>
          <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
            <div>
              <label class="form-label">Company Name</label>
              <p class="text-gray-900">{{ company?.name || 'Not available' }}</p>
            </div>
            <div>
              <label class="form-label">Website</label>
              <p class="text-gray-900">{{ company?.website || 'Not available' }}</p>
            </div>
            <div class="md:col-span-2">
              <label class="form-label">Mission Statement</label>
              <p class="text-gray-900">{{ company?.missionStatement || 'Not available' }}</p>
            </div>
          </div>
        </div>
      </div>

      <!-- Generate Button -->
      <div class="flex justify-center">
        <button
          @click="generateSignature"
          class="btn-primary flex items-center space-x-2"
          :disabled="generating || !canGenerate"
        >
          <DocumentCheckIcon v-if="!generating" class="h-5 w-5" />
          <div v-else class="animate-spin rounded-full h-5 w-5 border-b-2 border-white"></div>
          <span>{{ generating ? 'Generating...' : 'Generate Signature' }}</span>
        </button>
      </div>

      <!-- Error Message -->
      <p v-if="error" class="text-red-600 text-center">{{ error }}</p>

      <!-- Signature Preview -->
      <SignaturePreview v-if="signature" :signature="signature" @copied="showCopySuccess" />

      <!-- Success Toast -->
      <div
        v-if="showSuccessToast"
        class="fixed bottom-4 right-4 bg-green-500 text-white px-4 py-2 rounded-md shadow-lg"
      >
        Copied to clipboard!
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { storeToRefs } from 'pinia'
import { DocumentCheckIcon } from '@heroicons/vue/24/solid'
import { useAuthStore } from '../../stores/auth'
import { useCompanyStore } from '../../stores/company'
import { signatureService } from '../../services/signatureService'
import type { SignatureTemplate } from '../../types'
import SignaturePreview from './SignaturePreview.vue'

const authStore = useAuthStore()
const companyStore = useCompanyStore()

const { user } = storeToRefs(authStore)
const { company } = storeToRefs(companyStore)

console.log(user)
console.log(company)

const loading = ref(true)
const generating = ref(false)
const error = ref('')
const signature = ref<SignatureTemplate | null>(null)
const showSuccessToast = ref(false)

const canGenerate = computed(() => {
  return user.value?.title && company.value?.name
})

const generateSignature = async () => {
  if (!canGenerate.value) {
    error.value = 'Missing required information. Please ensure your title is set.'
    return
  }

  generating.value = true
  error.value = ''

  try {
    const result = await signatureService.generateSignature()
    signature.value = result
  } catch (err: any) {
    error.value = err.response?.data?.message || 'Failed to generate signature'
  } finally {
    generating.value = false
  }
}

const showCopySuccess = () => {
  showSuccessToast.value = true
  setTimeout(() => {
    showSuccessToast.value = false
  }, 2000)
}

onMounted(async () => {
  try {
    // Load user and company data in parallel
    await Promise.all([authStore.fetchUserProfile(), companyStore.fetchCompany()])
  } catch (err: any) {
    error.value = 'Failed to load required information'
  } finally {
    loading.value = false
  }
})
</script>
<script lang="ts">
// Add this separate script block with default export
export default {
  name: 'SignatureGenerator',
}
</script>
