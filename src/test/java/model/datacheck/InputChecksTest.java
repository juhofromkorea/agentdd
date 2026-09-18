
import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Proxy;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import agentdd.model.data.Contract;
import agentdd.model.datacheck.InputChecks;
import jakarta.servlet.http.HttpServletRequest;

/**
 * 入力チェッククラスのテストケース（モックライブラリ非使用）。
 * <p>
 * InputChecksの各静的メソッドについて単体テストを行う。
 * </p>
 * 
 * @author System 2026/09/11
 */
class InputChecksTest {

    /**
     * テスト対象クラス
     */
    private Contract dummyContract;

    /**
     * テストの前処理
     */
    @BeforeEach
    public void setUp() {
        dummyContract = new Contract();
        dummyContract.setInceptionDate("20260101");
        dummyContract.setConclusionDate("20261231");
    }

    /**
     * 新規試算チェック用の正常なリクエストパラメーターを生成する。
     */
    private Map<String, String[]> createValidEstimateParams() {
        Map<String, String[]> params = new HashMap<>();

        params.put("insuredKbn", new String[]{"1"});
        params.put("nameKanji1", new String[]{"東海"});
        params.put("nameKanji2", new String[]{"太郎"});
        params.put("nameKana1", new String[]{"トウカイ"});
        params.put("nameKana2", new String[]{"タロウ"});
        params.put("gender", new String[]{"1"});
        params.put("birthday", new String[]{"1990-01-01"});
        params.put("postcode", new String[]{"100-0001"});
        params.put("addressKanji1", new String[]{"東京都"});
        params.put("addressKana1", new String[]{"トウキョウト"});
        params.put("telephoneNo", new String[]{"03-0000-0000"});
        params.put("mobilephoneNo", new String[]{""});

        params.put("inceptionDate", new String[]{"2026-10-01"});
        params.put("inceptionTime", new String[]{"09"});
        params.put("conclusionDate", new String[]{"2027-10-01"});
        params.put("conclusionTime", new String[]{"18"});

        params.put("paymentMethod", new String[]{"1"});
        params.put("installment", new String[]{"12"});
        params.put("maker", new String[]{"トヨタ"});
        params.put("carName", new String[]{"プリウス"});
        params.put("licenseNo", new String[]{"123456789012"});
        params.put("licenseColor", new String[]{"1"});
        params.put("ageLimit", new String[]{"1"});

        return params;
    }

    /**
     * HttpServletRequestの簡易スタブを生成するヘルパーメソッド。
     * リクエストパラメーターのマップを受け取り、必要な値のみを返却するリクエストを構築します。
     */
    private HttpServletRequest createDummyRequest(Map<String, String[]> parameters) {
        return (HttpServletRequest) Proxy.newProxyInstance(
            HttpServletRequest.class.getClassLoader(),
            new Class<?>[] { HttpServletRequest.class },
            (proxy, method, args) -> {
                String methodName = method.getName();
                if ("getParameterValues".equals(methodName) && args.length > 0) {
                    String key = (String) args[0];
                    return parameters.get(key);
                }
                if ("getParameter".equals(methodName) && args.length > 0) {
                    String key = (String) args[0];
                    String[] values = parameters.get(key);
                    return (values != null && values.length > 0) ? values[0] : null;
                }
                return null;
            }
        );
    }

    // ==========================================
    // text(String) のテスト
    // ==========================================

    @Test
    void text_normal() {
        // 期待値
        String expected = "hello";
        // メソッド呼び出し
        String actual = InputChecks.text("  hello  ");
        // 検証
        assertEquals(expected, actual);
    }

    @Test
    void text_null() {
        // 期待値
        String expected = "";
        // メソッド呼び出し
        String actual = InputChecks.text(null);
        // 検証
        assertEquals(expected, actual);
    }

    @Test
    void text_blank() {
        // 期待値
        String expected = "";
        // メソッド呼び出し
        String actual = InputChecks.text("   ");
        // 検証
        assertEquals(expected, actual);
    }

