(function () {
    'use strict';
    const D = DataCheck;
    const form = document.getElementById('coverage-form');
    if (!form) return;
    const maker = document.getElementById('makerSelect');
    const cars = document.getElementById('carNameSelect');
    const database = Array.from(cars.querySelectorAll('option[data-maker]')).map(option => ({ maker: option.dataset.maker, value: option.value, label: option.textContent }));
    [...new Set(database.map(car => car.maker))].forEach(name => maker.add(new Option(name, name)));
    function refreshCars() {
        const old = cars.value;
        cars.replaceChildren(new Option('選択してください', ''));
        database.filter(car => !maker.value || car.maker === maker.value).forEach(car => cars.add(new Option(car.label, car.value)));
        if (Array.from(cars.options).some(option => option.value === old)) cars.value = old;
    }
    maker.value = maker.dataset.initial || '';
    refreshCars();
    const initialCar = database.find(car => (car.value === cars.dataset.initial || car.label === cars.dataset.initial) && (!maker.value || car.maker === maker.value));
    if (initialCar) { maker.value = initialCar.maker; refreshCars(); cars.value = initialCar.value; }
    maker.addEventListener('change', refreshCars);
    cars.addEventListener('change', () => {
        const car = database.find(item => item.value === cars.value && (!maker.value || item.maker === maker.value));
        if (car) { maker.value = car.maker; refreshCars(); cars.value = car.value; }
    });
    function syncParty() {
        const corporate = document.getElementById('policyholder-corporation').checked;
        document.querySelectorAll('.estimate-field--personal input, .estimate-field--corporation input').forEach(el => {
            el.disabled = el.closest('.estimate-field--personal') ? corporate : !corporate;
        });
    }
    syncParty();
    document.querySelectorAll('[name="insuredKbn"]').forEach(el => el.addEventListener('change', syncParty));
    const fingerprint = () => JSON.stringify(Object.entries(FormErrors.values(form)).sort(([a], [b]) => a.localeCompare(b)));
    // 車両候補・個人法人の初期化が終わってから比較用の入力値を保存する。
    const baseline = fingerprint();
    const calculated = form.dataset.calculated === 'true';
    // 業務仕様の確定後、次の設定だけを追加する。未指定の規則を捏造しない。
    const policy = window.EstimateValidationPolicy || {};
    FormErrors.attach(form, (v, button) => {
        const action = new URL(button?.getAttribute('formaction') || form.action, location.href).pathname;
        const saving = action.endsWith('/tempSave');
        const printing = action.endsWith('/estimatestatus');
        const errors = {};
        const add = (name, ok, message) => { if (!ok && !errors[name]) errors[name] = message; };
        // 一時保存は未完成でよい。入力済みの値の形式は確認する。
        if (!saving) {
            const required = ['insuredKbn', 'nameKanji1', 'nameKana1', 'postcode', 'addressKanji1', 'addressKana1', 'inceptionDate', 'conclusionDate', 'inceptionTime', 'conclusionTime', 'paymentMethod', 'installment', 'maker', 'carName', 'licenseNo', 'licenseColor', 'ageLimit'];
            if (v.insuredKbn === '1') required.push('nameKanji2', 'nameKana2', 'gender', 'birthday');
            required.forEach(name => add(name, D.checkRequired(v[name]), '入力・選択してください。'));
            if (!D.checkPhoneNoFilledIn(v.telephoneNo, v.mobilephoneNo)) ['telephoneNo', 'mobilephoneNo'].forEach(name => add(name, false, '電話番号・携帯電話番号のどちらかを入力してください。'));
        }
        ['nameKana1', 'nameKana2'].forEach(name => {
            if (D.checkRequired(v[name])) add(name, D.checkCharacterType(v[name], 'katakana'), '全角カタカナで入力してください。');
        });
        ['inceptionDate', 'conclusionDate', 'birthday'].forEach(name => {
            if (D.checkRequired(v[name])) add(name, D.checkDate(v[name]), '有効な日付を入力してください。');
        });
        if (D.checkDate(v.inceptionDate) && D.checkDate(v.conclusionDate)) add('conclusionDate', D.checkInsurancePeriod(v.inceptionDate, v.conclusionDate), '満期日は始期日より後にしてください。');
        if (D.checkRequired(v.birthday)) add('birthday', D.checkBirthday(v.birthday), '生年月日は本日以前の日付にしてください。');
        if (D.checkRequired(v.postcode)) add('postcode', D.checkpostcode(v.postcode), '郵便番号は123-4567または1234567の形式で入力してください。');
        ['telephoneNo', 'mobilephoneNo', 'faxNo'].forEach(name => {
            if (D.checkRequired(v[name])) add(name, D.checkPhoneNoFormat(v[name]), '電話番号は半角数字10～11桁で入力してください（ハイフン可）。');
        });
        const candidates = { insuredKbn: ['1', '2'], gender: ['1', '2'], licenseColor: ['1', '2', '3'], ageLimit: ['1', '2', '3'], paymentMethod: ['1', '2', '3'], installment: ['1', '6', '12'], inceptionTime: ['09', '10', '11', '12', '13', '14', '15', '16', '17', '18'], conclusionTime: ['09', '10', '11', '12', '13', '14', '15', '16', '17', '18'] };
        Object.entries(candidates).forEach(([name, choices]) => {
            if (D.checkRequired(v[name])) add(name, D.checkClaimMaster(v[name], choices), '選択肢から選び直してください。');
        });
        if (D.checkRequired(v.maker)) add('maker', database.some(car => car.maker === v.maker), 'メーカーを選び直してください。');
        if (D.checkRequired(v.carName)) add('carName', database.some(car => car.value === v.carName && car.maker === v.maker), 'メーカーに対応する車名を選択してください。');
        // 保険契約者が運転者であるか・年齢の基準日は一覧だけでは確定しない。
        if (policy.ageBasis === 'inceptionDate' && v.insuredKbn === '1' && D.checkDate(v.birthday) && D.checkDate(v.inceptionDate) && D.checkRequired(v.ageLimit)) add('ageLimit', D.checkAgeLimit(v.birthday, v.ageLimit, v.inceptionDate), '始期日時点の年齢に合う年齢条件を選択してください。');
        if (D.checkTransfer(v.paymentMethod, v.installment, policy.paymentCombinations) === false) add('installment', false, '払込方法に対応する払込回数を選択してください。');
        if (D.checkRequired(v.licenseNo) && D.checkCarNumber(v.licenseNo, policy.licenseNumberPattern) === false) add('licenseNo', false, '車両番号の形式を確認してください。');
        FormErrors.controls(form).forEach(el => {
            if (el.maxLength > -1) add(el.name, D.checkLength(v[el.name], el.maxLength), el.maxLength + '文字以内で入力してください。');
            if (el.validity.badInput) add(el.name, false, '入力形式を確認してください。');
        });
        if (printing) {
            if (!D.checkEstimate(calculated)) errors._form = '先に保険料試算を実行してください。';
            else if (D.checkConditionChanged(baseline, fingerprint())) errors._form = '入力内容が変わりました。保険料を再試算してから印刷してください。';
        }
        // 5件制限はDAOが最新件数をロック付きで判断。画面件数だけで送信を禁止しない。
        return errors;
    });
    document.querySelectorAll('form[data-confirm-delete]').forEach(deleteForm => {
        deleteForm.addEventListener('submit', event => {
            if (!window.confirm('本当に削除してもよろしいですか？')) event.preventDefault();
        });
    });
})();