import type { SignatureTemplate } from '@/types'
import apiClient from './api'

export const signatureService = {
  async generateSignature(): Promise<SignatureTemplate> {
    const response = await apiClient.get<SignatureTemplate>('/api/signature/generate')
    return response.data
  }
}