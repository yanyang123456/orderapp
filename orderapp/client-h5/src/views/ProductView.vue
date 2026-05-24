<template>
  <main class="page">
    <van-nav-bar title="商品浏览" fixed placeholder />
    <div class="content">
      <van-search v-model="params.keyword" shape="round" placeholder="搜索商品、材质、风格" @search="load" />
      <van-tabs v-model:active="activeCategory" sticky offset-top="46" @change="changeCategory">
        <van-tab title="全部" name="" />
        <van-tab title="沙发" name="沙发" />
        <van-tab title="床" name="床" />
        <van-tab title="桌椅" name="桌椅" />
        <van-tab title="柜类" name="柜类" />
      </van-tabs>

      <section class="section card">
        <ProductListItem v-for="item in products" :key="item.id" :product="item" />
        <van-empty v-if="!products.length" description="暂无商品" />
      </section>

      <van-popup v-model:show="showDetail" round position="bottom" :style="{ maxHeight: '86%' }">
        <div v-if="detail" style="padding-bottom: 18px">
          <van-swipe :autoplay="3000" style="height: 260px">
            <van-swipe-item v-for="image in detail.images" :key="image">
              <img :src="image" style="width:100%;height:260px;object-fit:cover" />
            </van-swipe-item>
          </van-swipe>
          <div class="content">
            <h2 style="margin: 0 0 8px">{{ detail.name }}</h2>
            <div class="price" style="font-size: 22px">¥{{ detail.price }}</div>
            <p class="muted">{{ detail.description }}</p>
            <van-cell-group inset>
              <van-cell title="材质" :value="detail.material" />
              <van-cell title="尺寸" :value="detail.size" />
              <van-cell title="库存" :value="`${detail.stock} 件`" />
              <van-cell title="配送区域" :value="detail.delivery_areas?.join('、')" />
              <van-cell title="包装信息" :value="detail.package_info" />
            </van-cell-group>
            <div class="section">
              <van-button block type="primary" round :loading="adding" @click="addCart(detail)">加入购物车</van-button>
            </div>
          </div>
        </div>
      </van-popup>
    </div>
    <AppTabBar />
  </main>
</template>

<script setup>
import { onMounted, reactive, ref, watch } from 'vue'
import { useRoute } from 'vue-router'
import { showToast } from 'vant'
import { clientApi } from '../api/client'
import AppTabBar from '../components/AppTabBar.vue'
import ProductListItem from '../components/ProductListItem.vue'

const route = useRoute()
const activeCategory = ref('')
const products = ref([])
const detail = ref(null)
const showDetail = ref(false)
const adding = ref(false)
const params = reactive({ page: 1, page_size: 10, keyword: '', category: '' })

const load = async () => {
  const response = await clientApi.getProducts(params)
  products.value = response.body?.list || []
}
const changeCategory = (name) => {
  params.category = name
  load()
}
const openDetail = async (id) => {
  const response = await clientApi.getProductDetail(id)
  detail.value = response.body
  showDetail.value = true
}
const addCart = async (product) => {
  adding.value = true
  try {
    await clientApi.addCartItem({ product_id: product.id, quantity: 1, support_installation: product.support_installation })
    showToast('已加入购物车')
    showDetail.value = false
    await router.replace('/cart')
  } finally {
    adding.value = false
  }
}

watch(() => route.query, async (query) => {
  params.keyword = query.keyword || ''
  params.category = query.category || ''
  activeCategory.value = params.category
  if (query.id) await openDetail(query.id)
  await load()
}, { immediate: true })

onMounted(load)
</script>
