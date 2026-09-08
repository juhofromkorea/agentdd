package agentdd.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import agentdd.model.data.Claim;

/**
 * 補償情報テーブル（COVER_TBL）に対するSQL操作を行う。
 */
public class ClaimDao {

    private final Connection con;

    /**
     * コンストラクタ
     *
     * @param con DBコネクション（クローズ・トランザクション管理は呼び出し元で行う）
     */
    public ClaimDao(Connection con) {
        this.con = con;
    }

    /**
     * 印刷連番に関連する補償情報を取得する。＜計上＞
     *
     * @param insatsuRenban 印刷連番
     * @return 補償情報（該当するデータがない場合はnull）
     * @throws SQLException DBエラーが発生した場合
     */
    public Claim getClaimForAccount(String insatsuRenban) throws SQLException {
        String sql = "SELECT * FROM COVER_TBL WHERE insatsu_renban = ?";

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, insatsuRenban);

            try (ResultSet res = stmt.executeQuery()) {
                if (res.next()) {
                    return createClaim(res);
                }
            }
        }
        return null;
    }

    /**
     * 証券番号に関連する補償情報を取得する。＜照会・解約・事故受付＞
     *
     * @param polNo 証券番号
     * @return 補償情報（該当するデータがない場合はnull）
     * @throws SQLException DBエラーが発生した場合
     */
    public Claim getClaim(String polNo) throws SQLException {
        // COVER_TBLには証券番号がないため、印刷連番で契約情報テーブルと結合する。
        String sql = "SELECT COVER_TBL.* "
                + "FROM COVER_TBL "
                + "INNER JOIN CONTRACTINFO_TBL "
                + "ON COVER_TBL.insatsu_renban = CONTRACTINFO_TBL.insatsu_renban "
                + "WHERE CONTRACTINFO_TBL.pol_no = ?";

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, polNo);

            try (ResultSet res = stmt.executeQuery()) {
                if (res.next()) {
                    return createClaim(res);
                }
            }
        }
        return null;
    }

    /**
     * 新規試算の補償情報を登録する。
     * 同じ印刷連番の契約情報を先に登録してから呼び出す。
     *
     * @param claim 補償情報
     * @return 登録件数
     * @throws SQLException DBエラーが発生した場合
     */
    public int setEstimate(Claim claim) throws SQLException {
        // cover_idはAUTO_INCREMENTのため、INSERTの対象に含めない。
        String sql = "INSERT INTO COVER_TBL ("
                + "insatsu_renban, premium_amount, premium_installment, "
                + "maker, car_name, license_no, vehicle_price, "
                + "vehicle_rates, bodily_rates, property_damage_rates, "
                + "accident_rates, license_color, age_limit"
                + ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, claim.getInsatsuRenban());
            // setObjectを使用し、未設定の金額・区分はSQLのNULLとして登録する。
            stmt.setObject(2, claim.getPremiumAmount(), Types.BIGINT);
            stmt.setObject(3, claim.getPremiumInstallment(), Types.BIGINT);
            stmt.setString(4, claim.getMaker());
            stmt.setString(5, claim.getCarName());
            stmt.setString(6, claim.getLicenseNo());
            stmt.setObject(7, claim.getVehiclePrice(), Types.BIGINT);
            stmt.setObject(8, claim.getVehicleRates(), Types.INTEGER);
            stmt.setObject(9, claim.getBodilyRates(), Types.INTEGER);
            stmt.setObject(10, claim.getPropertyDamageRates(), Types.INTEGER);
            stmt.setObject(11, claim.getAccidentRates(), Types.INTEGER);
            stmt.setObject(12, claim.getLicenseColor(), Types.INTEGER);
            stmt.setObject(13, claim.getAgeLimit(), Types.INTEGER);

            return stmt.executeUpdate();
        }
    }

    /**
     * 検索結果1件を補償情報に設定する。
     *
     * @param res 現在行に補償情報がある検索結果
     * @return 補償情報
     * @throws SQLException 検索結果の取得に失敗した場合
     */
    private Claim createClaim(ResultSet res) throws SQLException {
        Claim claim = new Claim();

        claim.setCoverId(res.getObject("cover_id", Integer.class));
        claim.setInsatsuRenban(res.getString("insatsu_renban"));
        claim.setPremiumAmount(res.getObject("premium_amount", Long.class));
        claim.setPremiumInstallment(res.getObject("premium_installment", Long.class));
        claim.setMaker(res.getString("maker"));
        claim.setCarName(res.getString("car_name"));
        claim.setLicenseNo(res.getString("license_no"));
        claim.setVehiclePrice(res.getObject("vehicle_price", Long.class));
        claim.setVehicleRates(res.getObject("vehicle_rates", Integer.class));
        claim.setBodilyRates(res.getObject("bodily_rates", Integer.class));
        claim.setPropertyDamageRates(res.getObject("property_damage_rates", Integer.class));
        claim.setAccidentRates(res.getObject("accident_rates", Integer.class));
        claim.setLicenseColor(res.getObject("license_color", Integer.class));
        claim.setAgeLimit(res.getObject("age_limit", Integer.class));

        return claim;
    }
}
