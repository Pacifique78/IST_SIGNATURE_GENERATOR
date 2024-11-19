import type { User } from '@/types'
import apiClient from './api'

export const userService = {
  async getProfile(): Promise<User> {
    const response = await apiClient.get<User>('/api/users/profile')
    return response.data
  },

  async updatePhoneNumber(phoneNumber: string): Promise<void> {
    await apiClient.put('/api/users/phone-number', { phoneNumber })
  }
}