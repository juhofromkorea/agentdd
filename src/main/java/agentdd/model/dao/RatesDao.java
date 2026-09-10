package agentdd.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class RatesDao {

    private final Connection con;

    public RatesDao(Connection con) {
        this.con = java.util.Objects.requireNonNull(con);
    }

    public Double getRate(int id) throws SQLException {
        Double rate = null; // 未登録を1.0で隠さず、呼出側で業務エラーとして扱う
        // SQL: IDをキーにして料率マスタから倍率を取得
        String sql = "SELECT RATES FROM M_RATES_TBL WHERE ID = ?";

        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            
            pstmt.setInt(1, id); // ? に年齢条件や免許証のIDをセット

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    // DBから実際の倍率（1.9など）を取り出す
                    rate = rs.getDouble("rates");
                    if (rs.wasNull()) rate = null;
                }
            }
        }
        return rate;
    }

    
}
