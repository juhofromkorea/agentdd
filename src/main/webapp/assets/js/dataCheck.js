/* JavaScript一覧 DC0001～DC0030: 判定だけを担当し、DOMやDBを操作しない。 */
(function (root) {
    'use strict';
    const text = value => String(value ?? '').trim();
    const required = value => text(value) !== '';
    function date(value) {
        const raw = text(value);
        if (!/^(?:\d{8}|\d{4}-\d{2}-\d{2})$/.test(raw)) return null;
        const digits = raw.replaceAll('-', '');
        const y = Number(digits.slice(0, 4)), m = Number(digits.slice(4, 6)), d = Number(digits.slice(6, 8));
        const result = new Date(0);
        result.setFullYear(y, m - 1, d);
        result.setHours(0, 0, 0, 0);
        return y > 0 && result.getFullYear() === y && result.getMonth() === m - 1 && result.getDate() === d ? result : null;
    }
    function amount(value, max = '999999999999999999') {
        return /^\d{1,18}$/.test(text(value)) && BigInt(text(value)) <= BigInt(max);
    }
    const D = {
        text, date,
        checkReceptionNumber: (polNo, claimNo) => required(polNo) !== required(claimNo), // 01
        checkRequired: required, // 02
        checkFaultRateTotal: (a, b) => D.checkRatingBlame(a) && D.checkRatingBlame(b) && Number(a) + Number(b) === 100, // 03
        checkEstimate: calculated => calculated === true, // 04
        // 05/06/25/28: 引数はControllerのDB判定結果。ブラウザだけではDB照合できない。
        checkPrintSeqExists: exists => exists === true,
        checkPrintSeqStatus: status => Number(status) === 1,
        checkPolNoRequired: required, // 07
        checkTempSaveCount: count => Number.isInteger(Number(count)) && Number(count) >= 0 && Number(count) < 5, // 08 新規追加時
        checkLength: (value, max) => String(value ?? '').length <= max, // 09 Java String.lengthと統一
        checkCharacterType(value, type) { // 10 使用する項目・文字種は画面仕様に合わせる
            const patterns = { digits: /^\d+$/, alphanumeric: /^[A-Za-z0-9]+$/, katakana: /^[ァ-ヺー・\u3000 ]+$/u };
            return !!patterns[type] && patterns[type].test(String(value ?? ''));
        },
        checkDate: value => date(value) !== null, // 11
        checkInsurancePeriod: (start, end) => !!date(start) && !!date(end) && date(end) > date(start), // 12
        checkBirthday: (birthday, today = new Date()) => !!date(birthday) && date(birthday) <= today, // 13
        checkpostcode: value => /^(?:\d{7}|\d{3}-\d{4})$/.test(text(value)), // 14 DBの7桁表示とも互換
        checkPhoneNoFormat: value => /^(?:\d{10,11}|\d{2,5}-\d{1,4}-\d{4})$/.test(text(value)) && /^(?:\d{10}|\d{11})$/.test(text(value).replaceAll('-', '')), // 15
        checkPhoneNoFilledIn: (tel, mobile) => required(tel) || required(mobile), // 16
        checkAmount: amount, // 17 BigIntで18桁を正確に比較
        checkRatingBlame: value => amount(value, 100), // 18
        checkContractItem: (values, names) => names.every(name => required(values[name])), // 19
        checkAgeLimit(birthday, ageLimit, atDate) { // 20 基準日は呼出側が渡す
            const birth = date(birthday), at = date(atDate);
            const minimum = { 1: 0, 2: 21, 3: 26 }[ageLimit];
            if (!birth || !at || minimum === undefined || birth > at) return false;
            const age = at.getFullYear() - birth.getFullYear() - (at.getMonth() < birth.getMonth() || (at.getMonth() === birth.getMonth() && at.getDate() < birth.getDate()) ? 1 : 0);
            return age >= minimum;
        },
        // 21/22: 一覧に具体的な規則がない。設定未提供時はnull（判定保留）を返す。
        checkTransfer: (method, count, combinations) => combinations ? (combinations[method] || []).map(String).includes(String(count)) : null,
        checkCarNumber: (value, pattern) => pattern ? new RegExp(pattern).test(text(value)) : null,
        checkClaimMaster: (value, options) => options.map(String).includes(String(value)), // 23 ブラウザ側候補検査
        checkConditionChanged: (before, after) => before !== after, // 24 trueなら再試算が必要
        checkDataValid: exists => exists === true, // 25
        checkSerialNumber: value => /^A\d{7}$/.test(text(value)), // 26
        checkPolicyNumber: value => /^B\d{9}$/.test(text(value)), // 27
        checkPllicyNumberExist: exists => exists === true, // 28 一覧の綴りに対応
        checkAccidentDate(value, start, end, today = new Date()) { // 29 日単位。時刻境界は別途仕様が必要
            const d = date(value), s = date(start), e = date(end);
            return !!d && !!s && !!e && d >= s && d <= e && d <= today;
        },
        checkDamage: (price, state) => (required(price) && amount(price) && BigInt(text(price)) > 0n) === required(state) // 30 現行モデルの0=未入力と統一
    };
    D.checkAgeIimit = D.checkAgeLimit; // 一覧の綴りに対する互換エイリアス
    if (typeof module !== 'undefined' && module.exports) module.exports = D;
    else root.DataCheck = Object.freeze(D);
})(globalThis);