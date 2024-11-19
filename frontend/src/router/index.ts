import { createRouter, createWebHistory } from 'vue-router'
import { useAuthStore } from '../stores/auth'
import { waitFor } from '@/utils/async'

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/',
      component: () => import('../layouts/DefaultLayout.vue'),
      children: [
        {
          path: '',
          name: 'home',
          component: () => import('../views/HomeView.vue'),
        },
        {
          path: 'generator',
          name: 'generator',
          component: () => import('../views/signature/GeneratorView.vue'),
          meta: { requiresAuth: true },
        },
        {
          path: 'profile',
          name: 'profile',
          component: () => import('../views/ProfileView.vue'),
          meta: { requiresAuth: true },
        },
      ],
    },
    {
      path: '/auth',
      component: () => import('../layouts/AuthLayout.vue'),
      children: [
        {
          path: 'login',
          name: 'login',
          component: () => import('../views/auth/LoginView.vue'),
          meta: { guest: true },
        },
        {
          path: 'register',
          name: 'register',
          component: () => import('../views/auth/RegisterView.vue'),
          meta: { guest: true },
        },
        {
          path: 'verify-email',
          name: 'verify-email',
          component: () => import('../views/auth/VerifyEmailView.vue'),
        },
      ],
    },
    {
      path: '/admin',
      component: () => import('../layouts/AdminLayout.vue'),
      meta: { requiresAuth: true, requiresAdmin: true },
      children: [
        {
          path: 'company',
          name: 'admin-company',
          component: () => import('../views/admin/CompanySettingsView.vue'),
        },
        {
          path: 'users',
          name: 'admin-users',
          component: () => import('../views/admin/UserManagementView.vue'),
        },
      ],
    },
    {
      path: '/:pathMatch(.*)*',
      name: 'not-found',
      component: () => import('../views/NotFoundView.vue'),
    },
  ],
})

// Navigation guards

router.beforeEach(async (to, from, next) => {
  const authStore = useAuthStore()

  // Wait for auth initialization
  if (authStore.loading) {
    // You might want to show a loading state here
    await waitFor(() => !authStore.loading)
  }
  
  // Check if route requires authentication
  if (to.meta.requiresAuth && !authStore.isAuthenticated) {
    return next({
      name: 'login',
      query: { 
        redirect: to.fullPath,
        error: 'Please log in to access this page'
      }
    })
  }

  // Check if route requires admin role
  if (to.meta.requiresAdmin && !authStore.isAdmin) {
    return next({
      name: 'home',
      query: { error: 'You do not have permission to access this page' }
    })
  }

  // Prevent authenticated users from accessing guest routes
  if (to.meta.guest && authStore.isAuthenticated) {
    return next({ name: 'home' })
  }

  next()
})

export default router
