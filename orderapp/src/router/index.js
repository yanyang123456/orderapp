import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  { path: '/', redirect: '/home' },
  { path: '/home', name: 'home', component: () => import('../views/HomeView.vue'), meta: { title: '工作台' } },
  { path: '/wallet', name: 'wallet', component: () => import('../views/WalletView.vue'), meta: { title: '钱包' } },
  { path: '/products', name: 'products', component: () => import('../views/ProductView.vue'), meta: { title: '商品' } },
  { path: '/quote', name: 'quote', component: () => import('../views/QuoteView.vue'), meta: { title: '报价' } },
  { path: '/fulfillment', name: 'fulfillment', component: () => import('../views/FulfillmentView.vue'), meta: { title: '履约' } },
  { path: '/installation', name: 'installation', component: () => import('../views/InstallationView.vue'), meta: { title: '安装' } },
  { path: '/aftersales', name: 'aftersales', component: () => import('../views/AftersalesView.vue'), meta: { title: '售后' } },
  { path: '/review', name: 'review', component: () => import('../views/ReviewView.vue'), meta: { title: '审核' } },
  { path: '/analytics', name: 'analytics', component: () => import('../views/AnalyticsView.vue'), meta: { title: '数据' } },
]

const router = createRouter({
  history: createWebHistory(),
  routes,
})

export default router
