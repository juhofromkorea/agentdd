package agentdd.model.datacheck;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.LinkedHashMap;
import java.util.Map;
import jakarta.servlet.http.HttpServletRequest;
import agentdd.model.data.Contract;

/** JSを通らないリクエストにも適用する、入力値の検査。DB検査はController/DAOに残す。 */
public final class InputChecks {

    private InputChecks() { 

    }

    public static String text(String value) { 
        return value == null ? "" : value.trim(); 
    }

    private static void add(Map<String, String> errors, String key, boolean valid, String message) {
        if (!valid) errors.putIfAbsent(key, message);
    }

    public static LocalDate date(String value) {
        String s = text(value);
        if (!s.matches("(?:[0-9]{8}|[0-9]{4}-[0-9]{2}-[0-9]{2})")) {
            return null;
        }
        try { 
            return LocalDate.parse(s.replace("-", ""), DateTimeFormatter.BASIC_ISO_DATE); 
        }
        catch (DateTimeParseException e) { 
            return null; 
        }
    }

    private static boolean amount(String value, long max) {
        String s = text(value);

        if (!s.matches("[0-9]{1,18}")) {
            return false;
        }

        return Long.parseLong(s) <= max;
    }

    private static final String[] ESTIMATE_FIELDS = {
        "insuredKbn", "nameKanji1", "nameKanji2", "nameKana1", "nameKana2", "gender", "birthday",
        "postcode", "addressKanji1", "addressKanji2", "addressKana1", "addressKana2",
        "telephoneNo", "mobilephoneNo", "faxNo", "inceptionDate", "inceptionTime",
        "conclusionDate", "conclusionTime", "paymentMethod", "installment", "maker", "carName",
        "licenseNo", "licenseColor", "ageLimit"
    };

    public static Map<String, String> login(
            String userId,
            String password) {

        Map<String, String> errors = new LinkedHashMap<>();

        if (userId == null || userId.isBlank()) {
            errors.put("userId", "ユーザーIDを入力してください。");
        }

        if (password == null || password.isEmpty()) {
            errors.put("password", "パスワードを入力してください。");
        }

        return errors;
    }

    /** 比較対象は業務項目のみ。タブラジオや送信ボタンを含めない。 */
    public static Map<String, String> estimateValues(HttpServletRequest request) {
        Map<String, String> result = new LinkedHashMap<>();
        boolean corporation = "2".equals(request.getParameter("insuredKbn"));
        for (String name : ESTIMATE_FIELDS) {
            String[] all = request.getParameterValues(name);
            String value = all == null || all.length == 0 ? "" : all[0];
            if (corporation && (name.equals("nameKanji1") || name.equals("nameKana1")) && all != null && all.length > 1) value = all[all.length - 1];
            if (corporation && (name.equals("nameKanji2") || name.equals("nameKana2") || name.equals("gender") || name.equals("birthday"))) value = "";
            result.put(name, text(value));
        }
        return result;
    }

    public static Map<String, String> estimateSnapshot(HttpServletRequest request) {
        Map<String, String> result = estimateValues(request);
        for (String name : new String[] {"birthday", "inceptionDate", "conclusionDate", "postcode", "telephoneNo", "mobilephoneNo", "faxNo"}) {
            result.put(name, result.get(name).replace("-", ""));
        }
        return result;
    }

