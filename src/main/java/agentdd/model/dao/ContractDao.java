package agentdd.model.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import agentdd.model.constant.SystemConst;
import agentdd.model.data.Contract;

public class ContractDao {

    static {
        try {
            Class.forName(SystemConst.JDBC_DRIVER_NAME);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
            SystemConst.JDBC_URL, 
            SystemConst.JDBC_USER, 
            SystemConst.JDBC_PASSWORD
        );
    }

    /**
     * 証券番号をもとに契約情報を取得する
     */
    public Contract findContractByPolNo(String polNo) throws SQLException {
        Contract contract = null;
        String sql = "SELECT * FROM contractinfo_tbl WHERE pol_no = ?";

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

    /**
     * 印刷連番をもとに契約情報を取得する
     */
    public Contract findContractByInsatsuRenban(String insatsuRenban) throws SQLException {
        Contract contract = null;
        String sql = "SELECT * FROM contractinfo_tbl WHERE insatsu_renban = ?";

        try (Connection conn = getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, insatsuRenban);

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