    // ==========================================
    // date(String) のテスト
    // ==========================================

    @Test
    void date_valid_8digits() {
        // 期待値
        LocalDate expected = LocalDate.of(2026, 7, 3);
        // メソッド呼び出し
        LocalDate actual = InputChecks.date("20260703");
        // 検証
        assertEquals(expected, actual);
    }

    @Test
    void date_valid_hyphenated() {
        // 期待値
        LocalDate expected = LocalDate.of(2026, 7, 3);
        // メソッド呼び出し
        LocalDate actual = InputChecks.date("2026-07-03");
        // 検証
        assertEquals(expected, actual);
    }

    @Test
    void date_invalid_format() {
        // 期待値
        LocalDate expected = null;
        // メソッド呼び出し
        LocalDate actual = InputChecks.date("2026/07/03");
        // 検証
        assertEquals(expected, actual);
    }

    @Test
    void date_invalid_calendar_date() {
        // 存在しない日付（2025年2月29日）
        LocalDate expected = null;
        // メソッド呼び出し
        LocalDate actual = InputChecks.date("20250229");
        // 検証
        assertEquals(expected, actual);
    }

    @Test
    void date_null() {
        // 期待値
        LocalDate expected = null;
        // メソッド呼び出し
        LocalDate actual = InputChecks.date(null);
        // 検証
        assertEquals(expected, actual);
    }

    // ==========================================
    // login(String, String) のテスト
    // ==========================================

    @Test
    void login_valid() {
        // メソッド呼び出し
        Map<String, String> errors = InputChecks.login("user01", "password123");
        // 検証
        assertTrue(errors.isEmpty());
    }

    @Test
    void login_userId_null_and_password_null() {
        // メソッド呼び出し
        Map<String, String> errors = InputChecks.login(null, null);
        // 検証
        assertEquals("ユーザーIDを入力してください。", errors.get("userId"));
        assertEquals("パスワードを入力してください。", errors.get("password"));
    }

    @Test
    void login_userId_blank_and_password_empty() {
        // メソッド呼び出し
        Map<String, String> errors = InputChecks.login("   ", "");
        // 検証
        assertEquals("ユーザーIDを入力してください。", errors.get("userId"));
        assertEquals("パスワードを入力してください。", errors.get("password"));
    }

    // ==========================================
    // estimateValues(HttpServletRequest) のテスト
    // ==========================================

    @Test
    void estimateValues_individual() {
        // パラメーター設定
        Map<String, String[]> params = new HashMap<>();
        params.put("insuredKbn", new String[]{"1"});
        params.put("nameKanji1", new String[]{"東海"});
        params.put("nameKanji2", new String[]{"太郎"});

        HttpServletRequest request = createDummyRequest(params);

        // メソッド呼び出し
        Map<String, String> result = InputChecks.estimateValues(request);

        // 検証
        assertEquals("1", result.get("insuredKbn"));
        assertEquals("東海", result.get("nameKanji1"));
        assertEquals("太郎", result.get("nameKanji2"));
    }

    @Test
    void estimateValues_corporation() {
        // 法人（insuredKbn = "2"）の場合の挙動確認
        Map<String, String[]> params = new HashMap<>();
        params.put("insuredKbn", new String[]{"2"});
        params.put("nameKanji1", new String[]{"旧社名", "株式会社サンプル"});
        params.put("nameKanji2", new String[]{"担当者名"});
        params.put("gender", new String[]{"1"});

        HttpServletRequest request = createDummyRequest(params);

        // メソッド呼び出し
        Map<String, String> result = InputChecks.estimateValues(request);

        // 検証（法人の場合、nameKanji1は配列の末尾が採用され、nameKanji2やgenderは空文字になる）
        assertEquals("2", result.get("insuredKbn"));
        assertEquals("株式会社サンプル", result.get("nameKanji1"));
        assertEquals("", result.get("nameKanji2"));
        assertEquals("", result.get("gender"));
    }

