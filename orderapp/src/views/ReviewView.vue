<template>
  <div class="page">
    <van-nav-bar title="审核中心" fixed placeholder />
    <div class="section"><div class="metric-grid"><div class="card"><strong>{{ stats.pending }}</strong><div class="muted">审核中</div></div><div class="card"><strong>{{ stats.rejected }}</strong><div class="muted">被驳回</div></div></div></div>
    <div class="section"><div class="card stack"><div v-for="item in list" :key="item.review_id"><div class="row"><strong>{{ item.title }}</strong><van-tag :type="item.status === 'rejected' ? 'danger' : 'warning'">{{ item.status }}</van-tag></div><div v-if="item.reject_reason" class="muted">{{ item.reject_reason }}</div></div></div></div>
    <TabBar />
  </div>
</template>
<script setup>
import { onMounted, ref } from 'vue'
import { supplierApi } from '../api/supplier'
import TabBar from './TabBar.vue'
const list = ref([])
const stats = ref({})
onMounted(async () => { const body = (await supplierApi.getReviews()).body || {}; list.value = body.list || []; stats.value = body.stats || {} })
</script>
