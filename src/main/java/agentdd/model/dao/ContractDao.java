package agentdd.model.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import agentdd.model.data.Contract;

public class ContractDao {

    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/your_database?useSSL=false&serverTimezone=UTC";
    private static final String DB_USER = "your_user";
    private static final String DB_PASSWORD = "your_password";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD);
    }

    /**
     * 証券番号をもとに契約情報を取得する
     */
    public Contract findContractByPolNo(String polNo) throws SQLException {
        Contract contract = null;
        String sql = "SELECT * FROM CONTRACTINFO_TBL WHERE pol_no = ?";

        try (Connection conn = getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, polNo);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    contract = new Contract();
                    contract.setInsatsuRenban(rs.getString("insatsu_renban"));
                    contract.setPolNo(rs.getString("pol_no"));
                    contract.setStatusFlg((Integer) rs.getObject("status_flg"));
                    contract.setCancelFlg(rs.getBoolean("cancel_flg"));
                    contract.setNameKanji1(rs.getString("name_kanji1"));
                    contract.setNameKanji2(rs.getString("name_kanji2"));
                    contract.setPostcode(rs.getString("postcode"));
                    contract.setAddressKanji1(rs.getString("address_kanji1"));
                    contract.setAddressKanji2(rs.getString("address_kanji2"));
                    contract.setTelephoneNo(rs.getString("telephone_no"));
                }
            }
        }
        return contract;
    }
}