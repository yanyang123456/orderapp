<template>
  <main class="page">
    <van-nav-bar title="结算" fixed placeholder />
    <div class="content">
      <section class="section card" style="padding: 14px">
        <h2 class="section-title">收货信息</h2>
        <van-cell-group inset>
          <van-field v-model="form.receiver_name" label="联系人" placeholder="请输入联系人" />
          <van-field v-model="form.receiver_mobile" label="手机号" placeholder="请输入手机号" />
          <van-field v-model="form.receiver_address" label="地址" type="textarea" rows="2" placeholder="请输入收货地址" />
          <van-field v-model="form.delivery_method" label="配送方式" placeholder="home_delivery" />
          <van-field v-model="form.remark" label="备注" type="textarea" rows="2" placeholder="选填" />
        </van-cell-group>
      </section>

      <section class="section card" style="padding: 14px">
        <h2 class="section-title">商品清单</h2>
        <div v-for="item in items" :key="item.cart_item_id" class="product-card" style="border:none;padding:10px 0">
          <div class="product-cover"><img :src="item.image" :alt="item.name" /></div>
          <div class="product-info">
            <div class="product-name">{{ item.name }}</div>
            <div class="muted">{{ item.quantity }} 件</div>
            <div class="price">¥{{ item.amount }}</div>
          </div>
        </div>
      </section>
    </div>
    <div class="safe-actions">
      <van-submit-bar :price="totalAmount * 100" button-text="提交订单" :loading="submitting" @submit="submit" />
    </div>
    <AppTabBar />
  </main>
</template>

<script setup>
import { computed, onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import { showFailToast, showSuccessToast } from 'vant'
import { clientApi } from '../api/client'
import AppTabBar from '../components/AppTabBar.vue'

const router = useRouter()
const items = ref([])
const submitting = ref(false)
const form = ref({ receiver_name: '张三', receiver_mobile: '13800000000', receiver_address: '杭州市西湖区文三路 18 号', delivery_method: 'home_delivery', remark: '' })

const totalAmount = computed(() => items.value.reduce((sum, item) => sum + Number(item.amount || 0), 0))

const load = async () => {
  const response = await clientApi.getCartItems()
  items.value = response.body?.list || []
}

const submit = async () => {
  if (!items.value.length) {
    showFailToast('购物车为空')
    return
  }
  submitting.value = true
  try {
    const payload = {
      items: items.value.map((item) => ({ product_id: item.product_id, quantity: item.quantity, support_installation: item.support_installation })),
      receiver_name: form.value.receiver_name,
      receiver_mobile: form.value.receiver_mobile,
      receiver_address: form.value.receiver_address,
      delivery_method: form.value.delivery_method,
      remark: form.value.remark,
    }
    const response = await clientApi.createOrder(payload)
    if (!response.success) {
      showFailToast(response.message || '提交失败')
      return
    }
    showSuccessToast('订单已创建，待付款')
    router.replace('/order?status=pending_payment')
  } finally {
    submitting.value = false
  }
}

onMounted(load)
</script>
