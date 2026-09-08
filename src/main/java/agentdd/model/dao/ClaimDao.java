package agentdd.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import agentdd.model.data.Claim;

public class ClaimDao {
    public void setEstimate(Claim claim) throws SQLException {
        String sql = "INSERT INTO cover_tbl (insatsu_renban, maker, car_name, license_no, vehicle_price, vehicle_rates, bodily_rates, property_damage_rates, accident_rates, license_color, age_limit, premium_amount) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, claim.getInsatsuRenban());
            pstmt.setString(2, claim.getMaker());
            pstmt.setString(3, claim.getCarName());
            pstmt.setString(4, claim.getLicenseNo());
            pstmt.setInt(5, claim.getVehiclePrice());
            pstmt.setDouble(6, claim.getVehicleRates());
            pstmt.setDouble(7, claim.getBodilyRates());
            pstmt.setDouble(8, claim.getPropertyDamageRates());
            pstmt.setDouble(9, claim.getAccidentRates());
            pstmt.setInt(10, claim.getLicenseColor());
            pstmt.setInt(11, claim.getAgeLimit());
            pstmt.setInt(12, claim.getPremiumAmount());
            pstmt.executeUpdate();
        }
    }
}
