import { defineStore } from 'pinia'
import { ref } from 'vue'
import { signatureService } from '@/services/signatureService'

export const useSignatureStore = defineStore('signature', () => {
  const signatureHtml = ref<string>('')
  const loading = ref(false)
  const error = ref<string | null>(null)

  const generateUserSignature = async () => {
    try {
      loading.value = true
      error.value = null
      const data = await signatureService.generateSignature()
      signatureHtml.value = data.signatureHtml
      return true
    } catch (err: any) {
      error.value = err.response?.data?.message || 'Failed to generate signature'
      return false
    } finally {
      loading.value = false
    }
  }

  const openOutlookSignatures = () => {
    window.open('https://outlook.office.com/mail/options/accounts-category/signatures-subcategory', '_blank')
  }

  return {
    signatureHtml,
    loading,
    error,
    generateUserSignature,
    openOutlookSignatures
  }
})