import type { AuthResponse } from '@/types'
import apiClient from './api'

export const authService = {
  async login(email: string, password: string): Promise<AuthResponse> {
    const response = await apiClient.post<AuthResponse>('/api/auth/login', {
      email,
      password
    })
    return response.data
  },

  async register(data: {
    name: string
    email: string
    password: string
    phoneNumber?: string
  }): Promise<AuthResponse> {
    const response = await apiClient.post<AuthResponse>('/api/auth/signup', data)
    return response.data
  },

  async verifyEmail(token: string): Promise<{ message: string }> {
    const response = await apiClient.post('/api/auth/verify-email', { token })
    return response.data
  },
  
  async resendVerification(email: string): Promise<{ message: string }> {
    const response = await apiClient.post('/api/auth/resend-verification', null, {
      params: { email }
    })
    return response.data
  }
}
