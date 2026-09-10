(function () {
    'use strict';
    const form = document.querySelector('[data-validation="login"]');
    if (!form) return;
    FormErrors.attach(form, values => {
        const errors = {};
        if (!DataCheck.checkRequired(values.userId)) errors.userId = 'ユーザーIDを入力してください。';
        // パスワードを勝手にtrimしたり、書き換えたりしない。
        if (!values.password) errors.password = 'パスワードを入力してください。';
        return errors;
    });
})();