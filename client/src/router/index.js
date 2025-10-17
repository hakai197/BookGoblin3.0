import { createRouter, createWebHistory } from 'vue-router'
import Home from '../views/HomeView.vue'
import Login from '../views/LoginView.vue'
import Register from '../views/RegisterView.vue'
import Dashboard from '../views/DashboardView.vue'
import Library from '../views/LibraryView.vue'
import ReadingLog from '../views/ReadingLog.vue'
import Discover from '../views/DiscoverView.vue'
import Account from '../views/AccountView.vue'

const routes = [
  {
    path: '/',
    name: 'home',
    component: Home
  },
  {
    path: '/login',
    name: 'login',
    component: Login
  },
  {
    path: '/register',
    name: 'register',
    component: Register
  },
  {
    path: '/dashboard',
    name: 'dashboard',
    component: Dashboard,
    meta: { requiresAuth: true }
  },
  {
    path: '/library',
    name: 'library',
    component: Library,
    meta: { requiresAuth: true }
  },
  {
    path: '/reading-log',
    name: 'reading-log',
    component: ReadingLog,
    meta: { requiresAuth: true }
  },
  {
    path: '/discover',
    name: 'discover',
    component: Discover,
    meta: { requiresAuth: true }
  },
  {
    path: '/account',
    name: 'account',
    component: Account,
    meta: { requiresAuth: true }
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  const isAuthenticated = localStorage.getItem('token')
  
  if (to.meta.requiresAuth && !isAuthenticated) {
    next('/login')
  } else {
    next()
  }
})

export default router