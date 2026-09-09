package agentdd.model.data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

// 一時保存情報を保持するデータクラス
public class TempSave {

    private static final DateTimeFormatter DISPLAY_FORMAT =
            DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
    
    // 一時保存データを識別するID
    private String tempSaveId;

    // 一時保存を行ったユーザーのID
    private String userId;

    // 一時保存した日時
    private LocalDateTime createdAt;

    // 契約情報
    private Contract contract;

    // 補償情報
    private Claim claim;

    
    // 一時保存IDを取得
    public String getTempSaveId() {
        return tempSaveId;
    }

    // 一時保存IDを設定
    public void setTempSaveId(String tempSaveId) {
        this.tempSaveId = tempSaveId;
    }

    // ユーザーIDを取得
    public String getUserId() {
        return userId;
    }

    // ユーザーIDを設定
    public void setUserId(String userId) {
        this.userId = userId;
    }

    // 一時保存日時を取得
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    // 一時保存日時を設定
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    /** JSPの一覧表示用。JSTLのfmt:formatDateはLocalDateTimeを扱えないため、Bean側で文字列化します。 */
    public String getCreatedAtText() {
        return createdAt == null ? "" : createdAt.format(DISPLAY_FORMAT);
    }

    // 契約情報を取得
    public Contract getContract() {
        return contract;
    }

    // 契約情報を設定
    public void setContract(Contract contract) {
        this.contract = contract;
    }

    // 補償情報を取得
    public Claim getClaim() {
        return claim;
    }

    // 補償情報を設定
    public void setClaim(Claim claim) {
        this.claim = claim;
    }
}