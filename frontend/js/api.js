// Full-path API client — paths match Spring Boot controller mappings exactly.
// Most controllers are under /api/v1. ItemController has no class-level prefix.

const API_BASE = 'http://localhost:8080';

async function request(method, path, body, params) {
  const url = new URL(path, API_BASE);
  if (params) {
    Object.entries(params).forEach(([k, v]) => {
      if (v !== undefined && v !== null) url.searchParams.set(k, v);
    });
  }
  const headers = { 'Content-Type': 'application/json', ...makeAuthHeader() };
  const res = await fetch(url.toString(), {
    method,
    headers,
    body: body !== undefined ? JSON.stringify(body) : undefined,
  });
  if (!res.ok) {
    const text = await res.text();
    throw new Error(text || `HTTP ${res.status}`);
  }
  const text = await res.text();
  try { return text ? JSON.parse(text) : null; } catch { return text; }
}

const api = {
  // ── Users  /api/v1/users ─────────────────────────────────────────────────
  login: (schoolId, password) =>
    request('POST', '/api/v1/users/login', { schoolId, password }),
  register: (data) =>
    request('POST', '/api/v1/users/register', data),
  getProfile: (schoolId) =>
    request('GET', '/api/v1/users/profile', undefined, { schoolId }),
  updateProfile: (schoolId, data) =>
    request('PUT', `/api/v1/users/${schoolId}`, data),
  changePassword: (schoolId, data) =>
    request('PATCH', `/api/v1/users/${schoolId}/password`, data),

  // ── Menus  /api/v1/menus ─────────────────────────────────────────────────
  getMenus: () =>
    request('GET', '/api/v1/menus'),
  createMenu: (data) =>
    request('POST', '/api/v1/menus/menu', data),
  updateMenu: (id, data) =>
    request('PATCH', `/api/v1/menus/menu/${id}`, data),

  // ── Items  /item — ItemController has no /api/v1 class-level mapping ──────
  getItems: () =>
    request('GET', '/item'),
  getPopularItems: (limit = 10) =>
    request('GET', '/item/popular', undefined, { limit }),
  createItem: (data) =>
    request('POST', '/item', data),
  updateItem: (id, data) =>
    request('PATCH', `/item/${id}`, data),
  linkMenuItem: (menuId, itemId) =>
    request('POST', '/menu_item', undefined, { menuId, itemId }),
  unlinkMenuItem: (menuId, itemId) =>
    request('DELETE', '/menu_item', undefined, { menuId, itemId }),

  // ── Orders  /api/v1/orders ───────────────────────────────────────────────
  placeOrder: (userId, orderData) =>
    request('POST', '/api/v1/orders/order', orderData, { userId }),
  cancelOrder: (userId, orderId) =>
    request('POST', `/api/v1/orders/cancel/${orderId}`, undefined, { userId }),
  trackOrder: (orderId) =>
    request('GET', `/api/v1/orders/track/${orderId}`),
  getOrderHistory: (userId) =>
    request('GET', '/api/v1/orders/history', undefined, { userId }),

  // ── Admin orders  /api/v1/orders ─────────────────────────────────────────
  getAllOrders: () =>
    request('GET', '/api/v1/orders/all'),
  adminSetStatus: (orderId, status) =>
    request('POST', `/api/v1/orders/admin/${orderId}/status`, undefined, { status }),
  getStatusLogs: () =>
    request('GET', '/api/v1/orders/status-logs'),

  // ── Kitchen  /api/v1/orders/kitchen ──────────────────────────────────────
  getKitchenOrders: () =>
    request('GET', '/api/v1/orders/kitchen/active'),
  prepareOrder: (orderId) =>
    request('POST', `/api/v1/orders/kitchen/${orderId}/prepare`),
  markReady: (orderId) =>
    request('POST', `/api/v1/orders/kitchen/${orderId}/ready`),
  completeOrder: (orderId) =>
    request('POST', `/api/v1/orders/kitchen/${orderId}/complete`),

  // ── Wallet  /api/v1/wallets ──────────────────────────────────────────────
  getWallet: (userId) =>
    request('GET', `/api/v1/wallets/${userId}`),
  getTransactions: (userId) =>
    request('GET', `/api/v1/wallets/${userId}/transactions`),
  topUp: (data) =>
    request('POST', '/api/v1/wallets/top-up', data),

  // ── Admin  /api/v1/admin ─────────────────────────────────────────────────
  getCutOff: () =>
    request('GET', '/api/v1/admin/get-cut-off'),
  setCutOff: (newCutOffTime) =>
    request('POST', '/api/v1/admin/set-cut-off', undefined, { newCutOffTime }),
  getOrderWindow: () =>
    request('GET', '/api/v1/admin/get-order-window'),
  setOrderWindow: (openTime, closeTime) =>
    request('POST', '/api/v1/admin/set-order-window', undefined, { openTime, closeTime }),
  getAllUsers: () =>
    request('GET', '/api/v1/admin/users'),
  deleteUser: (id) =>
    request('DELETE', `/api/v1/admin/users/${id}`),
};
