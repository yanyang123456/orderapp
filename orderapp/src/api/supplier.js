import { request } from './request'

export const supplierApi = {
  login: (data) => request('/auth/login', { method: 'POST', data }),
  getDashboard: () => request('/dashboard/overview'),
  getWallet: (params) => request('/wallet/overview', { params }),
  getTransactions: (params) => request('/wallet/transactions', { params }),
  getProducts: (params) => request('/products', { params }),
  createProduct: (data) => request('/products', { method: 'POST', data }),
  submitQuote: (data) => request('/quotes', { method: 'POST', data }),
  getFulfillments: (params) => request('/fulfillments', { params }),
  shipFulfillment: (deliveryId, data) => request(`/fulfillments/${deliveryId}/ship`, { method: 'POST', data }),
  getInstallations: (params) => request('/installations', { params }),
  completeInstallation: (installationId, data) => request(`/installations/${installationId}/complete`, { method: 'POST', data }),
  getAftersales: (params) => request('/aftersales', { params }),
  handleAftersale: (caseId, data) => request(`/aftersales/${caseId}/handle`, { method: 'POST', data }),
  getReviews: (params) => request('/reviews', { params }),
  getAnalytics: (params) => request('/analytics/overview', { params }),
}
