<template>
  <div class="min-h-screen bg-gray-50">
    <AppHeader />

    <div class="py-6">
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <h1 class="text-2xl font-semibold text-gray-900">{{ pageTitle }}</h1>
      </div>
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <!-- Side Navigation -->
        <div class="grid grid-cols-1 gap-6 lg:grid-cols-4 lg:gap-8 mt-6">
          <div class="lg:col-span-1">
            <nav class="space-y-1">
              <router-link
                v-for="item in navigation"
                :key="item.name"
                :to="item.to"
                :class="[
                  route.name === item.routeName
                    ? 'bg-indigo-50 text-indigo-700'
                    : 'text-gray-900 hover:bg-gray-50',
                  'group rounded-md px-3 py-2 flex items-center text-sm font-medium',
                ]"
              >
                <component
                  :is="item.icon"
                  :class="[
                    route.name === item.routeName
                      ? 'text-indigo-500'
                      : 'text-gray-400 group-hover:text-gray-500',
                    'flex-shrink-0 -ml-1 mr-3 h-6 w-6',
                  ]"
                  aria-hidden="true"
                />
                <span class="truncate">{{ item.name }}</span>
              </router-link>
            </nav>
          </div>

          <!-- Main Content -->
          <div class="lg:col-span-3">
            <div class="bg-white shadow sm:rounded-lg">
              <div class="px-4 py-5 sm:p-6">
                <router-view></router-view>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <AppFooter />
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { useRoute } from 'vue-router'
import { BuildingOfficeIcon, UsersIcon } from '@heroicons/vue/24/outline'
import AppHeader from '../components/common/AppHeader.vue'
import AppFooter from '../components/common/AppFooter.vue'

const route = useRoute()

const navigation = [
  {
    name: 'Company Settings',
    to: '/admin/company',
    routeName: 'admin-company',
    icon: BuildingOfficeIcon,
  },
  {
    name: 'User Management',
    to: '/admin/users',
    routeName: 'admin-users',
    icon: UsersIcon,
  },
]

const pageTitle = computed(() => {
  const currentRoute = navigation.find((item) => item.routeName === route.name)
  return currentRoute?.name || 'Admin Dashboard'
})
</script>
