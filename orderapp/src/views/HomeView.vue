<template>
  <div class="page">
    <van-nav-bar title="供应商工作台" fixed placeholder />
    <div class="hero">
      <p class="hero-title">{{ overview.pending_shipments || 0 }} 单待发货</p>
      <p class="hero-desc">{{ overview.pending_installations || 0 }} 单待安装确认，今日待处理 {{ overview.todo_count || 0 }} 项</p>
      <div class="metric-grid">
        <div class="metric-card"><span>销售金额</span><strong>¥{{ overview.sales_amount || 0 }}</strong></div>
        <div class="metric-card"><span>履约及时率</span><strong>{{ overview.fulfillment_rate || 0 }}%</strong></div>
      </div>
    </div>
    <div class="section">
      <div class="section-title">快捷入口</div>
      <van-grid :column-num="4" border="false">
        <van-grid-item icon="add-o" text="商品建档" to="/products" />
        <van-grid-item icon="coupon-o" text="报价改价" to="/quote" />
        <van-grid-item icon="logistics" text="发货配送" to="/fulfillment" />
        <van-grid-item icon="service-o" text="售后工单" to="/aftersales" />
      </van-grid>
    </div>
    <div class="section">
      <div class="section-title">待办提醒</div>
      <div class="card stack">
        <div v-for="item in overview.alerts" :key="item.id" class="row">
          <div><strong>{{ item.title }}</strong><div class="muted">{{ item.content }}</div></div>
          <van-tag :type="item.priority === 'high' ? 'danger' : 'warning'">{{ item.priority === 'high' ? '紧急' : '待处理' }}</van-tag>
        </div>
      </div>
    </div>
    <TabBar />
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { supplierApi } from '../api/supplier'
import TabBar from './TabBar.vue'

const overview = ref({})

onMounted(async () => {
  const res = await supplierApi.getDashboard()
  overview.value = res.body || {}
})
</script>
