<template>
  <div class="space-y-6">
    <div v-if="loading" class="flex justify-center py-12">
      <LoadingSpinner text="Loading users..." />
    </div>

    <template v-else>
      <!-- Users List -->
      <div class="overflow-x-auto">
        <table class="min-w-full divide-y divide-gray-200">
          <thead class="bg-gray-50">
            <tr>
              <th
                scope="col"
                class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"
              >
                Name
              </th>
              <th
                scope="col"
                class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"
              >
                Email
              </th>
              <th
                scope="col"
                class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"
              >
                Title
              </th>
              <th
                scope="col"
                class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"
              >
                Status
              </th>
              <th
                scope="col"
                class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"
              >
                Actions
              </th>
            </tr>
          </thead>
          <tbody class="bg-white divide-y divide-gray-200">
            <tr v-for="user in users" :key="user.id">
              <td class="px-6 py-4 whitespace-nowrap">
                {{ user.name }}
              </td>
              <td class="px-6 py-4 whitespace-nowrap">
                {{ user.email }}
              </td>
              <td class="px-6 py-4 whitespace-nowrap">
                <span v-if="editingUser?.id === user.id">
                  <input
                    v-model="editingUser.title"
                    type="text"
                    class="input-field"
                    @keyup.enter="updateUserTitle"
                    @keyup.esc="cancelEdit"
                  />
                </span>
                <span v-else>
                  {{ user.title || 'Not set' }}
                </span>
              </td>
              <td class="px-6 py-4 whitespace-nowrap">
                <span
                  class="px-2 inline-flex text-xs leading-5 font-semibold rounded-full"
                  :class="{
                    'bg-green-100 text-green-800': user.verified,
                    'bg-yellow-100 text-yellow-800': !user.verified,
                  }"
                >
                  {{ user.verified ? 'Verified' : 'Unverified' }}
                </span>
              </td>
              <td class="px-6 py-4 whitespace-nowrap text-sm">
                <template v-if="editingUser?.id === user.id">
                  <button
                    @click="updateUserTitle"
                    class="text-indigo-600 hover:text-indigo-900 mr-2"
                    :disabled="saving"
                  >
                    Save
                  </button>
                  <button
                    @click="cancelEdit"
                    class="text-gray-600 hover:text-gray-900"
                    :disabled="saving"
                  >
                    Cancel
                  </button>
                </template>
                <button
                  v-else
                  @click="startEdit(user)"
                  class="text-indigo-600 hover:text-indigo-900"
                >
                  Edit Title
                </button>
              </td>
            </tr>
          </tbody>
        </table>
      </div>

      <!-- Error Message -->
      <p v-if="error" class="error-text text-center">{{ error }}</p>
      <p v-if="success" class="text-green-600 text-center">{{ success }}</p>
    </template>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import type { User } from '../../types'
import { useAdminStore } from '../../stores/admin'
import LoadingSpinner from '../common/LoadingSpinner.vue'

const adminStore = useAdminStore()

const loading = ref(true)
const saving = ref(false)
const error = ref('')
const success = ref('')
const users = ref<User[]>([])
const editingUser = ref<User | null>(null)

const startEdit = (user: User) => {
  editingUser.value = { ...user }
}

const cancelEdit = () => {
  editingUser.value = null
  error.value = ''
}

const updateUserTitle = async () => {
  if (!editingUser.value) return

  saving.value = true
  error.value = ''
  success.value = ''

  try {
    await adminStore.updateUserTitle(editingUser.value.id, editingUser.value.title || '')

    // Update local user list
    const userIndex = users.value.findIndex((u) => u.id === editingUser.value?.id)
    if (userIndex !== -1 && editingUser.value) {
      users.value[userIndex] = { ...users.value[userIndex], title: editingUser.value.title }
    }

    success.value = 'User title updated successfully'
    editingUser.value = null
  } catch (err: unknown) {
    if (err && typeof err === 'object' && 'response' in err) {
      const apiError = err as { response?: { data?: { message?: string } } }
      error.value = apiError.response?.data?.message || 'Failed to update user title'
    } else {
      error.value = 'An unexpected error occurred'
    }
  } finally {
    saving.value = false
  }
}

onMounted(async () => {
  try {
    users.value = await adminStore.fetchUsers()
  } catch (err: unknown) {
    if (err && typeof err === 'object' && 'response' in err) {
      const apiError = err as { response?: { data?: { message?: string } } }
      error.value = apiError.response?.data?.message || 'Failed to load users'
    } else {
      error.value = 'Failed to load users'
    }
  } finally {
    loading.value = false
  }
})
</script>
<script lang="ts">
export default {
  name: 'UserManagement',
}
</script>
