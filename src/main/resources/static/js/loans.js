document.addEventListener('DOMContentLoaded', () => {
    requireAuth();
    document.getElementById('user-name').textContent = getUserName();
    loadLoans();
});

async function loadLoans() {
    try {
        const loans = await Loans.getMine();
        renderLoans(loans);
    } catch (err) { showToast(err.message, 'error'); }
}

function renderLoans(loans) {
    const tbody = document.getElementById('loans-tbody');
    if (!loans || loans.length === 0) {
        tbody.innerHTML = `<tr><td colspan="7">
          <div class="empty-state"><div class="icon">📋</div>
          <p>No loans yet. <a href="/catalog.html">Browse the catalog</a> to borrow something!</p></div>
        </td></tr>`;
        return;
    }

    // Sort: active/overdue first, then returned
    loans.sort((a, b) => {
        const order = { OVERDUE: 0, ACTIVE: 1, EXTENDED: 2, RETURNED: 3 };
        return (order[a.status] ?? 9) - (order[b.status] ?? 9);
    });

    tbody.innerHTML = loans.map(loanRow).join('');

    tbody.querySelectorAll('.return-btn').forEach(btn => {
        btn.addEventListener('click', async () => {
            const title   = btn.dataset.title;
            const penalty = parseFloat(btn.dataset.penalty) || 0;
            const penaltyLine = penalty > 0
                ? `\n⚠ Penalty to pay: ${penalty.toFixed(2)} MDL`
                : '\n✓ No penalty (returned on time)';
            if (!confirm(`Return "${title}"?${penaltyLine}`)) return;
            btn.disabled = true; btn.textContent = '…';
            try {
                const result = await Loans.return(btn.dataset.id);
                const finalPenalty = result.penaltyMDL > 0
                    ? ` Late fee: ${result.penaltyMDL.toFixed(2)} MDL` : '';
                showToast('Returned successfully!' + finalPenalty);
                loadLoans();
            } catch (err) {
                showToast(err.message, 'error');
                btn.disabled = false; btn.textContent = 'Return';
            }
        });
    });
}

function loanRow(loan) {
    const canReturn = ['ACTIVE','OVERDUE','EXTENDED'].includes(loan.status);
    const returnBtn = canReturn
        ? `<button class="btn btn-danger btn-sm return-btn"
             data-id="${loan.id}"
             data-title="${loan.itemTitle || loan.itemId}"
             data-penalty="${loan.currentPenaltyMDL || 0}">Return</button>`
        : '';

    const penaltyDisplay = buildPenaltyDisplay(loan);
    const isOverdue = loan.status === 'OVERDUE';
    const rowStyle  = isOverdue ? ' style="background:#fff5f5"' : '';

    return `<tr${rowStyle}>
      <td><strong>${loan.itemTitle || loan.itemId}</strong></td>
      <td><span class="badge-type badge-${loan.itemType}">${loan.itemType}</span></td>
      <td>${loan.borrowDate}</td>
      <td>${loan.dueDate}${isOverdue ? ' <span style="color:var(--danger);font-size:.75rem">⚠</span>' : ''}</td>
      <td><span class="badge-status badge-${loan.status}">${loan.status}</span></td>
      <td>${penaltyDisplay}</td>
      <td>${returnBtn}</td>
    </tr>`;
}

function buildPenaltyDisplay(loan) {
    // RETURNED → penalty definitiv (stocat în Excel)
    if (loan.status === 'RETURNED') {
        return loan.penaltyMDL > 0
            ? `<span class="penalty-val">${loan.penaltyMDL.toFixed(2)} MDL</span>`
            : '<span style="color:var(--success);font-weight:600">None</span>';
    }

    // Neretornat → afișează penalitatea live calculată de backend
    const p = parseFloat(loan.currentPenaltyMDL) || 0;
    if (p > 0) {
        return `<span class="penalty-val" title="Accumulating daily">${p.toFixed(2)} MDL</span>
                <small style="color:var(--muted);display:block;font-size:.7rem">+${rateFor(loan)}/day</small>`;
    }
    return '<span style="color:var(--success);font-weight:600">None</span>';
}

function rateFor(loan) {
    const rates = { BOOK: '1.50', MAGAZINE: '0.50', DVD: '2.00' };
    return (rates[loan.itemType] || '?') + ' MDL';
}
