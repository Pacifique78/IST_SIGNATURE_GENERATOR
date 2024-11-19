import { defineStore } from 'pinia'
import type { User } from '../types'
import apiClient from '../services/api'

export const useAdminStore = defineStore('admin', {
  state: () => ({
    loading: false,
    error: null as string | null,
  }),

  actions: {
    async fetchUsers(): Promise<User[]> {
      const response = await apiClient.get<User[]>('/api/admin/users')
      return response.data
    },

    async updateUserTitle(userId: string, title: string): Promise<void> {
      await apiClient.put(`/api/admin/users/${userId}/title`, { title })
    },
  },
})
