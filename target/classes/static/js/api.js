const API = '/api';

// ── Auth helpers ─────────────────────────────────────────────────────────────

function getUserId()   { return localStorage.getItem('userId'); }
function getUserName() { return localStorage.getItem('userName'); }

function requireAuth() {
    if (!getUserId()) { window.location.href = '/index.html'; }
}

function logout() {
    localStorage.clear();
    window.location.href = '/index.html';
}

// ── Core fetch wrapper ────────────────────────────────────────────────────────

async function apiFetch(method, path, body = null) {
    const headers = { 'Content-Type': 'application/json' };
    const uid = getUserId();
    if (uid) headers['X-User-Id'] = uid;
    const opts = { method, headers };
    if (body) opts.body = JSON.stringify(body);
    const res = await fetch(API + path, opts);
    if (res.status === 204 || res.headers.get('content-length') === '0') return null;
    const data = await res.json().catch(() => null);
    if (!res.ok) throw new Error((data && data.error) || 'Request failed');
    return data;
}

// ── API calls ─────────────────────────────────────────────────────────────────

const Auth = {
    login:    (email, password) => apiFetch('POST', '/auth/login',    { email, password }),
    register: (email, password, name) => apiFetch('POST', '/auth/register', { email, password, name }),
};

const Catalog = {
    getAll: (params = {}) => {
        const qs = new URLSearchParams();
        if (params.query)         qs.set('query',        params.query);
        if (params.type)          qs.set('type',         params.type);
        if (params.genre)         qs.set('genre',        params.genre);
        if (params.availableOnly) qs.set('availableOnly','true');
        const q = qs.toString();
        return apiFetch('GET', '/catalog' + (q ? '?' + q : ''));
    },
    undoSearch: () => apiFetch('GET', '/catalog/undo-search'),
};

const Loans = {
    borrow:  (itemId, itemType) => apiFetch('POST', '/loans',              { itemId, itemType }),
    return:  (loanId)           => apiFetch('PUT',  `/loans/${loanId}/return`),
    getMine: ()                 => apiFetch('GET',  '/loans/my'),
};

const Notifications = {
    getAll:     ()   => apiFetch('GET', '/notifications'),
    markRead:   (id) => apiFetch('PUT', `/notifications/${id}/read`),
};

// ── Toast notifications ───────────────────────────────────────────────────────

function showToast(message, type = 'success') {
    let container = document.getElementById('toast-container');
    if (!container) {
        container = document.createElement('div');
        container.id = 'toast-container';
        container.className = 'toast-container';
        document.body.appendChild(container);
    }
    const toast = document.createElement('div');
    toast.className = `toast${type === 'error' ? ' error' : ''}`;
    toast.innerHTML = `<span class="toast-icon">${type === 'success' ? '✓' : '✗'}</span>
                       <span class="toast-msg">${message}</span>`;
    container.appendChild(toast);
    setTimeout(() => { toast.style.opacity = '0'; toast.style.transition = 'opacity .3s';
        setTimeout(() => toast.remove(), 300); }, 4000);
}
