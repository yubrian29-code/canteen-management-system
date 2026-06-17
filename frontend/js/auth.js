const SESSION_KEY = 'canteen_session';

function saveSession(loginResponse, password) {
  const session = {
    id: loginResponse.id,
    schoolId: loginResponse.schoolId,
    role: loginResponse.role,
    userType: loginResponse.userType,
    password: password,
  };
  localStorage.setItem(SESSION_KEY, JSON.stringify(session));
}

function getSession() {
  try {
    return JSON.parse(localStorage.getItem(SESSION_KEY));
  } catch {
    return null;
  }
}

function clearSession() {
  localStorage.removeItem(SESSION_KEY);
  localStorage.removeItem('canteen_cart');
  location.replace('/login.html');
}

// Pages the KITCHEN role cannot access within the admin section
const KITCHEN_RESTRICTED = ['dashboard.html', 'logs.html', 'settings.html'];

// Redirect to login if not authenticated, or to wrong-role home if role mismatch.
// requiredRole: 'ADMIN' | 'CUSTOMER' | 'KITCHEN' | null (any authenticated)
function guardPage(requiredRole) {
  const session = getSession();
  if (!session) {
    location.replace('/login.html');
    return null;
  }

  const isAdminLike = session.role === 'ADMIN' || session.role === 'KITCHEN';

  if (requiredRole === 'ADMIN' && !isAdminLike) {
    location.replace('/customer/menu.html');
    return null;
  }

  // KITCHEN role cannot access Dashboard, Status Logs, or Settings
  if (session.role === 'KITCHEN') {
    const page = location.pathname.split('/').pop();
    if (KITCHEN_RESTRICTED.includes(page)) {
      location.replace('/admin/orders.html');
      return null;
    }
  }

  if (requiredRole === 'CUSTOMER' && session.role !== 'CUSTOMER') {
    location.replace('/admin/orders.html');
    return null;
  }
  startIdleTimer();
  return session;
}

const IDLE_TIMEOUT_MS = 5 * 60 * 1000;
let _idleTimer = null;

function resetIdleTimer() {
  clearTimeout(_idleTimer);
  _idleTimer = setTimeout(() => {
    clearSession();
  }, IDLE_TIMEOUT_MS);
}

function startIdleTimer() {
  const events = ['mousemove', 'keydown', 'click', 'scroll', 'touchstart'];
  events.forEach(e => document.addEventListener(e, resetIdleTimer, { passive: true }));
  resetIdleTimer();
}

function makeAuthHeader() {
  const session = getSession();
  if (!session) return {};
  const token = btoa(`${session.schoolId}:${session.password}`);
  return { Authorization: `Basic ${token}` };
}
