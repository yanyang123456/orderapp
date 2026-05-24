<template>
  <div class="page">
    <van-nav-bar title="报价改价" fixed placeholder />
    <div class="section"><div class="card stack"><div v-for="item in products" :key="item.id" class="row"><div><strong>{{ item.name }}</strong><div class="muted">当前 ¥{{ item.price }}</div></div><van-button size="small" type="primary" @click="submit(item)">提交改价</van-button></div></div></div>
    <TabBar />
  </div>
</template>
<script setup>
import { onMounted, ref } from 'vue'
import { showToast } from 'vant'
import { supplierApi } from '../api/supplier'
import TabBar from './TabBar.vue'
const products = ref([])
onMounted(async () => { products.value = ((await supplierApi.getProducts()).body || {}).list || [] })
const submit = async (item) => { await supplierApi.submitQuote({ product_id: item.id, current_price: item.price, new_price: item.price - 100, reason: '空框架默认提交', attachments: [] }); showToast('已提交审核') }
</script>
