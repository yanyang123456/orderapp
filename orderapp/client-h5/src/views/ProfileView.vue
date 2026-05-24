<template>
  <main class="page">
    <van-nav-bar title="我的" fixed placeholder />
    <div class="content">
      <section class="card" style="padding: 16px">
        <div class="row-between">
          <div>
            <div style="font-size: 18px; font-weight: 700">{{ profile.user?.nickname }}</div>
            <div class="muted">{{ profile.user?.mobile }}</div>
          </div>
          <van-tag type="primary">{{ profile.user?.level }}</van-tag>
        </div>
      </section>

      <section class="section card" style="padding: 14px">
        <van-grid :column-num="2" :border="false">
          <van-grid-item text="订单" :badge="String(profile.stats?.orders || 0)" icon="orders-o" />
          <van-grid-item text="售后" :badge="String(profile.stats?.aftersales || 0)" icon="service-o" />
          <van-grid-item text="收藏" :badge="String(profile.stats?.favorites || 0)" icon="like-o" />
          <van-grid-item text="历史" :badge="String(profile.stats?.history || 0)" icon="clock-o" />
        </van-grid>
      </section>

      <section class="section card">
        <van-cell title="收货地址管理" is-link />
        <van-cell title="优惠券" :value="`${profile.stats?.coupons || 0} 张`" is-link />
        <van-cell title="发票管理" is-link />
        <van-cell title="联系客服" is-link />
        <van-cell title="设置" is-link />
      </section>
    </div>
    <AppTabBar />
  </main>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { clientApi } from '../api/client'
import AppTabBar from '../components/AppTabBar.vue'

const profile = ref({ user: {}, stats: {} })

onMounted(async () => {
  const response = await clientApi.getProfileOverview()
  profile.value = response.body || profile.value
})
</script>
