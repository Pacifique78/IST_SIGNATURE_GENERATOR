<template>
  <div class="bg-white rounded-lg shadow p-6 space-y-6">
    <div class="border-b pb-4">
      <h2 class="text-lg font-medium text-gray-900 mb-4">Preview</h2>

      <!-- HTML Preview -->
      <div class="bg-gray-50 p-4 rounded-md mb-4">
        <h3 class="text-sm font-medium text-gray-700 mb-2">HTML Version</h3>
        <div class="border p-4 bg-white rounded" v-html="signature?.htmlContent"></div>
      </div>

      <!-- Plain Text Preview -->
      <div class="bg-gray-50 p-4 rounded-md">
        <h3 class="text-sm font-medium text-gray-700 mb-2">Plain Text Version</h3>
        <pre class="border p-4 bg-white rounded whitespace-pre-wrap font-mono text-sm">{{
          signature?.plainTextContent
        }}</pre>
      </div>
    </div>

    <!-- Action Buttons -->
    <div class="flex flex-wrap gap-4">
      <button @click="copyHtml" class="btn-primary flex items-center" :disabled="!signature">
        <DocumentDuplicateIcon class="h-5 w-5 mr-2" />
        Copy HTML
      </button>

      <button @click="copyPlainText" class="btn-primary flex items-center" :disabled="!signature">
        <DocumentDuplicateIcon class="h-5 w-5 mr-2" />
        Copy Plain Text
      </button>

      <button @click="openInOutlook" class="btn-primary flex items-center" :disabled="!signature">
        <ArrowTopRightOnSquareIcon class="h-5 w-5 mr-2" />
        Open in Outlook
      </button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { DocumentDuplicateIcon, ArrowTopRightOnSquareIcon } from '@heroicons/vue/24/solid'
import type { SignatureTemplate } from '../../types'

const props = defineProps<{
  signature?: SignatureTemplate
}>()

const emit = defineEmits<{
  (e: 'copied'): void
}>()

const copyHtml = async () => {
  if (props.signature?.htmlContent) {
    await navigator.clipboard.writeText(props.signature.htmlContent)
    emit('copied')
  }
}

const copyPlainText = async () => {
  if (props.signature?.plainTextContent) {
    await navigator.clipboard.writeText(props.signature.plainTextContent)
    emit('copied')
  }
}

const openInOutlook = () => {
  // Open Outlook signature settings in a new tab
  window.open('https://outlook.office.com/mail/options/accounts-category/signatures-subcategory', '_blank')
}
</script>
<script lang="ts">
// Add this separate script block with default export
export default {
  name: 'SignaturePreview',
}
</script>
