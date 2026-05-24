<template>
  <div class="page">
    <van-nav-bar title="安装确认" fixed placeholder />
    <div class="section"><div class="card stack"><div v-for="item in list" :key="item.installation_id" class="row"><div><strong>{{ item.order_no }}</strong><div class="muted">{{ item.installer_name }} · {{ item.appointment_time }}</div><div class="muted">状态：{{ item.status }}</div></div><van-button size="small" type="primary" @click="complete(item)">完成</van-button></div></div></div>
    <TabBar />
  </div>
</template>
<script setup>
import { onMounted, ref } from 'vue'
import { showToast } from 'vant'
import { supplierApi } from '../api/supplier'
import TabBar from './TabBar.vue'
const list = ref([])
onMounted(async () => { list.value = ((await supplierApi.getInstallations()).body || {}).list || [] })
const complete = async (item) => { await supplierApi.completeInstallation(item.installation_id, { status: 'completed', photos: [] }); showToast('已提交安装完成') }
</script>
