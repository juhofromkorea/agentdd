package agentdd.model.util;

import agentdd.model.data.Contract;
import agentdd.model.data.Claim;

public class InsuranceCalc {
    public int insurancecalc(Contract contract, Claim claim, double vRate, double bRate, double pRate, double aRate) {

        
        // ② 画面から来た生のIDを使って、この計算クラス内で免許証の色の倍率を決定する
        // 1:ブルー, 2:グリーン, 3:ゴールド
        double licenseColorRate = 1.0;
        if (claim.getLicenseColor() == 3) {
            licenseColorRate = 0.7;
        } else if (claim.getLicenseColor() == 2) {
            licenseColorRate = 1.5;
        }

        // ③ 画面から来た生のIDを使って、この計算クラス内で年齢条件の倍率を決定する
        // 1:無制限, 2:21歳以上, 3:26歳以上
        double ageLimitRate = 1.0;
        if (claim.getAgeLimit() == 3) {
            ageLimitRate = 0.7;
        } else if (claim.getAgeLimit() == 1) {
            ageLimitRate = 1.5;
        }

        // ④ 車両保険金額を取得
        long basePrice = claim.getVehiclePrice();

        // ⑤ 算出要領の通りに掛け算する[cite: 1]
        // 車両保険金額 × (料率・車両 + 料率・対人 + 料率・対物 + 料率・傷害) × 12 × 免許証の色 × 年齢条件
        double totalRates = vRate + bRate + pRate + aRate;
        double calcResult = basePrice * totalRates * 12 * licenseColorRate * ageLimitRate;

        // ⑥ 小数点切り上げ（Math.ceil）してint型に変換[cite: 1]
        int totalPremium = (int) Math.ceil(calcResult);

        return totalPremium;
    }
}