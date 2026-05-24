<template>
  <div class="page">
    <van-nav-bar title="钱包与结算" fixed placeholder />
    <div class="hero">
      <p class="hero-title">¥{{ wallet.total_income || 0 }}</p>
      <p class="hero-desc">收入总金额，含订单、配送、安装及逆向扣减</p>
      <div class="metric-grid">
        <div class="metric-card"><span>可提现</span><strong>¥{{ wallet.withdrawable_amount || 0 }}</strong></div>
        <div class="metric-card"><span>冻结中</span><strong>¥{{ wallet.frozen_amount || 0 }}</strong></div>
      </div>
    </div>
    <div class="section"><div class="section-title">结算单拆分</div><div class="card stack">
      <div class="row"><span>订单金额</span><strong>¥{{ wallet.order_amount }}</strong></div>
      <div class="row"><span>配送费</span><strong>+¥{{ wallet.delivery_fee }}</strong></div>
      <div class="row"><span>安装费</span><strong>+¥{{ wallet.installation_fee }}</strong></div>
      <div class="row"><span>退款扣减</span><strong class="amount-minus">-¥{{ wallet.refund_deduction }}</strong></div>
      <div class="row"><span>售后扣款</span><strong class="amount-minus">-¥{{ wallet.aftersale_deduction }}</strong></div>
    </div></div>
    <div class="section"><div class="section-title">收入明细</div><div class="card stack"><div v-for="item in transactions" :key="item.id" class="row"><div><strong>{{ item.title }}</strong><div class="muted">{{ item.created_at }}</div></div><strong :class="item.direction === 'income' ? 'amount-plus' : 'amount-minus'">{{ item.direction === 'income' ? '+' : '-' }}¥{{ item.amount }}</strong></div></div></div>
    <TabBar />
  </div>
</template>
<script setup>
import { onMounted, ref } from 'vue'
import { supplierApi } from '../api/supplier'
import TabBar from './TabBar.vue'
const wallet = ref({})
const transactions = ref([])
onMounted(async () => {
  wallet.value = (await supplierApi.getWallet()).body || {}
  transactions.value = ((await supplierApi.getTransactions()).body || {}).list || []
})
</script>
