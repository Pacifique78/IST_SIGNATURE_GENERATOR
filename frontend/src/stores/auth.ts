import { defineStore } from 'pinia'
import type { User, AuthResponse } from '@/types'
import { authService } from '@/services/authService'
import { userService } from '@/services/userService'

export const useAuthStore = defineStore('auth', {
  state: () => ({
    user: null as User | null,
    token: localStorage.getItem('token'),
    isAuthenticated: !!localStorage.getItem('token'),
    loading: true,
  }),

  getters: {
    isAdmin: (state) => state.user?.role === 'ADMIN',
  },

  actions: {
    async initializeAuth() {
      try {
        const token = localStorage.getItem('token')
        if (!token) {
          this.loading = false
          return
        }

        // Fetch user profile
        const user = await userService.getProfile()
        this.user = user
        this.isAuthenticated = true
      } catch (error) {
        // If token is invalid, clear auth state
        this.logout()
      } finally {
        this.loading = false
      }
    },
    async login(email: string, password: string) {
      const response = await authService.login(email, password)
      this.setAuthData(response)
      await this.fetchUserProfile()
    },

    async register(userData: {
      name: string
      email: string
      password: string
      phoneNumber?: string
    }) {
      return await authService.register(userData)
    },

    setAuthData(authData: AuthResponse) {
      this.token = authData.token
      this.isAuthenticated = true
      localStorage.setItem('token', authData.token)
    },

    async fetchUserProfile() {
      try {
        const user = await userService.getProfile()
        this.user = user
      } catch (error) {
        this.logout()
        throw error
      }
    },

    logout() {
      this.user = null
      this.token = null
      this.isAuthenticated = false
      localStorage.removeItem('token')
    },
  },
})
