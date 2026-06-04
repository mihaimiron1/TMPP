document.addEventListener('DOMContentLoaded', () => {
    requireAuth();
    document.getElementById('user-name').textContent = getUserName();

    // Notification badge
    const unread = parseInt(localStorage.getItem('unreadCount') || '0');
    if (unread > 0) {
        const badge = document.getElementById('notif-count');
        badge.textContent = unread;
        badge.style.display = 'flex';
    }

    loadCatalog();
    setupSearch();
    setupNotifPanel();
});

// ── Catalog ────────────────────────────────────────────────────────────────

async function loadCatalog() {
    try {
        const items = await Catalog.getAll();
        renderItems(items);
    } catch (err) { showToast(err.message, 'error'); }
}

function setupSearch() {
    let timer;
    const debounce = (fn, ms = 400) => { clearTimeout(timer); timer = setTimeout(fn, ms); };

    document.getElementById('search-input') .addEventListener('input',  () => debounce(doSearch));
    document.getElementById('genre-filter') .addEventListener('input',  () => debounce(doSearch));
    document.getElementById('type-filter')  .addEventListener('change', doSearch);
    document.getElementById('avail-filter') .addEventListener('change', doSearch);
    document.getElementById('search-btn')   .addEventListener('click',  doSearch);
    document.getElementById('undo-btn')     .addEventListener('click',  doUndo);
}

async function doSearch() {
    const params = {};
    const q = document.getElementById('search-input').value.trim();
    const t = document.getElementById('type-filter').value;
    const g = document.getElementById('genre-filter').value.trim();
    const a = document.getElementById('avail-filter').checked;
    if (q) params.query = q;
    if (t) params.type  = t;
    if (g) params.genre = g;
    if (a) params.availableOnly = true;

    try {
        const items = await Catalog.getAll(params);
        renderItems(items);
    } catch (err) { showToast(err.message, 'error'); }
}

async function doUndo() {
    try {
        const items = await Catalog.undoSearch();
        renderItems(items);
        showToast('Search undone');
    } catch (err) { showToast(err.message, 'error'); }
}

function renderItems(items) {
    const grid = document.getElementById('items-grid');
    if (!items || items.length === 0) {
        grid.innerHTML = `<div class="empty-state" style="grid-column:1/-1">
            <div class="icon">🔍</div><p>No items found</p></div>`;
        return;
    }
    grid.innerHTML = items.map(itemCard).join('');

    grid.querySelectorAll('.borrow-btn').forEach(btn => {
        btn.addEventListener('click', async () => {
            const { id, type } = btn.dataset;
            btn.disabled = true;
            btn.textContent = '…';
            try {
                const loan = await Loans.borrow(id, type);
                showToast(`Borrowed! Due date: ${loan.dueDate}`);
                loadCatalog();
            } catch (err) {
                showToast(err.message, 'error');
                btn.disabled = false;
                btn.textContent = 'Borrow';
            }
        });
    });
}

function itemCard(item) {
    let specific = '';
    if (item.type === 'BOOK')
        specific = `<tr><td class="meta-key">Author</td><td class="meta-val">${item.author || '—'}</td></tr>`;
    else if (item.type === 'MAGAZINE')
        specific = `<tr><td class="meta-key">Publisher</td><td class="meta-val">${item.publisher || '—'}</td></tr>
                    <tr><td class="meta-key">Issue</td><td class="meta-val">${item.month || ''} #${item.issueNumber || ''}</td></tr>`;
    else if (item.type === 'DVD')
        specific = `<tr><td class="meta-key">Director</td><td class="meta-val">${item.director || '—'}</td></tr>
                    <tr><td class="meta-key">Duration</td><td class="meta-val">${item.durationMinutes} min</td></tr>`;

    const availText = item.available
        ? `<span class="avail-yes">✓ ${item.availableQuantity}/${item.quantity} available</span>`
        : `<span class="avail-no">✗ Out of stock</span>`;

    const borrowBtn = item.available
        ? `<button class="btn btn-primary btn-sm borrow-btn" data-id="${item.id}" data-type="${item.type}">Borrow</button>`
        : `<button class="btn btn-outline btn-sm" disabled>Unavailable</button>`;

    return `
    <div class="item-card">
      <div class="item-card-top">
        <div style="display:flex;justify-content:space-between;align-items:flex-start;gap:.5rem">
          <div class="item-card-title">${item.title}</div>
          <span class="badge-type badge-${item.type}">${item.type}</span>
        </div>
        <div class="item-card-sub">${item.genre} · ${item.year}</div>
      </div>
      <div class="item-card-body">
        <table class="meta-table">
          ${specific}
          <tr><td class="meta-key">Loan period</td><td class="meta-val">${item.loanDays} days</td></tr>
          <tr><td class="meta-key">Late fee</td><td class="meta-val">${item.penaltyMDL} MDL/day</td></tr>
        </table>
      </div>
      <div class="item-card-foot">
        ${availText}
        ${borrowBtn}
      </div>
    </div>`;
}

// ── Notification panel ──────────────────────────────────────────────────────

function setupNotifPanel() {
    const panel   = document.getElementById('notif-panel');
    const overlay = document.getElementById('overlay');
    const btn     = document.getElementById('notif-btn');
    const close   = document.getElementById('close-notif');

    btn.addEventListener('click', async () => {
        panel.classList.add('open');
        overlay.classList.add('show');
        await loadNotifications();
    });

    const closePanel = () => { panel.classList.remove('open'); overlay.classList.remove('show'); };
    close.addEventListener('click', closePanel);
    overlay.addEventListener('click', closePanel);
}

async function loadNotifications() {
    const list = document.getElementById('notif-list');
    try {
        const notifs = await Notifications.getAll();
        if (!notifs || notifs.length === 0) {
            list.innerHTML = '<div class="empty-state"><div class="icon">🔔</div><p>No notifications</p></div>';
            return;
        }
        list.innerHTML = notifs.map(notifItem).join('');
        list.querySelectorAll('.mark-read-btn').forEach(btn => {
            btn.addEventListener('click', async () => {
                await Notifications.markRead(btn.dataset.id);
                btn.closest('.notif-item').classList.remove('unread');
                btn.remove();
                // Update badge
                const badge = document.getElementById('notif-count');
                const current = parseInt(badge.textContent || '0');
                if (current > 1) { badge.textContent = current - 1; }
                else { badge.style.display = 'none'; }
                localStorage.setItem('unreadCount', Math.max(0, current - 1));
            });
        });
    } catch (err) {
        list.innerHTML = `<div class="empty-state"><p>${err.message}</p></div>`;
    }
}

function notifItem(n) {
    const unreadClass = !n.read ? ' unread' : '';
    const markBtn = !n.read
        ? `<span class="mark-read-btn notif-mark" data-id="${n.id}">Mark as read</span>` : '';
    return `<div class="notif-item${unreadClass}">
      <div class="notif-tag tag-${n.type}">${n.type.replace(/_/g,' ')}</div>
      <div class="notif-msg">${n.message}</div>
      <div class="notif-date">${n.createdAt}</div>
      ${markBtn}
    </div>`;
}
