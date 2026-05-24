<template>
  <main class="page">
    <van-nav-bar title="购物车" fixed placeholder />
    <div class="content">
      <section class="card">
        <div v-for="item in items" :key="item.cart_item_id" class="product-card">
          <div class="product-cover"><img :src="item.image" :alt="item.name" /></div>
          <div class="product-info">
            <div class="product-name">{{ item.name }}</div>
            <div class="muted">支持安装：{{ item.support_installation ? '是' : '否' }}</div>
            <div class="row-between" style="margin-top: 8px">
              <span class="price">¥{{ item.price }}</span>
              <van-stepper v-model="item.quantity" min="0" integer :disabled="updating" @change="handleQuantityChange(item)" />
            </div>
          </div>
        </div>
        <van-empty v-if="!items.length" description="购物车为空" />
      </section>
    </div>
    <div class="safe-actions">
      <van-submit-bar :price="totalAmount * 100" button-text="去结算" @submit="goCheckout" />
    </div>
    <AppTabBar />
  </main>
</template>

<script setup>
import { onActivated, onMounted, ref } from 'vue'
import { showFailToast, showSuccessToast } from 'vant'
import { useRouter } from 'vue-router'
import { clientApi } from '../api/client'
import AppTabBar from '../components/AppTabBar.vue'

const router = useRouter()
const items = ref([])
const totalAmount = ref(0)
const updating = ref(false)

const calculateTotal = () => {
  totalAmount.value = items.value.reduce((sum, current) => sum + Number(current.price || 0) * Number(current.quantity || 0), 0)
}

const load = async () => {
  const response = await clientApi.getCartItems()
  items.value = response.body?.list || []
  totalAmount.value = response.body?.total_amount || 0
}

const handleQuantityChange = async (item) => {
  if (updating.value) return
  updating.value = true
  try {
    const quantity = Number(item.quantity || 0)
    const response = await clientApi.updateCartItem(item.cart_item_id, { quantity })
    if (!response.success) {
      showFailToast(response.message || '更新购物车失败')
      await load()
      return
    }
    if (quantity <= 0 || response.body?.removed) {
      items.value = items.value.filter((current) => current.cart_item_id !== item.cart_item_id)
      showSuccessToast('已从购物车移除')
    }
    calculateTotal()
  } finally {
    updating.value = false
  }
}

const goCheckout = async () => {
  if (!items.value.length) {
    showFailToast('购物车为空')
    return
  }
  router.push('/checkout')
}

onMounted(load)
onActivated(load)
</script>
