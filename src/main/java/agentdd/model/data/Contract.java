package agentdd.model.data;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.chrono.JapaneseDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class Contract {

    private String insatsuRenban;
    private String polNo;
    private Integer statusFlg;
    private boolean cancelFlg;
    private String inceptionDate;
    private String inceptionTime;
    private String conclusionDate;
    private String conclusionTime;
    private Integer paymentMethod;
    private Integer installment;
    private Integer insuredKbn;
    private String nameKana1;
    private String nameKana2;
    private String nameKanji1;
    private String nameKanji2;
    private String postcode;
    private String addressKana1;
    private String addressKana2;
    private String addressKanji1;
    private String addressKanji2;
    private String birthday;
    private Integer gender;
    private String telephoneNo;
    private String mobilephoneNo;
    private String faxNo;

    /** マスタテーブル用メンバ変数 */
    private String paymentStr;
    private String statusStr;
    private String insuredStr;
    private String genderStr;

    // 和暦表示用フォーマッター
    private static final DateTimeFormatter JAPANESE_DATE_FORMATTER =
            DateTimeFormatter.ofPattern("Gy年M月d日", Locale.JAPAN);
    
    // 午前・午後表示用フォーマッター
    private static final DateTimeFormatter JAPANESE_TIME_FORMATTER =
            DateTimeFormatter.ofPattern("aK時", Locale.JAPAN);

    public String getPaymentStr() {
        return paymentStr;
    }

    public void setPaymentStr(String paymentStr) {
        this.paymentStr = paymentStr;
    }

    public String getStatusStr() {
        return statusStr;
    }

    public void setStatusStr(String statusStr) {
        this.statusStr = statusStr;
    }

    public String getInsuredStr() {
        return insuredStr;
    }

    public void setInsuredStr(String insuredStr) {
        this.insuredStr = insuredStr;
    }

    public String getGenderStr() {
        return genderStr;
    }

    public void setGenderStr(String genderStr) {
        this.genderStr = genderStr;
    }

    public String getInsatsuRenban() {
        return insatsuRenban;
    }

    public void setInsatsuRenban(String insatsuRenban) {
        this.insatsuRenban = insatsuRenban;
    }

    public String getPolNo() {
        return polNo;
    }

    public void setPolNo(String polNo) {
        this.polNo = polNo;
    }

    public Integer getStatusFlg() {
        return statusFlg;
    }

    public void setStatusFlg(Integer statusFlg) {
        this.statusFlg = statusFlg;
    }

    public boolean isCancelFlg() {
        return cancelFlg;
    }

    public void setCancelFlg(boolean cancelFlg) {
        this.cancelFlg = cancelFlg;
    }

    public String getInceptionDate() {
        return inceptionDate;
    }

    public void setInceptionDate(String inceptionDate) {
        this.inceptionDate = inceptionDate;
    }

    public String getInceptionTime() {
        return inceptionTime;
    }

    public void setInceptionTime(String inceptionTime) {
        this.inceptionTime = inceptionTime;
    }

    public String getConclusionDate() {
        return conclusionDate;
    }

    public void setConclusionDate(String conclusionDate) {
        this.conclusionDate = conclusionDate;
    }

    public String getConclusionTime() {
        return conclusionTime;
    }

    public void setConclusionTime(String conclusionTime) {
        this.conclusionTime = conclusionTime;
    }

    public Integer getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(Integer paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Integer getInstallment() {
        return installment;
    }

    public void setInstallment(Integer installment) {
        this.installment = installment;
    }

    public Integer getInsuredKbn() {
        return insuredKbn;
    }

    public void setInsuredKbn(Integer insuredKbn) {
        this.insuredKbn = insuredKbn;
    }

    public String getNameKana1() {
        return nameKana1;
    }

    public void setNameKana1(String nameKana1) {
        this.nameKana1 = nameKana1;
    }

    public String getNameKana2() {
        return nameKana2;
    }

    public void setNameKana2(String nameKana2) {
        this.nameKana2 = nameKana2;
    }

    public String getNameKanji1() {
        return nameKanji1;
    }

    public void setNameKanji1(String nameKanji1) {
        this.nameKanji1 = nameKanji1;
    }

    public String getNameKanji2() {
        return nameKanji2;
    }

    public void setNameKanji2(String nameKanji2) {
        this.nameKanji2 = nameKanji2;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getAddressKana1() {
        return addressKana1;
    }

    public void setAddressKana1(String addressKana1) {
        this.addressKana1 = addressKana1;
    }

    public String getAddressKana2() {
        return addressKana2;
    }

    public void setAddressKana2(String addressKana2) {
        this.addressKana2 = addressKana2;
    }

    public String getAddressKanji1() {
        return addressKanji1;
    }

    public void setAddressKanji1(String addressKanji1) {
        this.addressKanji1 = addressKanji1;
    }

    public String getAddressKanji2() {
        return addressKanji2;
    }

    public void setAddressKanji2(String addressKanji2) {
        this.addressKanji2 = addressKanji2;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public String getTelephoneNo() {
        return telephoneNo;
    }

    public void setTelephoneNo(String telephoneNo) {
        this.telephoneNo = telephoneNo;
    }

    public String getMobilephoneNo() {
        return mobilephoneNo;
    }

    public void setMobilephoneNo(String mobilephoneNo) {
        this.mobilephoneNo = mobilephoneNo;
    }

    public String getFaxNo() {
        return faxNo;
    }

    public void setFaxNo(String faxNo) {
        this.faxNo = faxNo;
    }

    /**
     * 電話番号（表示用）
     * 0312345678 → 03-1234-5678
     */
    public String getFormattedTelephoneNo() {
        return formatFixedPhoneNumber(telephoneNo);
    }

    public String getFormattedPostcode() {
        if (postcode == null || postcode.isEmpty()) {
            return "";
        }

        return postcode.replaceFirst(
                "(\\d{3})(\\d{4})",
                "$1-$2");
    }

    /**
     * 携帯電話番号（表示用）
     * 09012345678 → 090-1234-5678
     */
    public String getFormattedMobilephoneNo() {
        if (mobilephoneNo == null || mobilephoneNo.isEmpty()) {
            return "";
        }

        return mobilephoneNo.replaceFirst(
                "(\\d{3})(\\d{4})(\\d{4})",
                "$1-$2-$3");
    }

    /**
     * FAX番号（表示用）
     * 0312345678 → 03-1234-5678
     */
    public String getFormattedFaxNo() {
        return formatFixedPhoneNumber(faxNo);
    }

    /**
     * 電話番号・FAX番号を表示用に整形する
     * 0312345678 → 03-1234-5678
     */
    private String formatFixedPhoneNumber(String number) {
        if (number == null || number.isEmpty()) {
            return "";
        }

        return number.replaceFirst(
                "(\\d{2})(\\d{4})(\\d{4})",
                "$1-$2-$3");
    }

    /**
     * 生年月日（表示用）
     * 19980219 → 平成10年2月19日
     */
    public String getFormattedBirthday() {
        return formatJapaneseDate(birthday);
    }

    /**
     * 保険期間開始日（表示用）
     * 20260901 → 令和8年9月1日
     */
    public String getFormattedInceptionDate() {
        return formatJapaneseDate(inceptionDate);
    }

    /**
     * 保険期間満期日（表示用）
     * 20260930 → 令和8年9月30日
     */
    public String getFormattedConclusionDate() {
        return formatJapaneseDate(conclusionDate);
    }

    /**
     * 保険期間開始時刻（表示用）
     * 09 → 午前9時
     * 15 → 午後3時
     */
    public String getFormattedInceptionTime() {
        return formatJapaneseTime(inceptionTime);
    }

    /**
     * 保険期間満期時刻（表示用）
     * 18 → 午後6時
     */
    public String getFormattedConclusionTime() {
        return formatJapaneseTime(conclusionTime);
    }

    /**
     * yyyyMMdd形式の日付を和暦に変換する
     */
    private String formatJapaneseDate(String dateValue) {
        if (dateValue == null || dateValue.isBlank()) {
            return "";
        }

        try {
            LocalDate localDate = LocalDate.parse(
                dateValue,
                DateTimeFormatter.BASIC_ISO_DATE);
            
            JapaneseDate japaneseDate = JapaneseDate.from(localDate);

            return japaneseDate.format(JAPANESE_DATE_FORMATTER);

        } catch (DateTimeException e) {
            // 不正な値の場合は、調査できるよう元の値を返す
            return dateValue;
        }
    }

    /**
     * HH形式の時刻を午前・午後表記に変換する
     */
    private String formatJapaneseTime(String timeValue) {
        if (timeValue == null || timeValue.isBlank()) {
            return "";
        }

        try {
            int hour = Integer.parseInt(timeValue);
            LocalTime localTime = LocalTime.of(hour, 0);

            return localTime.format(JAPANESE_TIME_FORMATTER);

        } catch (NumberFormatException | DateTimeException e) {
            return timeValue;
        }
    }
}