<template>
  <main class="page">
    <van-nav-bar title="订单" fixed placeholder />
    <div class="content">
      <van-tabs v-model:active="active" @change="load">
        <van-tab title="全部" name="all" />
        <van-tab title="待付款" name="pending_payment" />
        <van-tab title="待发货" name="pending_delivery" />
        <van-tab title="配送中" name="shipping" />
        <van-tab title="已完成" name="completed" />
      </van-tabs>

      <section class="section card">
        <div v-for="item in orders" :key="item.order_id" class="product-card">
          <div class="product-info" style="width:100%">
            <div class="row-between">
              <div class="product-name">{{ item.order_no }}</div>
              <van-tag type="primary">{{ statusText(item.status) }}</van-tag>
            </div>
            <div class="muted">{{ item.product_summary }}</div>
            <div class="muted">地址：{{ item.receiver_address }}</div>
            <div class="row-between" style="margin-top: 8px">
              <span class="price">¥{{ item.pay_amount }}</span>
              <van-button v-if="item.status === 'pending_payment'" size="small" type="primary" plain @click="pay(item.order_id)">去支付</van-button>
              <van-button v-else-if="item.status === 'shipping'" size="small" type="primary" plain @click="receive(item.order_id)">确认收货</van-button>
            </div>
          </div>
        </div>
        <van-empty v-if="!orders.length" description="暂无订单" />
      </section>
    </div>
    <AppTabBar />
  </main>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { showFailToast, showSuccessToast, showToast } from 'vant'
import { useRoute } from 'vue-router'
import { clientApi } from '../api/client'
import AppTabBar from '../components/AppTabBar.vue'

const route = useRoute()
const active = ref(route.query.status || 'all')
const orders = ref([])

const statusText = (status) => ({
  pending_payment: '待付款',
  pending_delivery: '待发货',
  shipping: '配送中',
  completed: '已完成',
}[status] || status)

const load = async () => {
  const response = await clientApi.getOrders({ page: 1, page_size: 10, status: active.value })
  orders.value = response.body?.list || []
}

const pay = async (orderId) => {
  const response = await clientApi.payOrder(orderId, { pay_channel: 'mock_pay' })
  if (!response.success) {
    showFailToast(response.message || '支付失败')
    return
  }
  showSuccessToast('支付成功')
  await load()
}

const receive = async (orderId) => {
  const response = await clientApi.receiveOrder(orderId)
  if (!response.success) {
    showFailToast(response.message || '确认收货失败')
    return
  }
  showToast('已确认收货')
  await load()
}

onMounted(load)
</script>
