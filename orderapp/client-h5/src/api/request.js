import { mockRequest } from './mock'

const baseURL = import.meta.env.VITE_API_BASE_URL || ''
const useMockFallback = import.meta.env.VITE_USE_MOCK_FALLBACK !== 'false'

export async function request(path, options = {}) {
  const query = options.params
    ? `?${new URLSearchParams(
        Object.entries(options.params).filter(([, value]) => value !== undefined && value !== null && value !== '')
      ).toString()}`
    : ''

  const headers = {
    ...(options.data ? { 'Content-Type': 'application/json' } : {}),
    ...options.headers,
  }

  try {
    const response = await fetch(`${baseURL}${path}${query}`, {
      method: options.method || 'GET',
      headers,
      body: options.data ? JSON.stringify(options.data) : undefined,
    })

    if (!response.ok) throw new Error(`HTTP ${response.status}`)
    const result = await response.json()
    if (result?.success === false) return result
    return result
  } catch (error) {
    if (useMockFallback) return mockRequest(path, options)
    throw error
  }
}