    public static Map<String, String> estimate(HttpServletRequest request, boolean draft) {
        
        Map<String, String> v = estimateValues(request), e = new LinkedHashMap<>();
        
        if (!draft) {
            for (String name : new String[] {"insuredKbn", "nameKanji1", "nameKana1", "postcode", "addressKanji1", "addressKana1", "inceptionDate", "conclusionDate", "inceptionTime", "conclusionTime", "paymentMethod", "installment", "maker", "carName", "licenseNo", "licenseColor", "ageLimit"}) {
                add(e, name, !v.get(name).isEmpty(), "入力・選択してください。");
            }
            if ("1".equals(v.get("insuredKbn"))) {
                for (String name : new String[] {"nameKanji2", "nameKana2", "gender", "birthday"}) add(e, name, !v.get(name).isEmpty(), "入力・選択してください。");
            }
            boolean phone = !v.get("telephoneNo").isEmpty() || !v.get("mobilephoneNo").isEmpty();
            add(e, "telephoneNo", phone, "電話番号・携帯電話番号のどちらかを入力してください。");
            add(e, "mobilephoneNo", phone, "電話番号・携帯電話番号のどちらかを入力してください。");
        }

        for (String name : new String[] {"nameKana1", "nameKana2"}) {
            if (!v.get(name).isEmpty()) {
                add(e, name, v.get(name).matches("[ァ-ヺー・　 ]+"), "全角カタカナで入力してください。");
            }
        }

        Map<String, String> enums = Map.of("insuredKbn", "[12]", "gender", "[12]", "licenseColor", "[123]", "ageLimit", "[123]", "paymentMethod", "[123]", "installment", "(?:1|6|12)", "inceptionTime", "(?:09|1[0-8])", "conclusionTime", "(?:09|1[0-8])");
        
        enums.forEach((name, pattern) -> { 
            if (!v.get(name).isEmpty()) {
                add(e, name, v.get(name).matches(pattern), "選択肢から選び直してください。");
            }});
        
        for (String name : new String[] {"inceptionDate", "conclusionDate", "birthday"}) {
            if (!v.get(name).isEmpty()) {
                add(e, name, date(v.get(name)) != null, "有効な日付を入力してください。");
            }
        }

        LocalDate start = date(v.get("inceptionDate")), end = date(v.get("conclusionDate")), birthday = date(v.get("birthday"));
        if (start != null && end != null) {
            add(e, "conclusionDate", end.isAfter(start), "満期日は始期日より後にしてください。");
        }
        if (birthday != null) {
            add(e, "birthday", !birthday.isAfter(LocalDate.now()), "生年月日は本日以前の日付にしてください。");
        }
        if (!v.get("postcode").isEmpty()) {
            add(e, "postcode", v.get("postcode").matches("(?:[0-9]{7}|[0-9]{3}-[0-9]{4})"), "郵便番号は123-4567または1234567の形式で入力してください。");
        }
        for (String name : new String[] {"telephoneNo", "mobilephoneNo", "faxNo"}) {
            String s = v.get(name);
            if (!s.isEmpty()) {
                add(e, name, s.matches("(?:[0-9]{10,11}|[0-9]{2,5}-[0-9]{1,4}-[0-9]{4})") && s.replace("-", "").matches("[0-9]{10,11}"), "電話番号は半角数字10～11桁で入力してください（ハイフン可）。");
            }
        }
        return e;
    }

    public static Map<String, String> accident(Map<String, String> v, Contract contract, boolean complete) {
        Map<String, String> e = new LinkedHashMap<>();
        if (complete) for (String name : new String[] {"accidentDate", "accidentLocationKanji1", "accidentLocationKana1", "accidentSituation"}) add(e, name, !text(v.get(name)).isEmpty(), "入力してください。");
        
        if (!text(v.get("accidentDate")).isEmpty()) {
            LocalDate d = date(v.get("accidentDate")), start = date(contract.getInceptionDate()), end = date(contract.getConclusionDate());
            add(e, "accidentDate", text(v.get("accidentDate")).matches("[0-9]{8}") && d != null && start != null && end != null && !d.isBefore(start) && !d.isAfter(end) && !d.isAfter(LocalDate.now()), "事故日は有効な日付（YYYYMMDD）で、契約期間内かつ本日以前にしてください。");
        }

        String mine = text(v.get("ratingBlameMyself")), theirs = text(v.get("ratingBlameYourself"));

        if (mine.isEmpty()) mine = "0";
        if (theirs.isEmpty()) theirs = "0";
        add(e, "ratingBlameMyself", amount(mine, 100), "過失割合は0～100の半角数字で入力してください。");
        add(e, "ratingBlameYourself", amount(theirs, 100), "過失割合は0～100の半角数字で入力してください。");
        
        if (amount(mine, 100) && amount(theirs, 100)) {
            long total = Long.parseLong(mine) + Long.parseLong(theirs);
            boolean valid = total == 100 || (!complete && total == 0);
            for (String name : new String[] {"ratingBlameMyself", "ratingBlameYourself"}) add(e, name, valid, complete ? "過失割合の合計を100にしてください。" : "合計を100にしてください。未定の場合は双方を空欄または0にしてください。");
        }
        
        for (String type : new String[] {"Car", "Bodily", "Property", "Accident"}) {
            String price = "damage" + type + "Price", state = "damage" + type + "State", s = text(v.get(price));
            boolean valid = s.isEmpty() || amount(s, 999_999_999_999_999_999L);
            add(e, price, valid, "損害額は0以上・18桁以内の半角数字で入力してください。");
            if (valid) {
                boolean paired = (!s.isEmpty() && Long.parseLong(s) > 0) == !text(v.get(state)).isEmpty();
                for (String name : new String[] {price, state}) add(e, name, paired, "損害額（1以上）と損害状況をセットで入力してください。");
            }
        }

        for (String name : new String[] {"accidentLocationKanji1", "accidentLocationKanji2", "accidentLocationKana1", "accidentLocationKana2", "damageCarState", "damageBodilyState", "damagePropertyState", "damageAccidentState"}) add(e, name, v.getOrDefault(name, "").length() <= 48, "48文字以内で入力してください。");
        add(e, "accidentSituation", v.getOrDefault("accidentSituation", "").length() <= 100, "100文字以内で入力してください。");
        
        return e;
    }
}