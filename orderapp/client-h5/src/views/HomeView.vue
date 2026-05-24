<template>
  <main class="page">
    <van-nav-bar title="家具商城" fixed placeholder />
    <div class="content">
      <van-search v-model="keyword" shape="round" placeholder="搜索沙发、床、餐桌、衣柜" @search="goProduct" />

      <section class="section banner" v-if="home.banners?.length">
        <img :src="home.banners[0].image" alt="活动" />
        <div class="banner-text">
          <strong>{{ home.banners[0].title }}</strong>
          <span>精选家具限时优惠，支持配送安装</span>
        </div>
      </section>

      <section class="section card" style="padding: 14px">
        <h2 class="section-title">家具分类</h2>
        <div class="grid-5">
          <div v-for="item in home.categories" :key="item.id" class="category-item" @click="goCategory(item.name)">
            <div class="category-icon"><van-icon :name="categoryIcon(item.icon)" /></div>
            <span>{{ item.name }}</span>
          </div>
        </div>
      </section>

      <section class="section">
        <div class="row-between">
          <h2 class="section-title">推荐商品</h2>
          <van-button size="mini" type="primary" plain to="/product">更多</van-button>
        </div>
        <div class="card">
          <ProductListItem v-for="item in home.recommend_products" :key="item.id" :product="item" />
        </div>
      </section>

      <section class="section card" style="padding: 14px">
        <h2 class="section-title">订单提醒</h2>
        <van-grid :column-num="3" :border="false">
          <van-grid-item text="待付款" :badge="home.order_reminders?.pending_payment || undefined" icon="pending-payment" />
          <van-grid-item text="待发货" :badge="home.order_reminders?.pending_delivery || undefined" icon="logistics" />
          <van-grid-item text="待安装" :badge="home.order_reminders?.pending_installation || undefined" icon="wrench-o" />
          <van-grid-item text="售后中" :badge="home.order_reminders?.aftersale_processing || undefined" icon="service-o" to="/aftersales" />
        </van-grid>
      </section>
    </div>
    <AppTabBar />
  </main>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import { clientApi } from '../api/client'
import AppTabBar from '../components/AppTabBar.vue'
import ProductListItem from '../components/ProductListItem.vue'

const router = useRouter()
const keyword = ref('')
const home = ref({ banners: [], categories: [], recommend_products: [], hot_products: [], order_reminders: {} })

const categoryIcon = (icon) => ({ sofa: 'shop-o', bed: 'hotel-o', table: 'wap-home-o', cabinet: 'apps-o', set: 'cluster-o' }[icon] || 'goods-collect-o')
const goProduct = () => router.push({ path: '/product', query: { keyword: keyword.value } })
const goCategory = (category) => router.push({ path: '/product', query: { category } })

onMounted(async () => {
  const response = await clientApi.getHome()
  home.value = response.body || home.value
})
</script>
