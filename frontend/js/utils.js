function formatDate(isoString) {
  if (!isoString) return '-';
  return new Date(isoString).toLocaleString('en-HK', {
    day: '2-digit', month: 'short', year: 'numeric',
    hour: '2-digit', minute: '2-digit',
    timeZone: 'Asia/Hong_Kong',
  });
}

function formatTime(isoString) {
  if (!isoString) return '-';
  return new Date(isoString).toLocaleTimeString('en-HK', {
    hour: '2-digit', minute: '2-digit',
    timeZone: 'Asia/Hong_Kong',
  });
}

function formatCurrency(amount) {
  return 'HKD ' + Number(amount).toFixed(2);
}

const STATUS_LABELS = {
  ORDERED: 'Ordered',
  PREPARING: 'Preparing',
  READY_FOR_PICK_UP: 'Ready for Pick-up',
  PICKED_UP: 'Picked Up',
  CANCELLED: 'Cancelled',
  REJECTED: 'Rejected',
  ABANDONED: 'Abandoned',
};

function statusBadgeHTML(status) {
  const label = STATUS_LABELS[status] || status;
  return `<span class="badge badge--${status.toLowerCase()}">${label}</span>`;
}

// ── Cart Store ─────────────────────────────────────────────────────────────
const CART_KEY = 'canteen_cart';

const cartStore = {
  get() {
    try { return JSON.parse(localStorage.getItem(CART_KEY)) || []; } catch { return []; }
  },
  save(items) {
    localStorage.setItem(CART_KEY, JSON.stringify(items));
  },
  add(item) {
    const cart = this.get();
    const existing = cart.find(i => i.itemId === item.itemId);
    if (existing) {
      existing.quantity += 1;
    } else {
      cart.push({ ...item, quantity: 1 });
    }
    this.save(cart);
  },
  remove(itemId) {
    this.save(this.get().filter(i => i.itemId !== itemId));
  },
  updateQty(itemId, qty) {
    const cart = this.get();
    const item = cart.find(i => i.itemId === itemId);
    if (item) {
      if (qty <= 0) return this.remove(itemId);
      item.quantity = qty;
      this.save(cart);
    }
  },
  clear() {
    localStorage.removeItem(CART_KEY);
  },
  total() {
    return this.get().reduce((sum, i) => sum + i.price * i.quantity, 0);
  },
  count() {
    return this.get().reduce((sum, i) => sum + i.quantity, 0);
  },
};

// ── Misc helpers ───────────────────────────────────────────────────────────
function showToast(message, type = 'info') {
  let container = document.getElementById('toast-container');
  if (!container) {
    container = document.createElement('div');
    container.id = 'toast-container';
    document.body.appendChild(container);
  }
  const toast = document.createElement('div');
  toast.className = `toast toast--${type}`;
  toast.textContent = message;
  container.appendChild(toast);
  setTimeout(() => toast.classList.add('toast--visible'), 10);
  setTimeout(() => {
    toast.classList.remove('toast--visible');
    setTimeout(() => toast.remove(), 300);
  }, 3000);
}

function setLoading(btn, loading) {
  if (loading) {
    btn.dataset.originalText = btn.textContent;
    btn.disabled = true;
    btn.textContent = 'Loading…';
  } else {
    btn.disabled = false;
    btn.textContent = btn.dataset.originalText || btn.textContent;
  }
}

function getParam(name) {
  return new URLSearchParams(location.search).get(name);
}

// ── Mobile navigation ──────────────────────────────────────────────────────
document.addEventListener('DOMContentLoaded', () => {
  // Hide restricted sidebar links for the KITCHEN role
  const session = getSession ? getSession() : null;
  if (session && session.role === 'KITCHEN') {
    const restricted = ['dashboard.html', 'logs.html', 'settings.html'];
    document.querySelectorAll('.admin-sidebar__nav a').forEach(a => {
      const page = (a.getAttribute('href') || '').split('/').pop();
      if (restricted.includes(page)) a.closest('li').style.display = 'none';
    });
  }

  // Admin: hamburger toggles the sidebar as a slide-in overlay
  const sidebar = document.querySelector('.admin-sidebar');
  if (sidebar) {
    const navbar = document.querySelector('.navbar');
    const btn = document.createElement('button');
    btn.className = 'mobile-menu-btn';
    btn.setAttribute('aria-label', 'Toggle menu');
    btn.textContent = '☰';
    navbar.insertBefore(btn, navbar.firstChild);

    const overlay = document.createElement('div');
    overlay.className = 'admin-sidebar-overlay';
    document.body.appendChild(overlay);

    btn.addEventListener('click', () => {
      sidebar.classList.toggle('open');
      overlay.classList.toggle('open');
    });
    overlay.addEventListener('click', () => {
      sidebar.classList.remove('open');
      overlay.classList.remove('open');
    });
    sidebar.querySelectorAll('a').forEach(a => {
      a.addEventListener('click', () => {
        sidebar.classList.remove('open');
        overlay.classList.remove('open');
      });
    });
  }

  // Customer / kitchen: hamburger toggles the top navbar links
  const navLinks = document.querySelector('.navbar__nav');
  if (navLinks) {
    const navbar = document.querySelector('.navbar');
    const btn = document.createElement('button');
    btn.className = 'mobile-nav-btn';
    btn.setAttribute('aria-label', 'Toggle navigation');
    btn.textContent = '☰';
    navbar.appendChild(btn);

    btn.addEventListener('click', () => {
      const open = navLinks.classList.toggle('mobile-open');
      btn.textContent = open ? '✕' : '☰';
    });
    navLinks.querySelectorAll('a').forEach(a => {
      a.addEventListener('click', () => {
        navLinks.classList.remove('mobile-open');
        btn.textContent = '☰';
      });
    });
  }
});
