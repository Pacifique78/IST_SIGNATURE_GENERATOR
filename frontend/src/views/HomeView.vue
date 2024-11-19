<template>
  <div class="relative">
    <!-- Hero Section -->
    <div class="relative mx-auto max-w-7xl px-4 sm:px-6 lg:px-8 py-24 text-center">
      <h1 class="text-4xl font-bold tracking-tight text-gray-900 sm:text-5xl md:text-6xl">
        <span class="block">Generate Professional</span>
        <span class="block text-indigo-600">Email Signatures</span>
      </h1>
      <p class="mx-auto mt-3 max-w-md text-base text-gray-500 sm:text-lg md:mt-5 md:max-w-3xl md:text-xl">
        Create consistent and professional email signatures for your organization. Get started in minutes.
      </p>
      <div class="mt-10 flex justify-center gap-x-6">
        <router-link
          v-if="!isAuthenticated"
          to="/auth/register"
          class="rounded-md bg-indigo-600 px-3.5 py-2.5 text-sm font-semibold text-white shadow-sm hover:bg-indigo-500 focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-indigo-600"
        >
          Get Started
        </router-link>
        <router-link
          v-else
          to="/generator"
          class="rounded-md bg-indigo-600 px-3.5 py-2.5 text-sm font-semibold text-white shadow-sm hover:bg-indigo-500 focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-indigo-600"
        >
          Generate Signature
        </router-link>
      </div>
    </div>

    <!-- Features Section -->
    <div class="bg-white py-24">
      <div class="mx-auto max-w-7xl px-4 sm:px-6 lg:px-8">
        <div class="grid grid-cols-1 gap-8 sm:grid-cols-2 lg:grid-cols-3">
          <!-- Feature 1 -->
          <div class="relative">
            <dt>
              <div class="absolute flex h-12 w-12 items-center justify-center rounded-md bg-indigo-500 text-white">
                <DocumentCheckIcon class="h-6 w-6" aria-hidden="true" />
              </div>
              <p class="ml-16 text-lg font-semibold leading-8 text-gray-900">Professional Design</p>
            </dt>
            <dd class="ml-16 mt-2 text-base text-gray-600">
              Create professional email signatures that match your brand identity.
            </dd>
          </div>

          <!-- Feature 2 -->
          <div class="relative">
            <dt>
              <div class="absolute flex h-12 w-12 items-center justify-center rounded-md bg-indigo-500 text-white">
                <UserGroupIcon class="h-6 w-6" aria-hidden="true" />
              </div>
              <p class="ml-16 text-lg font-semibold leading-8 text-gray-900">Team Management</p>
            </dt>
            <dd class="ml-16 mt-2 text-base text-gray-600">
              Manage and update signatures for your entire team from one place.
            </dd>
          </div>

          <!-- Feature 3 -->
          <div class="relative">
            <dt>
              <div class="absolute flex h-12 w-12 items-center justify-center rounded-md bg-indigo-500 text-white">
                <ArrowPathIcon class="h-6 w-6" aria-hidden="true" />
              </div>
              <p class="ml-16 text-lg font-semibold leading-8 text-gray-900">Easy Updates</p>
            </dt>
            <dd class="ml-16 mt-2 text-base text-gray-600">
              Update your signature information quickly and easily whenever needed.
            </dd>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { DocumentCheckIcon, UserGroupIcon, ArrowPathIcon } from '@heroicons/vue/24/outline'
import { useAuthStore } from '../stores/auth'

const { isAuthenticated } = useAuthStore()
</script>
