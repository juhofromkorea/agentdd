(function () {
    'use strict';
    const D = DataCheck;
    document.querySelectorAll('[data-validation]').forEach(form => {
        const kind = form.dataset.validation;
        if (['login', 'estimate'].includes(kind)) return;
        FormErrors.attach(form, (v, button) => {
            const errors = {};
            const add = (name, ok, message) => { if (!ok && !errors[name]) errors[name] = message; };
            if (kind === 'accounting') {
                add('insatsuRenban', D.checkRequired(v.insatsuRenban), '印刷連番を入力してください。');
                add('insatsuRenban', D.checkSerialNumber(v.insatsuRenban), '印刷連番はAと半角数字7桁で入力してください。');
            } else if (['inquiry', 'cancel'].includes(kind)) {
                add('polNo', D.checkPolNoRequired(v.polNo), '証券番号を入力してください。');
                add('polNo', D.checkPolicyNumber(v.polNo), '証券番号はBと半角数字9桁で入力してください。');
            } else if (kind === 'accident-start') {
                const exclusive = D.checkReceptionNumber(v.polNo, v.claimNo);
                ['polNo', 'claimNo'].forEach(name => add(name, exclusive, '証券番号・事故受付番号のどちらか一方を入力してください。'));
                if (D.checkRequired(v.polNo)) add('polNo', D.checkPolicyNumber(v.polNo), '証券番号はBと半角数字9桁で入力してください。');
                if (D.checkRequired(v.claimNo)) add('claimNo', /^C\d{7}$/.test(D.text(v.claimNo)), '事故受付番号はCと半角数字7桁で入力してください。');
            } else if (kind === 'accident-detail') {
                const complete = !button || button.value === 'completeReceipt';
                if (complete) ['accidentDate', 'accidentLocationKanji1', 'accidentLocationKana1', 'accidentSituation'].forEach(name => add(name, D.checkRequired(v[name]), '入力してください。'));
                if (D.checkRequired(v.accidentDate)) add('accidentDate', /^\d{8}$/.test(D.text(v.accidentDate)) && D.checkAccidentDate(v.accidentDate, form.dataset.inceptionDate, form.dataset.conclusionDate), '事故日は有効な日付（YYYYMMDD）で、契約期間内かつ本日以前にしてください。');
                const a = D.text(v.ratingBlameMyself) || '0', b = D.text(v.ratingBlameYourself) || '0';
                add('ratingBlameMyself', D.checkRatingBlame(a), '過失割合は0～100の半角数字で入力してください。');
                add('ratingBlameYourself', D.checkRatingBlame(b), '過失割合は0～100の半角数字で入力してください。');
                const totalOK = D.checkFaultRateTotal(a, b) || (!complete && D.checkRatingBlame(a) && D.checkRatingBlame(b) && Number(a) + Number(b) === 0);
                ['ratingBlameMyself', 'ratingBlameYourself'].forEach(name => add(name, totalOK, complete ? '過失割合の合計を100にしてください。' : '合計を100にしてください。未定の場合は双方を空欄または0にしてください。'));
                ['Car', 'Bodily', 'Property', 'Accident'].forEach(type => {
                    const price = 'damage' + type + 'Price', state = 'damage' + type + 'State';
                    if (D.checkRequired(v[price])) add(price, D.checkAmount(v[price]), '損害額は0以上・18桁以内の半角数字で入力してください。');
                    if (!errors[price] && !D.checkDamage(v[price], v[state])) {
                        add(price, false, '損害額（1以上）と損害状況をセットで入力してください。');
                        add(state, false, '損害額（1以上）と損害状況をセットで入力してください。');
                    }
                });
            }
            FormErrors.controls(form).forEach(el => {
                if (el.maxLength > -1) add(el.name, D.checkLength(v[el.name], el.maxLength), el.maxLength + '文字以内で入力してください。');
            });
            return errors;
        });
    });
})();