import { defineStore } from 'pinia'
import type { Company } from '@/types'
import apiClient from '@/services/api'

export const useCompanyStore = defineStore('company', {
  state: () => ({
    company: null as Company | null,
    loading: false,
    error: null as string | null,
  }),

  actions: {
    async fetchCompany() {
      this.loading = true
      try {
        const response = await apiClient.get<Company>('/api/users/company')
        this.company = response.data
        console.log("COMPANY: ", response.data)
      } catch (error) {
        this.error = 'Failed to fetch company details'
        throw error
      } finally {
        this.loading = false
      }
    },

    async updateCompany(data: Partial<Company>) {
      this.loading = true
      try {
        const response = await apiClient.put<Company>('/api/admin/company', data)
        this.company = response.data
      } catch (error) {
        this.error = 'Failed to update company details'
        throw error
      } finally {
        this.loading = false
      }
    },
  },
})
