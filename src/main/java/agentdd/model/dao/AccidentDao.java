package agentdd.model.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import agentdd.model.constant.SystemConst;
import agentdd.model.data.Accident;

/**
 * 事故受付テーブルDAOクラス
 * 事故受付テーブル（claim_tbl）への検索・登録・更新などのDB操作を担当します。
 */
public class AccidentDao {

    private Connection con;

    /**
     * コンストラクタ
     * SystemConstからDB接続情報を取得して接続を保持します。
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public AccidentDao() throws SQLException, ClassNotFoundException {
        Class.forName(SystemConst.JDBC_DRIVER_NAME);
        this.con = DriverManager.getConnection(
            SystemConst.JDBC_URL,
            SystemConst.JDBC_USER,
            SystemConst.JDBC_PASSWORD
        );
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

        String sql = "SELECT cl.*, co.pol_no, co.name_kanji1, co.name_kanji2 " +
                "FROM claim_tbl cl " +
                "LEFT JOIN cover_tbl cv ON cl.cover_id = cv.cover_id " +
                "LEFT JOIN contractinfo_tbl co ON cv.insatsu_renban = co.insatsu_renban " +
                    "WHERE cl.claim_no = ?";

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, accidentNo);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    accident = new Accident();

                    accident.setClaimNo(rs.getString("claim_no"));
                    accident.setPolNo(rs.getString("pol_no"));
                    accident.setCoverId(rs.getInt("cover_id"));
                    accident.setClaimStatus(rs.getInt("claim_status"));
                    accident.setPaymentPrice(rs.getLong("payment_price"));
                    
                    accident.setAccidentLocationKana1(rs.getString("accident_location_kana1"));
                    accident.setAccidentLocationKana2(rs.getString("accident_location_kana2"));
                    accident.setAccidentLocationKanji1(rs.getString("accident_location_kanji1"));
                    accident.setAccidentLocationKanji2(rs.getString("accident_location_kanji2"));
                    accident.setAccidentDate(rs.getString("accident_date"));
                    
                    accident.setAccidentSituation(rs.getString("accident_situation"));
                    accident.setRatingBlameMyself(rs.getInt("rating_blame_myself"));
                    accident.setRatingBlameYourself(rs.getInt("rating_blame_yourself"));
                    
                    accident.setDamageCarPrice(rs.getLong("damage_car_price"));
                    accident.setDamageBodilyPrice(rs.getLong("damage_bodily_price"));
                    accident.setDamagePropertyPrice(rs.getLong("damage_property_price"));
                    accident.setDamageAccidentPrice(rs.getLong("damage_accident_price"));
                    
                    accident.setDamageCarState(rs.getString("damage_car_state"));
                    accident.setDamageBodilyState(rs.getString("damage_bodily_state"));
                    accident.setDamagePropertyState(rs.getString("damage_property_state"));
                    accident.setDamageAccidentState(rs.getString("damage_accident_state"));
                }
            }
        }

        return accident;
    }


    /**
     * 証券番号を指定して、既に登録されている事故受付データを取得します。
     * 
     * @param polNo 証券番号
     * @return 検索結果の事故受付データ (該当データがない場合は null)
     * @throws SQLException
     */
    public Accident getAccidentByPolNo(String polNo) throws SQLException {
        Accident accident = null;
        String sql = "SELECT cl.*, co.pol_no FROM claim_tbl cl " +
                "JOIN cover_tbl cv ON cl.cover_id = cv.cover_id " +
                "JOIN contractinfo_tbl co ON cv.insatsu_renban = co.insatsu_renban " +
                    "WHERE co.pol_no = ?";

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, polNo);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    accident = new Accident();
                    accident.setClaimNo(rs.getString("claim_no"));
                    accident.setPolNo(rs.getString("pol_no"));
                    accident.setCoverId(rs.getInt("cover_id"));
                    accident.setClaimStatus(rs.getInt("claim_status"));
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
     * 事故受付情報の新規登録（INSERT）を実行します。
     * @param accident 登録対象の事故受付データ
     * @throws SQLException
     */
    public void insertAccident(Accident accident) throws SQLException {
        String insertSql = "INSERT INTO claim_tbl ("
                + " claim_no, cover_id, claim_status, payment_price, "
                + " accident_location_kana1, accident_location_kana2, accident_location_kanji1, accident_location_kanji2, "
                + " accident_date, accident_situation, rating_blame_myself, rating_blame_yourself, "
                + " damage_car_price, damage_bodily_price, damage_property_price, damage_accident_price, "
                + " damage_car_state, damage_bodily_state, damage_property_state, damage_accident_state"
                + ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = con.prepareStatement(insertSql)) {
            stmt.setString(1, accident.getClaimNo());
            stmt.setInt(2, accident.getCoverId());
            stmt.setInt(3, accident.getClaimStatus());
            stmt.setLong(4, accident.getPaymentPrice());
            
            stmt.setString(5, accident.getAccidentLocationKana1());
            stmt.setString(6, accident.getAccidentLocationKana2());
            stmt.setString(7, accident.getAccidentLocationKanji1());
            stmt.setString(8, accident.getAccidentLocationKanji2());
            stmt.setString(9, accident.getAccidentDate());
            
            stmt.setString(10, accident.getAccidentSituation());
            stmt.setInt(11, accident.getRatingBlameMyself());
            stmt.setInt(12, accident.getRatingBlameYourself());
            
            stmt.setLong(13, accident.getDamageCarPrice());
            stmt.setLong(14, accident.getDamageBodilyPrice());
            stmt.setLong(15, accident.getDamagePropertyPrice());
            stmt.setLong(16, accident.getDamageAccidentPrice());
            
            stmt.setString(17, accident.getDamageCarState());
            stmt.setString(18, accident.getDamageBodilyState());
            stmt.setString(19, accident.getDamagePropertyState());
            stmt.setString(20, accident.getDamageAccidentState());

            stmt.executeUpdate();
        }
    }

    /**
     * 事故受付情報の更新（UPDATE）を実行します。
     * @param accident 更新対象の事故受付データ
     * @throws SQLException
     */
    public void updateAccident(Accident accident) throws SQLException {
        String updateSql = "UPDATE claim_tbl SET " +
                "cover_id = ?, " +
                "claim_status = ?, " +
                "payment_price = ?, " +
                "accident_location_kana1 = ?, " +
                "accident_location_kana2 = ?, " +
                "accident_location_kanji1 = ?, " +
                "accident_location_kanji2 = ?, " +
                "accident_date = ?, " +
                "accident_situation = ?, " +
                "rating_blame_myself = ?, " +
                "rating_blame_yourself = ?, " +
                "damage_car_price = ?, " +
                "damage_bodily_price = ?, " +
                "damage_property_price = ?, " +
                "damage_accident_price = ?, " +
                "damage_car_state = ?, " +
                "damage_bodily_state = ?, " +
                "damage_property_state = ?, " +
                "damage_accident_state = ? " +
                "WHERE claim_no = ?";

        try (PreparedStatement stmt = con.prepareStatement(updateSql)) {
            stmt.setInt(1, accident.getCoverId());
            stmt.setInt(2, accident.getClaimStatus());
            stmt.setLong(3, accident.getPaymentPrice());
            
            stmt.setString(4, accident.getAccidentLocationKana1());
            stmt.setString(5, accident.getAccidentLocationKana2());
            stmt.setString(6, accident.getAccidentLocationKanji1());
            stmt.setString(7, accident.getAccidentLocationKanji2());
            stmt.setString(8, accident.getAccidentDate());
            
            stmt.setString(9, accident.getAccidentSituation());
            stmt.setInt(10, accident.getRatingBlameMyself());
            stmt.setInt(11, accident.getRatingBlameYourself());
            
            stmt.setLong(12, accident.getDamageCarPrice());
            stmt.setLong(13, accident.getDamageBodilyPrice());
            stmt.setLong(14, accident.getDamagePropertyPrice());
            stmt.setLong(15, accident.getDamageAccidentPrice());
            
            stmt.setString(16, accident.getDamageCarState());
            stmt.setString(17, accident.getDamageBodilyState());
            stmt.setString(18, accident.getDamagePropertyState());
            stmt.setString(19, accident.getDamageAccidentState());
            
            // WHERE句の条件
            stmt.setString(20, accident.getClaimNo());

            stmt.executeUpdate();
        }
    }
}