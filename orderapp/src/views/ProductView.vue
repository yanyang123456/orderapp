<template>
  <div class="page">
    <van-nav-bar title="商品建档" fixed placeholder right-text="新增" @click-right="submit" />
    <van-form class="section">
      <van-cell-group inset>
        <van-field v-model="form.name" label="名称" placeholder="商品名称" />
        <van-field v-model="form.category" label="分类" placeholder="家具分类" />
        <van-field v-model="form.material" label="材质" placeholder="材质" />
        <van-field v-model="form.color" label="颜色" placeholder="颜色" />
        <van-field v-model="form.size" label="尺寸" placeholder="尺寸" />
        <van-field v-model="form.model" label="型号" placeholder="规格型号" />
        <van-field v-model="form.price" label="价格" type="number" placeholder="价格" />
        <van-field v-model="form.stock" label="库存" type="digit" placeholder="库存" />
      </van-cell-group>
    </van-form>
    <div class="section">
      <div class="section-title">商品列表</div>
      <div class="card stack">
        <div v-for="item in products" :key="item.id" class="row">
          <div>
            <strong>{{ item.name }}</strong>
            <div class="muted">{{ item.category }} · {{ item.model }} · 库存 {{ item.stock }}</div>
          </div>
          <div>
            <strong>¥{{ item.price }}</strong>
            <div><van-tag type="primary">{{ item.status }}</van-tag></div>
          </div>
        </div>
      </div>
    </div>
    <TabBar />
  </div>
</template>

<script setup>
import { onMounted, reactive, ref } from 'vue'
import { showToast } from 'vant'
import { supplierApi } from '../api/supplier'
import TabBar from './TabBar.vue'

const products = ref([])
const form = reactive({ name: '', category: '', material: '', color: '', size: '', model: '', price: '', stock: '' })

const load = async () => {
  const response = await supplierApi.getProducts({ page: 1, page_size: 10, status: 'all' })
  products.value = (response.body || {}).list || []
}

const submit = async () => {
  const response = await supplierApi.createProduct({ ...form })
  if (!response.success) {
    showToast(response.message || '商品创建失败')
    return
  }
  showToast('商品创建成功')
  await load()
}

onMounted(load)
</script>
