<template>
  <form @submit.prevent="handleSubmit" class="space-y-6">
    <div v-if="loading" class="flex justify-center py-12">
      <LoadingSpinner text="Loading company details..." />
    </div>

    <template v-else>
      <div>
        <label for="name" class="form-label">Company Name</label>
        <input
          id="name"
          v-model="formData.name"
          type="text"
          required
          class="input-field"
          :disabled="saving"
        />
        <p v-if="errors.name" class="error-text">{{ errors.name }}</p>
      </div>

      <div>
        <label for="missionStatement" class="form-label">Mission Statement</label>
        <textarea
          id="missionStatement"
          v-model="formData.missionStatement"
          rows="3"
          class="input-field"
          :disabled="saving"
        ></textarea>
        <p v-if="errors.missionStatement" class="error-text">{{ errors.missionStatement }}</p>
      </div>

      <div>
        <label for="address" class="form-label">Address</label>
        <input
          id="address"
          v-model="formData.address"
          type="text"
          class="input-field"
          :disabled="saving"
        />
        <p v-if="errors.address" class="error-text">{{ errors.address }}</p>
      </div>

      <div>
        <label for="website" class="form-label">Website</label>
        <input
          id="website"
          v-model="formData.website"
          type="url"
          required
          class="input-field"
          :disabled="saving"
          placeholder="https://"
        />
        <p v-if="errors.website" class="error-text">{{ errors.website }}</p>
      </div>

      <div class="flex justify-end">
        <button type="submit" class="btn-primary flex items-center" :disabled="saving">
          <span v-if="saving" class="mr-2">
            <LoadingSpinner light />
          </span>
          {{ saving ? 'Saving...' : 'Save Changes' }}
        </button>
      </div>

      <p v-if="error" class="error-text text-center">{{ error }}</p>
      <p v-if="success" class="text-green-600 text-center">{{ success }}</p>
    </template>
  </form>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import type { Company } from '../../types'
import { useCompanyStore } from '../../stores/company'
import LoadingSpinner from '../common/LoadingSpinner.vue'

const companyStore = useCompanyStore()

const loading = ref(true)
const saving = ref(false)
const error = ref('')
const success = ref('')
const errors = ref({
  name: '',
  missionStatement: '',
  address: '',
  website: '',
})

const formData = ref<Partial<Company>>({
  name: '',
  missionStatement: '',
  address: '',
  website: '',
})

const handleSubmit = async () => {
  saving.value = true
  error.value = ''
  success.value = ''
  errors.value = {
    name: '',
    missionStatement: '',
    address: '',
    website: '',
  }

  try {
    await companyStore.updateCompany(formData.value)
    success.value = 'Company details updated successfully'
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

onMounted(async () => {
  try {
    await companyStore.fetchCompany()
    if (companyStore.company) {
      formData.value = { ...companyStore.company }
    }
  } catch (err) {
    error.value = 'Failed to load company details'
  } finally {
    loading.value = false
  }
})
</script>
<script lang="ts">
export default {
  name: 'CompanyForm',
}
</script>
