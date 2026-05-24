<template>
  <div class="product-card" @click="router.push(`/product?id=${product.id}`)">
    <div class="product-cover"><img :src="product.image" :alt="product.name" /></div>
    <div class="product-info">
      <div class="product-name">{{ product.name }}</div>
      <div class="muted">{{ product.material }} · {{ product.color }} · {{ product.size }}</div>
      <div class="muted">库存 {{ product.stock }} · {{ product.support_installation ? '支持安装' : '无需安装' }}</div>
      <div class="row-between" style="margin-top: 8px">
        <span class="price">¥{{ product.price }}</span>
        <van-tag type="primary">{{ product.category }}</van-tag>
      </div>
    </div>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'

defineProps({ product: { type: Object, required: true } })
const router = useRouter()
</script>
