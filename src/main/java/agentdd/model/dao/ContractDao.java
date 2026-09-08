package agentdd.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import agentdd.model.data.Contract;

public class ContractDao {

    public String getEstimate() throws SQLException, ClassNotFoundException {
        String sql = "SELECT insatsu_renban FROM contractinfo_tbl WHERE insatsu_renban IS NOT NULL ORDER BY insatsu_renban DESC LIMIT 1";

        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                String latest = rs.getString("insatsu_renban");
                if (latest != null && !latest.trim().isEmpty());    
                    return latest;         
                }
        }
        return "A0000000";
    }

    public void setEstimate(Contract contract) throws SQLException {
        
        String sql = """
                
                INSERT INTO contractinfo_tbl (
                    insatsu_renban, pol_no, status_flg, cancel_flg,
                    inception_date, inception_time, conclusion_date, conclusion_time,
                    payment_method, installment, insured_kbn,
                    name_kana1, name_kana2, name_kanji1, name_kanji2,
                    postcode, address_kana1, address_kana2, address_kanji1, address_kanji2,
                    birthday, gender, telephone_no, mobilephone_no, fax_no
                ) VALUES (
                    ?, ?, ?, ?, ?,
                    ?, ?, ?, ?, ?,
                    ?, ?, ?, ?, ?,
                    ?, ?, ?, ?, ?,
                    ?, ?, ?, ?, ?
                )
                """;;

        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, contract.getInsatsuRenban());
            pstmt.setString(2, contract.getPolNo());
            pstmt.setObject(3, contract.getStatusFlg());
            pstmt.setBoolean(4, contract.isCancelFlg());
            pstmt.setString(5, contract.getInceptionDate());
            pstmt.setString(6, contract.getInceptionTime());
            pstmt.setString(7, contract.getConclusionDate());
            pstmt.setString(8, contract.getConclusionTime());
            pstmt.setObject(9, contract.getPaymentMethod());
            pstmt.setObject(10, contract.getInstallment());
            pstmt.setObject(11, contract.getInsuredKbn());
            pstmt.setString(12, contract.getNameKana1());
            pstmt.setString(13, contract.getNameKana2());
            pstmt.setString(14, contract.getNameKanji1());
            pstmt.setString(15, contract.getNameKanji2());
            pstmt.setString(16, contract.getPostcode());
            pstmt.setString(17, contract.getAddressKana1());
            pstmt.setString(18, contract.getAddressKana2());
            pstmt.setString(19, contract.getAddressKanji1());
            pstmt.setString(20, contract.getAddressKanji2());
            pstmt.setString(21, contract.getBirthday());
            pstmt.setObject(22, contract.getGender());
            pstmt.setString(23, contract.getTelephoneNo());
            pstmt.setString(24, contract.getMobilephoneNo());
            pstmt.setString(25, contract.getFaxNo());
            pstmt.executeUpdate();
        }
    }
}
