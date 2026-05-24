import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  { path: '/', redirect: '/home' },
  { path: '/home', name: 'home', component: () => import('../views/HomeView.vue') },
  { path: '/product', name: 'product', component: () => import('../views/ProductView.vue') },
  { path: '/cart', name: 'cart', component: () => import('../views/CartView.vue') },
  { path: '/checkout', name: 'checkout', component: () => import('../views/CheckoutView.vue') },
  { path: '/order', name: 'order', component: () => import('../views/OrderView.vue') },
  { path: '/aftersales', name: 'aftersales', component: () => import('../views/AftersalesView.vue') },
  { path: '/profile', name: 'profile', component: () => import('../views/ProfileView.vue') },
]

export default createRouter({
  history: createWebHistory(),
  routes,
})
