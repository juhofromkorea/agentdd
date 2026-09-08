package agentdd.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import agentdd.model.data.Accident;

/**
 * 事故受付テーブルDAOクラス
 * 事故受付テーブル（claim_tbl）への検索・登録などのDB操作を担当します。
 */
public class AccidentDao {

    private Connection con;

    /**
     * コンストラクタ
     * DB接続を取得してフィールドに保持します。
     * @throws SQLException 
     */
    public AccidentDao() throws SQLException {
        this.con = ConnectionManager.getConnection();
    }

    /**
     * 事故受付番号検索（指定した番号のデータをDBから取得）
     * 
     * @param accidentNo 事故受付番号
     * @return 検索結果の事故受付データ (該当データがない場合は null)
     * @throws SQLException
     */
    public Accident getAccident(String accidentNo) throws SQLException {
        Accident accident = null;

        // 契約情報テーブルと結合して漢字氏名を取得するようSQLを修正
        String sql = "SELECT cl.*, co.name_kanji1, co.name_kanji2 " +
                    "FROM claim_tbl cl " +
                    "LEFT JOIN contractinfo_tbl co ON cl.cover_id = co.insatsu_renban " +
                    "WHERE cl.claim_no = ?";

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, accidentNo);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    accident = new Accident();

                    accident.setClaimNo(rs.getString("claim_no"));
                    accident.setCoverId(rs.getInt("cover_id"));
                    accident.setAccidentFlag(rs.getInt("claim_status"));
                    
                    accident.setPaymentAmount(rs.getInt("payment_price"));
                    
                    // 氏名漢字1と漢字2を結合してセット
                    String kanji1 = rs.getString("name_kanji1");
                    String kanji2 = rs.getString("name_kanji2");
                    String contractorName = (kanji1 != null ? kanji1 : "") + (kanji2 != null ? kanji2 : "");
                    accident.setContractorName(contractorName);

                    // 【修正】occur -> accident に変更
                    accident.setAccidentPlaceKana1(rs.getString("accident_location_kana1"));
                    accident.setAccidentPlaceKana2(rs.getString("accident_location_kana2"));
                    accident.setAccidentPlaceKanji1(rs.getString("accident_location_kanji1"));
                    accident.setAccidentPlaceKanji2(rs.getString("accident_location_kanji2"));
                    accident.setAccidentDate(rs.getString("accident_date"));
                    
                    accident.setOpponentStatus(rs.getString("accident_situation"));
                    accident.setNegligenceInsured(rs.getInt("rating_blame_myself"));
                    accident.setNegligenceOpponent(rs.getInt("rating_blame_yourself"));
                    
                    accident.setDamageVehicle(rs.getInt("damage_car_price"));
                    accident.setDamagePerson(rs.getInt("damage_bodily_price"));
                    accident.setDamageObject(rs.getInt("damage_property_price"));
                    accident.setDamageInjury(rs.getInt("damage_accident_price"));
                    
                    accident.setStatusVehicle(rs.getString("damage_car_state"));
                    accident.setStatusPerson(rs.getString("damage_bodily_state"));
                    accident.setStatusObject(rs.getString("damage_property_state"));
                    accident.setStatusInjury(rs.getString("damage_accident_state"));
                }
            }
        }

        return accident;
    }

    /**
     * 新規の事故受付番号を自動採番する（C0000001から順に採番）
     * @return 新規事故受付番号
     * @throws SQLException
     */
    public String generateNextClaimNo() throws SQLException {
        String newClaimNo = "C0000001";
        String sql = "SELECT MAX(claim_no) FROM claim_tbl";
        try (PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()) {
            if (rs.next() && rs.getString(1) != null) {
                String maxNo = rs.getString(1);
                if (maxNo.startsWith("C")) {
                    try {
                        int num = Integer.parseInt(maxNo.substring(1)) + 1;
                        newClaimNo = String.format("C%07d", num);
                    } catch (NumberFormatException e) {
                        // パース失敗時のフォールバック
                    }
                }
            }
        }
        return newClaimNo;
    }

    /**
     * 事故受付情報の新規登録
     * @param accident 登録対象の事故受付データ
     * @throws SQLException
     */
    public void insertAccident(Accident accident) throws SQLException {
        String sql = "INSERT INTO claim_tbl ("
                + " claim_no, cover_id, claim_status, payment_price, "
                + " accident_location_kana1, accident_location_kana2, accident_location_kanji1, accident_location_kanji2, "
                + " accident_date, accident_situation, rating_blame_myself, rating_blame_yourself, "
                + " damage_car_price, damage_bodily_price, damage_property_price, damage_accident_price, "
                + " damage_car_state, damage_bodily_state, damage_property_state, damage_accident_state"
                + ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, accident.getClaimNo());
            stmt.setInt(2, accident.getCoverId());
            stmt.setInt(3, accident.getAccidentFlag());
            
            stmt.setLong(4, accident.getPaymentAmount());
            
            // 【修正】occur -> accident に変更
            stmt.setString(5, accident.getAccidentPlaceKana1());
            stmt.setString(6, accident.getAccidentPlaceKana2());
            stmt.setString(7, accident.getAccidentPlaceKanji1());
            stmt.setString(8, accident.getAccidentPlaceKanji2());
            stmt.setString(9, accident.getAccidentDate());
            
            stmt.setString(10, accident.getOpponentStatus());
            stmt.setInt(11, accident.getNegligenceInsured());
            stmt.setInt(12, accident.getNegligenceOpponent());
            
            stmt.setLong(13, accident.getDamageVehicle());
            stmt.setLong(14, accident.getDamagePerson());
            stmt.setLong(15, accident.getDamageObject());
            stmt.setLong(16, accident.getDamageInjury());
            
            stmt.setString(17, accident.getStatusVehicle());
            stmt.setString(18, accident.getStatusPerson());
            stmt.setString(19, accident.getStatusObject());
            stmt.setString(20, accident.getStatusInjury());

            stmt.executeUpdate();
        }
    }

    /**
     * コントローラーからのリフレクション呼び出し（setAccident）に対応するメソッド
     * @param accident 事故データ
     * @throws SQLException
     */
    public void setAccident(Accident accident) throws SQLException {
        insertAccident(accident);
    }
}