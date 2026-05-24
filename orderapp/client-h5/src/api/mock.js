const success = (body, message = 'success') => ({ error: 0, body, message, success: true })

const products = [
  {
    id: 101,
    name: '北欧云朵模块沙发',
    image: 'https://images.unsplash.com/photo-1618220179428-22790b461013?auto=format&fit=crop&w=800&q=80',
    images: ['https://images.unsplash.com/photo-1618220179428-22790b461013?auto=format&fit=crop&w=800&q=80'],
    category: '沙发',
    material: '棉麻',
    color: '燕麦白',
    size: '280×96×78cm',
    model: 'SF-ND-280',
    style: '现代简约',
    price: 6899,
    stock: 126,
    spot_stock: 126,
    presale_cycle_days: 15,
    custom_cycle_days: 45,
    min_order_quantity: 1,
    delivery_areas: ['华东', '华南'],
    package_info: '三层加固纸箱 + 防撞护角',
    support_installation: true,
    description: '高回弹坐垫，模块化组合，适合客厅大空间。',
    sales: 238,
  },
  {
    id: 102,
    name: '意式岩板餐桌',
    image: 'https://images.unsplash.com/photo-1604578762246-41134e37f9cc?auto=format&fit=crop&w=800&q=80',
    images: ['https://images.unsplash.com/photo-1604578762246-41134e37f9cc?auto=format&fit=crop&w=800&q=80'],
    category: '桌椅',
    material: '岩板',
    color: '鱼肚白',
    size: '180×90×76cm',
    model: 'DT-LUX-180',
    style: '意式轻奢',
    price: 4299,
    stock: 48,
    spot_stock: 48,
    presale_cycle_days: 7,
    custom_cycle_days: 30,
    min_order_quantity: 1,
    delivery_areas: ['华东', '华南'],
    package_info: '木架包装 + 软膜保护',
    support_installation: true,
    description: '耐磨岩板桌面，适合四至六人家庭餐厅。',
    sales: 86,
  },
  {
    id: 103,
    name: '精品实木茶几',
    image: 'https://images.unsplash.com/photo-1538688525198-9b88f6f53126?auto=format&fit=crop&w=800&q=80',
    images: ['https://images.unsplash.com/photo-1538688525198-9b88f6f53126?auto=format&fit=crop&w=800&q=80'],
    category: '桌椅',
    material: '胡桃木',
    color: '原木色',
    size: '120×60×42cm',
    model: 'xz54651',
    style: '日式原木',
    price: 1500,
    stock: 35,
    spot_stock: 35,
    presale_cycle_days: 3,
    custom_cycle_days: 20,
    min_order_quantity: 1,
    delivery_areas: ['全国'],
    package_info: '纸箱包装',
    support_installation: false,
    description: '实木纹理清晰，适合小户型客厅。',
    sales: 152,
  },
]

const cartItems = [
  { cart_item_id: 3001, product_id: 101, name: products[0].name, price: products[0].price, quantity: 1, image: products[0].image, support_installation: true },
]

const orders = [
  { order_id: 4001, order_no: 'CO202405180001', status: 'shipping', pay_amount: 6899, product_summary: '北欧云朵模块沙发 ×1', appointment_time: '2024-05-18 10:00-12:00', receiver_address: '杭州市西湖区文三路 18 号' },
  { order_id: 4002, order_no: 'CO202405180002', status: 'pending_installation', pay_amount: 4299, product_summary: '意式岩板餐桌 ×1', appointment_time: '2024-05-18 14:00-18:00', receiver_address: '杭州市上城区庆春路 88 号' },
]

const aftersales = [
  { case_id: 6001, case_no: 'AS24051809', type: '运输破损', description: '岩板餐桌边角破损', status: 'processing', solution: 'redispatch' },
]

export function mockRequest(path, options = {}) {
  if (path === '/api/client/home') {
    return success({
      banners: [{ id: 1, title: '春季焕新季', image: 'https://images.unsplash.com/photo-1600210492493-0946911123ea?auto=format&fit=crop&w=1000&q=80', link_type: 'activity', link_id: 1 }],
      categories: [
        { id: 1, name: '沙发', icon: 'sofa' },
        { id: 2, name: '床', icon: 'bed' },
        { id: 3, name: '桌椅', icon: 'table' },
        { id: 4, name: '柜类', icon: 'cabinet' },
        { id: 5, name: '成套家具', icon: 'set' },
      ],
      recommend_products: products,
      hot_products: products.slice().sort((a, b) => b.sales - a.sales),
      order_reminders: { pending_payment: 1, pending_delivery: 2, pending_installation: 1, aftersale_processing: 1 },
    }, '获取首页成功')
  }
  if (path === '/api/client/products') {
    const keyword = options.params?.keyword || ''
    const category = options.params?.category || ''
    const list = products.filter((item) => (!keyword || item.name.includes(keyword)) && (!category || item.category === category))
    return success({ list, total: list.length, page: 1, page_size: 10 }, '获取商品列表成功')
  }
  if (path.startsWith('/api/client/products/')) {
    const id = Number(path.split('/').pop())
    return success(products.find((item) => item.id === id) || products[0], '获取商品详情成功')
  }
  if (path === '/api/client/cart/items' && options.method === 'POST') {
    return success({ cart_item_id: Date.now() }, '加入购物车成功')
  }
  if (path === '/api/client/cart/items') {
    return success({ list: cartItems, total_amount: cartItems.reduce((sum, item) => sum + item.price * item.quantity, 0) }, '获取购物车成功')
  }
  if (path === '/api/client/orders' && options.method === 'POST') {
    return success({ order_id: Date.now(), order_no: 'CO202405180099', pay_amount: 6899, status: 'pending_payment' }, '订单创建成功')
  }
  if (path === '/api/client/orders') {
    return success({ list: orders, total: orders.length, page: 1, page_size: 10 }, '获取订单列表成功')
  }
  if (path.includes('/pay')) return success({ order_id: Number(path.split('/').at(-2)), status: 'pending_delivery' }, '支付成功')
  if (path.includes('/receive')) return success({ order_id: Number(path.split('/').at(-2)), status: 'received' }, '确认收货成功')
  if (path === '/api/client/aftersales' && options.method === 'POST') {
    return success({ case_id: Date.now(), case_no: 'AS24051899', status: 'pending' }, '售后申请提交成功')
  }
  if (path === '/api/client/aftersales') {
    return success({ list: aftersales, total: aftersales.length, page: 1, page_size: 10 }, '获取售后列表成功')
  }
  if (path === '/api/client/profile/overview') {
    return success({ user: { user_id: 1, nickname: '张三', mobile: '13800000000', level: '普通会员', points: 1280 }, stats: { orders: 12, aftersales: 3, favorites: 8, history: 24, coupons: 3 } }, '获取我的页面成功')
  }
  return success(null)
}
