package agentdd.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import agentdd.model.data.Accident;

/**
 * 事故受付テーブルDAOクラス
 * 事故受付テーブル（accident）への検索・登録などのDB操作を担当します。
 */
public class AccidentDao {

    private Connection con;

    /**
     * コンストラクタ
     * DB接続を取得してフィールドに保持します。
     * @throws SQLException 
     * @throws ClassNotFoundException 
     */
    public AccidentDao() throws SQLException, ClassNotFoundException {
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

        String sql = "SELECT * FROM accident WHERE accident_no = ?";

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, accidentNo);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    accident = new Accident();

                    accident.setAccidentNo(rs.getString("accident_no"));
                    accident.setClaim(rs.getInt("claim"));
                    accident.setAccidentFlag(rs.getInt("accident_flag"));
                    accident.setPaymentAmount(rs.getInt("payment_amount"));
                    accident.setOccurPlaceKana1(rs.getString("occur_place_kana1"));
                    accident.setOccurPlaceKana2(rs.getString("occur_place_kana2"));
                    accident.setOccurPlaceKanji1(rs.getString("occur_place_kanji1"));
                    accident.setOccurPlaceKanji2(rs.getString("occur_place_kanji2"));
                    accident.setOccurDate(rs.getString("occur_date"));
                    accident.setOpponentStatus(rs.getString("opponent_status"));
                    accident.setNegligenceInsured(rs.getInt("negligence_insured"));
                    accident.setNegligenceOpponent(rs.getInt("negligence_opponent"));
                    accident.setDamageVehicle(rs.getInt("damage_vehicle"));
                    accident.setDamagePerson(rs.getInt("damage_person"));
                    accident.setDamageObject(rs.getInt("damage_object"));
                    accident.setDamageInjury(rs.getInt("damage_injury"));
                    accident.setStatusVehicle(rs.getString("status_vehicle"));
                    accident.setStatusPerson(rs.getString("status_person"));
                    accident.setStatusObject(rs.getString("status_object"));
                    accident.setStatusInjury(rs.getString("status_injury"));
                }
            }
        }

        return accident;
    }

    /**
     * 新規の事故受付番号を自動採番する
     * @return 新規事故受付番号
     * @throws SQLException
     */
    public String generateNextClaimNo() throws SQLException {
        String newClaimNo = "AC00001";
        String sql = "SELECT MAX(accident_no) FROM accident";
        try (PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next() && rs.getString(1) != null) {
                String maxNo = rs.getString(1);
                if (maxNo.startsWith("AC")) {
                    try {
                        int num = Integer.parseInt(maxNo.substring(2)) + 1;
                        newClaimNo = String.format("AC%05d", num);
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
        String sql = "INSERT INTO accident ("
                + " accident_no, claim, accident_flag, payment_amount, "
                + " occur_place_kana1, occur_place_kana2, occur_place_kanji1, occur_place_kanji2, "
                + " occur_date, opponent_status, negligence_insured, negligence_opponent, "
                + " damage_vehicle, damage_person, damage_object, damage_injury, "
                + " status_vehicle, status_person, status_object, status_injury"
                + ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, accident.getAccidentNo());
            stmt.setInt(2, accident.getClaim());
            stmt.setInt(3, accident.getAccidentFlag());
            stmt.setInt(4, accident.getPaymentAmount());
            stmt.setString(5, accident.getOccurPlaceKana1());
            stmt.setString(6, accident.getOccurPlaceKana2());
            stmt.setString(7, accident.getOccurPlaceKanji1());
            stmt.setString(8, accident.getOccurPlaceKanji2());
            stmt.setString(9, accident.getOccurDate());
            stmt.setString(10, accident.getOpponentStatus());
            stmt.setInt(11, accident.getNegligenceInsured());
            stmt.setInt(12, accident.getNegligenceOpponent());
            stmt.setInt(13, accident.getDamageVehicle());
            stmt.setInt(14, accident.getDamagePerson());
            stmt.setInt(15, accident.getDamageObject());
            stmt.setInt(16, accident.getDamageInjury());
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