    // ==========================================
    // estimateSnapshot(HttpServletRequest) のテスト
    // ==========================================

    @Test
    void estimateSnapshot_hyphen_removed() {
        // パラメーター設定
        Map<String, String[]> params = new HashMap<>();
        params.put("insuredKbn", new String[]{"1"});
        params.put("postcode", new String[]{"100-0001"});
        params.put("telephoneNo", new String[]{"03-1234-5678"});

        HttpServletRequest request = createDummyRequest(params);

        // メソッド呼び出し
        Map<String, String> result = InputChecks.estimateSnapshot(request);

        // 検証（ハイフンが除去されていること）
        assertEquals("1000001", result.get("postcode"));
        assertEquals("0312345678", result.get("telephoneNo"));
    }

    // ==========================================
    // estimate(HttpServletRequest, boolean) のテスト
    // ==========================================

    @Test
    void estimate_success() {
        // 正常な入力パラメーターを設定
        Map<String, String[]> params = new HashMap<>();
        params.put("insuredKbn", new String[]{"1"});
        params.put("nameKanji1", new String[]{"東海"});
        params.put("nameKanji2", new String[]{"太郎"});
        params.put("nameKana1", new String[]{"トウカイ"});
        params.put("nameKana2", new String[]{"タロウ"});
        params.put("gender", new String[]{"1"});
        params.put("birthday", new String[]{"1990-01-01"});
        params.put("postcode", new String[]{"100-0001"});
        params.put("addressKanji1", new String[]{"東京都"});
        params.put("addressKana1", new String[]{"トウキョウト"});
        params.put("telephoneNo", new String[]{"03-0000-0000"});
        params.put("mobilephoneNo", new String[]{""});
        params.put("inceptionDate", new String[]{"2026-10-01"});
        params.put("inceptionTime", new String[]{"09"});
        params.put("conclusionDate", new String[]{"2027-10-01"});
        params.put("conclusionTime", new String[]{"18"});
        params.put("paymentMethod", new String[]{"1"});
        params.put("installment", new String[]{"12"});
        params.put("maker", new String[]{"トヨタ"});
        params.put("carName", new String[]{"プリウス"});
        params.put("licenseNo", new String[]{"123456789012"});
        params.put("licenseColor", new String[]{"1"});
        params.put("ageLimit", new String[]{"1"});

        HttpServletRequest request = createDummyRequest(params);

        // メソッド呼び出し
        Map<String, String> errors = InputChecks.estimate(request, false);

        // 検証
        assertTrue(errors.isEmpty());
    }

    @Test
    void estimate_required_error() {
        // 空のパラメーター
        Map<String, String[]> params = new HashMap<>();
        params.put("insuredKbn", new String[]{"1"});

        HttpServletRequest request = createDummyRequest(params);

        // メソッド呼び出し（draft = false）
        Map<String, String> errors = InputChecks.estimate(request, false);

        // 検証
        assertTrue(errors.containsKey("nameKanji1"));
        assertTrue(errors.containsKey("postcode"));
        assertTrue(errors.containsKey("telephoneNo"));
        assertEquals("入力・選択してください。", errors.get("nameKanji1"));
        assertEquals("電話番号・携帯電話番号のどちらかを入力してください。", errors.get("telephoneNo"));
    }

    @Test
    void estimate_invalid_format_error() {
        // フォーマットエラーのあるパラメーター
        Map<String, String[]> params = new HashMap<>();
        params.put("insuredKbn", new String[]{"1"});
        params.put("nameKana1", new String[]{"漢字"});
        params.put("inceptionDate", new String[]{"2026-13-45"});
        params.put("postcode", new String[]{"1234"});

        HttpServletRequest request = createDummyRequest(params);

        // メソッド呼び出し（draft = true）
        Map<String, String> errors = InputChecks.estimate(request, true);

        // 検証
        assertEquals("全角カタカナで入力してください。", errors.get("nameKana1"));
        assertEquals("有効な日付を入力してください。", errors.get("inceptionDate"));
        assertEquals("郵便番号は123-4567または1234567の形式で入力してください。", errors.get("postcode"));
    }

