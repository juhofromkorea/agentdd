package agentdd.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import agentdd.model.data.Claim;

public class VehicleDao {

    public List<Map<String, String>> findAll() throws SQLException {
        String sql = "SELECT maker, name FROM M_CARS_TBL ORDER BY maker, name";
        List<Map<String, String>> vehicles = new ArrayList<>();

        try (Connection con = ConnectionManager.getConnection();
                PreparedStatement pstmt = con.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Map<String, String> vehicle = new HashMap<>();
                vehicle.put("maker", rs.getString("maker"));
                vehicle.put("name", rs.getString("name"));
                vehicles.add(vehicle);
            }
            return vehicles;
        } catch (SQLException e) {
            throw new SQLException("車両マスタ一覧の取得に失敗しました。", e);
        }
    }
    
    public Claim getVehicle(Claim claim) throws SQLException {
        
        String sql = "SELECT * FROM M_CARS_TBL WHERE maker = ? AND name = ?";

        try (Connection con = ConnectionManager.getConnection();
            PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setString(1, claim.getMaker());
            pstmt.setString(2, claim.getCarName());

                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                claim.setVehiclePrice(rs.getInt("vehicle_price") * 10);
                claim.setVehicleRates(rs.getInt("vehicle_rates"));
                claim.setBodilyRates(rs.getInt("bodily_rates"));
                claim.setPropertyDamageRates(rs.getInt("property_damage_rates"));
                claim.setAccidentRates(rs.getInt("accident_rates"));
            }
            return claim;
        } catch (SQLException e) {
            throw new SQLException("車両情報の取得に失敗しました。", e);
        }
    }
    
}
