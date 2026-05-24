<template>
  <div class="page">
    <van-nav-bar title="售后工单" fixed placeholder />
    <div class="section"><div class="card stack"><div v-for="item in list" :key="item.case_id" class="row"><div><strong>{{ item.case_no }} · {{ item.type }}</strong><div class="muted">{{ item.description }}</div><div class="muted">剩余 {{ item.remaining_hours }} 小时</div></div><van-button size="small" type="danger" @click="handle(item)">处理</van-button></div></div></div>
    <TabBar />
  </div>
</template>
<script setup>
import { onMounted, ref } from 'vue'
import { showToast } from 'vant'
import { supplierApi } from '../api/supplier'
import TabBar from './TabBar.vue'
const list = ref([])
onMounted(async () => { list.value = ((await supplierApi.getAftersales()).body || {}).list || [] })
const handle = async (item) => { await supplierApi.handleAftersale(item.case_id, { opinion: '确认属实', solution: 'redispatch' }); showToast('方案已提交审核') }
</script>
