<template>
  <div class="page">
    <van-nav-bar title="发货配送" fixed placeholder />
    <div class="section">
      <div class="card stack">
        <div v-for="item in list" :key="item.id" class="row">
          <div>
            <strong>{{ item.orderNo }}</strong>
            <div class="muted">{{ normalizeAddress(item.area, item.customerAddress) }}</div>
            <div class="muted">{{ item.appointmentTime }} · {{ item.quantity }}件</div>
          </div>
          <van-button size="small" type="primary" @click="ship(item)">确认发货</van-button>
        </div>
      </div>
    </div>
    <TabBar />
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { showToast } from 'vant'
import { supplierApi } from '../api/supplier'
import TabBar from './TabBar.vue'

const list = ref([])

const normalizeAddress = (area, address) => {
  if (!address) return area || ''
  if (!area) return address
  return address.startsWith(area) ? address : `${area} · ${address}`
}

const load = async () => {
  const response = await supplierApi.getFulfillments({ status: 'pending' })
  list.value = (response.body || {}).list || []
}

const ship = async (item) => {
  await supplierApi.shipFulfillment(item.id, {})
  showToast('已确认发货')
  await load()
}

onMounted(load)
</script>
