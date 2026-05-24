const isMock = import.meta.env.VITE_USE_MOCK !== 'false'
const baseURL = import.meta.env.VITE_API_BASE_URL || '/api/supplier'

const wait = (data) => new Promise((resolve) => setTimeout(() => resolve(data), 180))

const success = (body, message = 'success') => ({ error: 0, body, message, success: true })

const mockData = {
  '/dashboard/overview': success({
    todo_count: 18,
    pending_shipments: 12,
    pending_installations: 3,
    sales_amount: 86420,
    fulfillment_rate: 96.8,
    alerts: [
      { id: 1, title: '报价审核被驳回', content: '北欧橡木餐桌需补充材质证明', priority: 'high' },
      { id: 2, title: '安装完成照片待上传', content: '订单 SO20240518023 · 预约 16:30', priority: 'normal' },
    ],
  }, '获取工作台成功'),
  '/wallet/overview': success({
    total_income: 128560.8,
    withdrawable_amount: 92340,
    frozen_amount: 8216,
    order_amount: 119800,
    delivery_fee: 6240,
    installation_fee: 4860,
    refund_deduction: 1120,
    aftersale_deduction: 1220,
  }, '获取钱包成功'),
  '/wallet/transactions': success({
    list: [
      { id: 1, title: '订单 SO20240518023', amount: 8680, direction: 'income', created_at: '2024-05-18' },
      { id: 2, title: '售后扣减 AS24051809', amount: 360, direction: 'deduction', created_at: '2024-05-17' },
    ],
    total: 2,
  }, '获取收入明细成功'),
  '/products': success({
    list: [
      { id: 101, name: '北欧云朵模块沙发', category: '沙发', material: '棉麻', color: '燕麦白', size: '280×96×78cm', model: 'SF-ND-280', price: 6899, stock: 126, status: 'draft', support_installation: true },
      { id: 102, name: '意式岩板餐桌', category: '餐桌', material: '岩板', color: '鱼肚白', size: '180×90×76cm', model: 'DT-LUX-180', price: 4299, stock: 48, status: 'reviewing', support_installation: true },
    ],
    total: 2,
  }, '获取商品列表成功'),
  '/fulfillments': success({
    summary: { order_count: 18, item_count: 42, total_volume: 86.4, total_weight: 2180 },
    list: [
      { delivery_id: 7001, order_no: 'SO20240518023', area: '西湖区', warehouse: '仓库 A', customer_address: '杭州市西湖区', appointment_time: '10:00-12:00', quantity: 3, volume: 8.6, weight: 260, status: 'pending' },
      { delivery_id: 7002, order_no: 'SO20240518025', area: '滨江区', warehouse: '仓库 A', customer_address: '杭州市滨江区', appointment_time: '14:00-18:00', quantity: 2, volume: 6.2, weight: 210, status: 'pending' },
    ],
  }, '获取配送单成功'),
  '/installations': success({
    list: [
      { installation_id: 8001, order_no: 'SO20240518023', installer_name: '周师傅', appointment_time: '16:30-18:00', status: 'in_progress', photos: [] },
    ],
  }, '获取安装订单成功'),
  '/aftersales': success({
    list: [
      { case_id: 6001, case_no: 'AS24051809', type: '运输破损', description: '岩板餐桌边角破损', status: 'pending', remaining_hours: 2 },
      { case_id: 6002, case_no: 'AS24051812', type: '少件', description: '柜体五金包缺失', status: 'pending', remaining_hours: 6 },
    ],
    total: 2,
  }, '获取售后工单成功'),
  '/reviews': success({
    list: [
      { review_id: 5001, type: 'product', title: '商品审核 · 北欧云朵沙发', status: 'rejected', reject_reason: '缺少包装尺寸与安装服务说明' },
      { review_id: 5002, type: 'quote', title: '报价审核 · 岩板餐桌', status: 'pending', reject_reason: '' },
    ],
    stats: { pending: 8, rejected: 3, approved: 42 },
  }, '获取审核列表成功'),
  '/analytics/overview': success({
    product_views: 48620,
    order_count: 386,
    sales_amount: 486920,
    return_rate: 2.8,
    aftersale_rate: 4.6,
    fulfillment_rate: 96.8,
    trend: [4.8, 6.6, 5.4, 7.8, 6.2, 8.8, 7.2],
    top_products: [
      { product_id: 101, name: '云朵模块沙发', views: 9820, orders: 86, sales_amount: 592000 },
      { product_id: 102, name: '实木储物床', views: 6420, orders: 51, sales_amount: 317000 },
    ],
  }, '获取经营数据成功'),
}

export async function request(path, options = {}) {
  if (isMock) {
    if (options.method === 'POST') {
      return wait(success({ submitted: true }, '提交成功'))
    }
    return wait(mockData[path] || success(null))
  }

  const query = options.params
    ? `?${new URLSearchParams(
        Object.entries(options.params).filter(([, value]) => value !== undefined && value !== null && value !== '')
      ).toString()}`
    : ''

  const method = options.method || 'GET'
  const token = localStorage.getItem('supplier_token')
  const headers = {
    ...(options.data ? { 'Content-Type': 'application/json' } : {}),
    ...(token ? { Authorization: `Bearer ${token}` } : {}),
    ...options.headers,
  }

  const response = await fetch(`${baseURL}${path}${query}`, {
    method,
    headers,
    body: options.data ? JSON.stringify(options.data) : undefined,
  })

  return response.json()
}
