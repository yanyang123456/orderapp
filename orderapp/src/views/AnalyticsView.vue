<template>
  <div class="page">
    <van-nav-bar title="经营数据" fixed placeholder />
    <div class="hero"><p class="hero-title">¥{{ data.sales_amount || 0 }}</p><p class="hero-desc">成交 {{ data.order_count || 0 }} 单，浏览 {{ data.product_views || 0 }}</p><div class="metric-grid"><div class="metric-card"><span>退货率</span><strong>{{ data.return_rate || 0 }}%</strong></div><div class="metric-card"><span>履约及时率</span><strong>{{ data.fulfillment_rate || 0 }}%</strong></div></div></div>
    <div class="section"><div class="section-title">热销商品</div><div class="card stack"><div v-for="item in data.top_products" :key="item.product_id" class="row"><div><strong>{{ item.name }}</strong><div class="muted">浏览 {{ item.views }} · 成交 {{ item.orders }}</div></div><strong>¥{{ item.sales_amount }}</strong></div></div></div>
    <TabBar />
  </div>
</template>
<script setup>
import { onMounted, ref } from 'vue'
import { supplierApi } from '../api/supplier'
import TabBar from './TabBar.vue'
const data = ref({ top_products: [] })
onMounted(async () => { data.value = (await supplierApi.getAnalytics()).body || { top_products: [] } })
</script>
