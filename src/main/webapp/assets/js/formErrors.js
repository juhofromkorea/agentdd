/* 検査結果 {項目name: 日本語メッセージ} を入力欄の下へ表示する。 */
(function () {
    'use strict';
    let sequence = 0;
    const controls = form => Array.from(form.elements).filter(el => el.name && !el.disabled && !['hidden', 'submit', 'button'].includes(el.type));
    function values(form) {
        const result = {};
        controls(form).forEach(el => {
            if (!['radio', 'checkbox'].includes(el.type) || el.checked) result[el.name] = el.value;
        });
        return result;
    }
    function reveal(el) {
        const panel = el.closest('.estimate-panel');
        if (panel) {
            const tab = panel.classList.contains('estimate-panel--contract') ? 'contract' : 'coverage';
            document.getElementById('estimate-tab-' + tab).checked = true;
        }
        if (el.closest('.accident-card') && document.getElementById('accident-tab-reception')) {
            document.getElementById('accident-tab-reception').checked = true;
        }
    }
    function attach(form, validate) {
        form.noValidate = true; // 初期化成功後のみ、ブラウザ標準吹き出しを置き換える。
        const slots = new Map();
        let submitted = false, lastButton = null;
        function ensureSlots() {
            controls(form).forEach(el => {
                if (slots.has(el)) return;
                const slot = document.createElement('span');
                slot.className = 'field-error'; slot.id = 'field-error-' + (++sequence);
                slot.hidden = true; slot.setAttribute('aria-live', 'polite');
                const holder = el.closest('.form-field, .accident-affixed-control, .accounting-start-field, .estimate-field');
                (holder || el).insertAdjacentElement('afterend', slot);
                const described = new Set((el.getAttribute('aria-describedby') || '').split(/\s+/).filter(Boolean));
                described.add(slot.id); el.setAttribute('aria-describedby', [...described].join(' '));
                slots.set(el, slot);
            });
        }
        const summary = document.createElement('p');
        summary.className = 'field-error form-error'; summary.hidden = true;
        summary.tabIndex = -1; summary.setAttribute('role', 'alert');
        // estimateのformは補償タブ内なので、全体エラーはタブの外へ。
        const workspace = form.closest('.estimate-workspace');
        if (workspace) workspace.before(summary); else form.prepend(summary);
        function show(errors, focus = false) {
            ensureSlots();
            let first = null;
            slots.forEach((slot, el) => {
                const message = el.disabled ? '' : (errors[el.name] || '');
                slot.textContent = message; slot.hidden = !message;
                if (message) { el.setAttribute('aria-invalid', 'true'); first ||= el; }
                else el.removeAttribute('aria-invalid');
            });
            const unresolved = Object.keys(errors).some(name => name !== '_form' && !controls(form).some(el => el.name === name));
            summary.textContent = errors._form || (unresolved ? '入力内容を確認してください。' : '');
            summary.hidden = !summary.textContent;
            if (focus && first) { reveal(first); first.focus(); }
            else if (focus && !summary.hidden) summary.focus();
            return Object.keys(errors).length === 0;
        }
        form.addEventListener('submit', event => {
            if (event.defaultPrevented) return;
            lastButton = event.submitter;
            submitted = true;
            if (!show(validate(values(form), lastButton), true)) event.preventDefault();
        });
        // form属性で関連付けられた、formの外側にある入力も対象とする。
        function onEdit(event) {
            if (event.target.form !== form) return;
            if (submitted) show(validate(values(form), lastButton));
        }
        document.addEventListener('input', onEdit);
        document.addEventListener('change', onEdit);
        form.addEventListener('reset', () => {
            submitted = false; lastButton = null;
            show({});
            const loginError = document.getElementById('login-error-message');
            if (loginError && form.dataset.validation === 'login') { loginError.textContent = ''; loginError.hidden = true; }
        });
        const server = {};
        document.querySelectorAll('[data-server-error]').forEach(el => {
            if (el.dataset.errorForm === form.id) { server[el.dataset.serverError] = el.textContent.trim(); el.hidden = true; }
        });
        if (Object.keys(server).length) show(server, true);
        return { show, values: () => values(form) };
    }
    window.FormErrors = { attach, values, controls };
})();