    @Test
    void estimate_date_relation_error() {
        // 満期日が始期日より前
        Map<String, String[]> params = new HashMap<>();
        params.put("insuredKbn", new String[]{"1"});
        params.put("inceptionDate", new String[]{"2026-10-01"});
        params.put("conclusionDate", new String[]{"2026-09-01"});

        HttpServletRequest request = createDummyRequest(params);

        // メソッド呼び出し
        Map<String, String> errors = InputChecks.estimate(request, true);

        // 検証
        assertEquals("満期日は始期日より後にしてください。", errors.get("conclusionDate"));
    }

    // ==========================================
    // accident(Map, Contract, boolean) のテスト
    // ==========================================

    @Test
    void accident_valid_complete() {
        Map<String, String> v = Map.of(
            "accidentDate", "20260501",
            "accidentLocationKanji1", "東京都千代田区",
            "accidentLocationKana1", "トウキョウトチヨダク",
            "accidentSituation", "交差点での接触事故",
            "ratingBlameMyself", "80",
            "ratingBlameYourself", "20",
            "damageCarPrice", "100000",
            "damageCarState", "フロントバンパー破損"
        );

        // メソッド呼び出し
        Map<String, String> errors = InputChecks.accident(v, dummyContract, true);

        // 検証
        assertTrue(errors.isEmpty());
    }

    @Test
    void accident_rating_sum_invalid() {
        // 過失割合の合計が100にならない（80 + 30 = 110）
        Map<String, String> v = Map.of(
            "accidentDate", "20260501",
            "ratingBlameMyself", "80",
            "ratingBlameYourself", "30"
        );

        // メソッド呼び出し
        Map<String, String> errors = InputChecks.accident(v, dummyContract, true);

        // 検証
        assertEquals("過失割合の合計を100にしてください。", errors.get("ratingBlameMyself"));
        assertEquals("過失割合の合計を100にしてください。", errors.get("ratingBlameYourself"));
    }

    @Test
    void accident_damage_pair_unmatched() {
        // 損害額のみ指定して損害状況が空欄
        Map<String, String> v = Map.of(
            "damageCarPrice", "100000",
            "damageCarState", ""
        );

        // メソッド呼び出し
        Map<String, String> errors = InputChecks.accident(v, dummyContract, false);

        // 検証
        assertEquals("損害額（1以上）と損害状況をセットで入力してください。", errors.get("damageCarPrice"));
        assertEquals("損害額（1以上）と損害状況をセットで入力してください。", errors.get("damageCarState"));
    }

    @Test
    void accident_date_out_of_range() {
        // 契約開始前の日付
        Map<String, String> v = Map.of("accidentDate", "20251231");

        // メソッド呼び出し
        Map<String, String> errors = InputChecks.accident(v, dummyContract, false);

        // 検証
        assertEquals("事故日は有効な日付（YYYYMMDD）で、契約期間内かつ本日以前にしてください。", errors.get("accidentDate"));
    }

/**
     * テスト対象で使用するエラーマップ
     */
    private Map<String, String> errors;

    /**
     * テストの前処理
     */
    @BeforeEach
    public void setUp1() {
        errors = new HashMap<>();
    }

    // ==========================================
    // add(Map, String, boolean, String) のテスト
    // ==========================================

    @Test
    void add_valid_true() {
        // メソッド呼び出し（valid = true のためエラー追加されない）
        InputChecks.add(errors, "insatsuRenban", true, "印刷連番を入力してください。");

        // 検証
        assertTrue(errors.isEmpty());
    }

