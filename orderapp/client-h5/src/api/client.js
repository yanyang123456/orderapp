import { request } from './request'

export const clientApi = {
  getHome: () => request('/api/client/home'),
  getProducts: (params) => request('/api/client/products', { params }),
  getProductDetail: (productId) => request(`/api/client/products/${productId}`),
  addCartItem: (data) => request('/api/client/cart/items', { method: 'POST', data }),
  getCartItems: () => request('/api/client/cart/items'),
  updateCartItem: (cartItemId, data) => request(`/api/client/cart/items/${cartItemId}`, { method: 'PUT', data }),
  createOrder: (data) => request('/api/client/orders', { method: 'POST', data }),
  payOrder: (orderId, data) => request(`/api/client/orders/${orderId}/pay`, { method: 'POST', data }),
  getOrders: (params) => request('/api/client/orders', { params }),
  receiveOrder: (orderId) => request(`/api/client/orders/${orderId}/receive`, { method: 'POST' }),
  createAftersale: (data) => request('/api/client/aftersales', { method: 'POST', data }),
  getAftersales: (params) => request('/api/client/aftersales', { params }),
  getProfileOverview: () => request('/api/client/profile/overview'),
}
