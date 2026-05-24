<template>
  <main class="page">
    <van-nav-bar title="售后" fixed placeholder />
    <div class="content">
      <section class="section card" style="padding: 14px">
        <van-field v-model="form.order_id" type="number" label="订单号" placeholder="输入订单号" />
        <van-field v-model="form.type" label="售后类型" placeholder="运输破损 / 少件 / 退货" />
        <van-field v-model="form.description" type="textarea" rows="3" label="问题描述" placeholder="描述问题" />
        <van-field v-model="form.solution" label="处理诉求" placeholder="补发 / 维修 / 退款" />
        <van-button block type="primary" round style="margin-top: 12px" @click="submit">提交售后</van-button>
      </section>

      <section class="section card">
        <div v-for="item in aftersales" :key="item.case_id" class="product-card">
          <div class="product-info" style="width:100%">
            <div class="row-between">
              <div class="product-name">{{ item.case_no }}</div>
              <van-tag type="warning">{{ item.status }}</van-tag>
            </div>
            <div class="muted">{{ item.type }}</div>
            <div class="muted">{{ item.description }}</div>
          </div>
        </div>
        <van-empty v-if="!aftersales.length" description="暂无售后" />
      </section>
    </div>
    <AppTabBar />
  </main>
</template>

<script setup>
import { onMounted, reactive, ref } from 'vue'
import { showToast } from 'vant'
import { clientApi } from '../api/client'
import AppTabBar from '../components/AppTabBar.vue'

const aftersales = ref([])
const form = reactive({ order_id: '', type: '', description: '', solution: '' })

const load = async () => {
  const response = await clientApi.getAftersales({ page: 1, page_size: 10, status: 'all' })
  aftersales.value = response.body?.list || []
}
const submit = async () => {
  await clientApi.createAftersale({ ...form })
  showToast('已提交售后')
  await load()
}

onMounted(load)
</script>