    @Test
    void add_valid_false_new_key() {
        // メソッド呼び出し（valid = false かつキーが存在しないため追加される）
        InputChecks.add(errors, "insatsuRenban", false, "印刷連番を入力してください。");

        // 検証
        assertEquals(1, errors.size());
        assertEquals("印刷連番を入力してください。", errors.get("insatsuRenban"));
    }

    @Test
    void add_valid_false_existing_key() {
        // 事前条件：すでに最初のエラーメッセージがセットされている
        errors.put("insatsuRenban", "最初のメッセージ");

        // メソッド呼び出し（同じキーで別のエラーを追加しようとする）
        InputChecks.add(errors, "insatsuRenban", false, "後から追加しようとしたメッセージ");

        // 検証（上書きされず、最初のエラーメッセージが保持されていること）
        assertEquals(1, errors.size());
        assertEquals("最初のメッセージ", errors.get("insatsuRenban"));
    }

    @Test
    void add_valid_false_multiple_keys() {
        // 事前条件：別項目のエラーが存在する
        errors.put("userId", "ユーザーIDを入力してください。");

        // メソッド呼び出し（別のキーでエラーを追加）
        InputChecks.add(errors, "password", false, "パスワードを入力してください。");

        // 検証（要素数が2になり、両方のエラーが保持されていること）
        assertEquals(2, errors.size());
        assertEquals("ユーザーIDを入力してください。", errors.get("userId"));
        assertEquals("パスワードを入力してください。", errors.get("password"));
    }

    @Test
    void estimate_age_limit_error() {
        Map<String, String[]> params = createValidEstimateParams();

        params.put("birthday", new String[]{"2005-01-01"});
        params.put("inceptionDate", new String[]{"2026-10-01"});
        params.put("ageLimit", new String[]{"3"});

        Map<String, String> errors =
                InputChecks.estimate(
                        createDummyRequest(params),
                        false);

        assertEquals(
                "年齢条件と生年月日が一致していません。"
                        + "年齢条件または生年月日を見直してください。",
                errors.get("birthday"));
    }

    @Test
    void estimate_minimum_driving_age_error() {
        Map<String, String[]> params = createValidEstimateParams();

        params.put("birthday", new String[]{"2015-01-01"});
        params.put("ageLimit", new String[]{"1"});

        Map<String, String> errors =
                InputChecks.estimate(
                        createDummyRequest(params),
                        false);

        assertEquals(
                "運転免許を取得できる年齢に達していません。",
                errors.get("birthday"));
    }

    @Test
    void accident_complete_requires_positive_damage() {
        Map<String, String> values = new HashMap<>();

        values.put("accidentDate", "20260501");
        values.put("accidentLocationKanji1", "東京都");
        values.put("accidentLocationKana1", "トウキョウト");
        values.put("accidentSituation", "接触事故");
        values.put("ratingBlameMyself", "50");
        values.put("ratingBlameYourself", "50");

        values.put("damageCarPrice", "0");
        values.put("damageBodilyPrice", "0");
        values.put("damagePropertyPrice", "0");
        values.put("damageAccidentPrice", "0");

        Map<String, String> errors =
                InputChecks.accident(
                        values,
                        dummyContract,
                        true);

        assertEquals(
                "事故受付完了時は、いずれか1つの損害額を1円以上で入力してください。",
                errors.get("_damage"));
    }

    @Test
    void accident_update_allows_zero_damage() {
        Map<String, String> values = new HashMap<>();

        values.put("ratingBlameMyself", "0");
        values.put("ratingBlameYourself", "0");
        values.put("damageCarPrice", "0");
        values.put("damageBodilyPrice", "0");
        values.put("damagePropertyPrice", "0");
        values.put("damageAccidentPrice", "0");

        Map<String, String> errors =
                InputChecks.accident(
                        values,
                        dummyContract,
                        false);

        assertFalse(errors.containsKey("_damage"));
    }
}