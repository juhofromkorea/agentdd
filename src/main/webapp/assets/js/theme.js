/* headでdeferなし。初期描画前にテーマを適用し、イベントは一度だけ登録。 */
(function () {
    'use strict';

    let dark = false;

    try { 
        dark = localStorage.getItem('appTheme') === 'dark'; 
    } catch (_) { 
        /* 保存禁止でも画面を利用可能 */
    }

    document.documentElement.classList.toggle('dark-mode', dark);

    document.addEventListener('DOMContentLoaded', () => {
        const toggle = document.getElementById('theme-toggle');

        if (!toggle) return;

        toggle.checked = dark;
        
        toggle.addEventListener('change', () => {
            document.documentElement.classList.toggle(
                'dark-mode', toggle.checked);
            try { 
                localStorage.setItem(
                    'appTheme', toggle.checked ? 'dark' : 'light'
                ); 
            } catch (_) { 
                /* 保存のみ省略 */ 
            }
        });
    });
})();