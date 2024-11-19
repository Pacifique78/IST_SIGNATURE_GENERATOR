<template>
  <header class="bg-white shadow">
    <nav class="mx-auto px-4 sm:px-6 lg:px-8" aria-label="Top">
      <div
        class="w-full py-6 flex items-center justify-between border-b border-indigo-500 lg:border-none"
      >
        <div class="flex items-center">
          <router-link to="/" class="text-2xl font-bold text-gray-900"> IST Signature </router-link>

          <!-- Navigation Links -->
          <div class="hidden ml-10 space-x-8 lg:block">
            <router-link
              v-if="isAuthenticated"
              to="/generator"
              class="text-base font-medium text-gray-500 hover:text-gray-900"
            >
              Generate Signature
            </router-link>

            <router-link
              v-if="isAuthenticated"
              to="/profile"
              class="text-base font-medium text-gray-500 hover:text-gray-900"
            >
              Profile
            </router-link>

            <template v-if="isAdmin">
              <router-link
                to="/admin/company"
                class="text-base font-medium text-gray-500 hover:text-gray-900"
              >
                Company Settings
              </router-link>
              <router-link
                to="/admin/users"
                class="text-base font-medium text-gray-500 hover:text-gray-900"
              >
                User Management
              </router-link>
            </template>
          </div>
        </div>

        <!-- Auth Buttons -->
        <div class="ml-10 space-x-4">
          <template v-if="isAuthenticated">
            <button
              @click="handleLogout"
              class="inline-flex items-center px-4 py-2 border border-transparent text-sm font-medium rounded-md text-indigo-700 bg-indigo-100 hover:bg-indigo-200"
            >
              Logout
            </button>
          </template>
          <template v-else>
            <router-link
              to="/auth/login"
              class="inline-flex items-center px-4 py-2 border border-transparent text-sm font-medium rounded-md text-indigo-700 bg-indigo-100 hover:bg-indigo-200"
            >
              Sign in
            </router-link>
            <router-link
              to="/auth/register"
              class="inline-flex items-center px-4 py-2 border border-transparent text-sm font-medium rounded-md text-white bg-indigo-600 hover:bg-indigo-700"
            >
              Sign up
            </router-link>
          </template>
        </div>

        <!-- Mobile menu button -->
        <div class="flex lg:hidden">
          <button
            type="button"
            class="-m-2.5 inline-flex items-center justify-center rounded-md p-2.5 text-gray-700"
            @click="mobileMenuOpen = true"
          >
            <span class="sr-only">Open main menu</span>
            <Bars3Icon class="h-6 w-6" aria-hidden="true" />
          </button>
        </div>
      </div>

      <!-- Mobile menu -->
      <DialogModal :open="mobileMenuOpen" @close="mobileMenuOpen = false">
        <div class="fixed inset-0 z-50" />
        <Dialog.Panel
          class="fixed inset-y-0 right-0 z-50 w-full overflow-y-auto bg-white px-6 py-6 sm:max-w-sm sm:ring-1 sm:ring-gray-900/10"
        >
          <div class="flex items-center justify-between">
            <router-link to="/" class="-m-1.5 p-1.5">
              <span class="text-2xl font-bold text-gray-900">IST Signature</span>
            </router-link>
            <button
              type="button"
              class="-m-2.5 rounded-md p-2.5 text-gray-700"
              @click="mobileMenuOpen = false"
            >
              <span class="sr-only">Close menu</span>
              <XMarkIcon class="h-6 w-6" aria-hidden="true" />
            </button>
          </div>
          <div class="mt-6 flow-root">
            <div class="-my-6 divide-y divide-gray-500/10">
              <div class="space-y-2 py-6">
                <template v-if="isAuthenticated">
                  <router-link
                    to="/generator"
                    class="-mx-3 block rounded-lg px-3 py-2 text-base font-semibold leading-7 text-gray-900 hover:bg-gray-50"
                    @click="mobileMenuOpen = false"
                  >
                    Generate Signature
                  </router-link>
                  <router-link
                    to="/profile"
                    class="-mx-3 block rounded-lg px-3 py-2 text-base font-semibold leading-7 text-gray-900 hover:bg-gray-50"
                    @click="mobileMenuOpen = false"
                  >
                    Profile
                  </router-link>
                  <template v-if="isAdmin">
                    <router-link
                      to="/admin/company"
                      class="-mx-3 block rounded-lg px-3 py-2 text-base font-semibold leading-7 text-gray-900 hover:bg-gray-50"
                      @click="mobileMenuOpen = false"
                    >
                      Company Settings
                    </router-link>
                    <router-link
                      to="/admin/users"
                      class="-mx-3 block rounded-lg px-3 py-2 text-base font-semibold leading-7 text-gray-900 hover:bg-gray-50"
                      @click="mobileMenuOpen = false"
                    >
                      User Management
                    </router-link>
                  </template>
                  <button
                    @click="handleLogout"
                    class="-mx-3 block rounded-lg px-3 py-2.5 text-base font-semibold leading-7 text-gray-900 hover:bg-gray-50"
                  >
                    Logout
                  </button>
                </template>
                <template v-else>
                  <router-link
                    to="/auth/login"
                    class="-mx-3 block rounded-lg px-3 py-2 text-base font-semibold leading-7 text-gray-900 hover:bg-gray-50"
                    @click="mobileMenuOpen = false"
                  >
                    Sign in
                  </router-link>
                  <router-link
                    to="/auth/register"
                    class="-mx-3 block rounded-lg px-3 py-2.5 text-base font-semibold leading-7 text-gray-900 hover:bg-gray-50"
                    @click="mobileMenuOpen = false"
                  >
                    Sign up
                  </router-link>
                </template>
              </div>
            </div>
          </div>
        </Dialog.Panel>
      </DialogModal>
    </nav>
  </header>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'
import { Bars3Icon, XMarkIcon } from '@heroicons/vue/24/outline'
import { Dialog } from '@headlessui/vue'
import { useAuthStore } from '../../stores/auth'
import DialogModal from './DialogModal.vue'

const router = useRouter()
const authStore = useAuthStore()
const mobileMenuOpen = ref(false)

const isAuthenticated = computed(() => authStore.isAuthenticated)
const isAdmin = computed(() => authStore.isAdmin)

const handleLogout = async () => {
  mobileMenuOpen.value = false
  authStore.logout()
  router.push('/auth/login')
}
</script>
<script lang="ts">
export default {
  name: 'AppHeader'
}
</script>