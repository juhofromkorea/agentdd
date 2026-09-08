package agentdd.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import agentdd.model.data.Claim;

public class ClaimDao {

    private final Connection con;

    public ClaimDao() throws SQLException {
        this.con = ConnectionManager.getConnection();
    }

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

    public Claim getClaim(String polNo) throws SQLException {

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

    public int setEstimate(Claim claim) throws SQLException {

        String sql = "INSERT INTO COVER_TBL ("
                + "insatsu_renban, premium_amount, premium_installment, "
                + "maker, car_name, license_no, vehicle_price, "
                + "vehicle_rates, bodily_rates, property_damage_rates, "
                + "accident_rates, license_color, age_limit"
                + ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, claim.getInsatsuRenban());
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

    private Claim createClaim(ResultSet res) throws SQLException {
        Claim claim = new Claim();

        claim.setCoverId(res.getObject("cover_id", Integer.class));
        claim.setInsatsuRenban(res.getString("insatsu_renban"));
        claim.setPremiumAmount(res.getInt("premium_amount"));
        claim.setPremiumInstallment(res.getInt("premium_installment"));
        claim.setMaker(res.getString("maker"));
        claim.setCarName(res.getString("car_name"));
        claim.setLicenseNo(res.getString("license_no"));
        claim.setVehiclePrice(res.getInt("vehicle_price"));
        claim.setVehicleRates(res.getObject("vehicle_rates", Integer.class));
        claim.setBodilyRates(res.getObject("bodily_rates", Integer.class));
        claim.setPropertyDamageRates(res.getObject("property_damage_rates", Integer.class));
        claim.setAccidentRates(res.getObject("accident_rates", Integer.class));
        claim.setLicenseColor(res.getObject("license_color", Integer.class));
        claim.setAgeLimit(res.getObject("age_limit", Integer.class));

        return claim;
    }
}
