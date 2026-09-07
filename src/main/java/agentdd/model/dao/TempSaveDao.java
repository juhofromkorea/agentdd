package agentdd.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import agentdd.model.data.Claim;
import agentdd.model.data.Contract;
import agentdd.model.data.TempSave;

public class TempSaveDao {
    
    private Connection con;

    public TempSaveDao(Connection con){
        this.con = con;
    }

        //一時保存件数取得
    public int countByUserId(String userId)
        throws SQLException{
        
        String sql = "SELECT COUNT(*) "
        + "FROM 途中保存TBL "
        + "WHERE user_id = ?";

        try(PreparedStatement pstmt = con.prepareStatement(sql)){

            pstmt.setString(1, userId);

            try(ResultSet rs = pstmt.executeQuery()){

                if(rs.next()){
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }

        //一時保存削除
    public int delete(String tempSaveId, String userId)
        throws SQLException{

        String sql = 
        "DELETE FROM 途中保存TBL "
        + "WHERE save_no = ? "
        + "AND user_id = ?";
        
        try(PreparedStatement pstmt = con.prepareStatement(sql)){

            pstmt.setString(1, tempSaveId);
            pstmt.setString(2, userId);

            return pstmt.executeUpdate();
        }
    }

    //一時保存一覧取得
    public List<TempSave> selectAll(String userId)throws SQLException{
        List<TempSave> tempSaveList = new ArrayList<>();

        String sql = "SELECT * "
        + "FROM 途中保存TBL "
        + "WHERE user_id = ? "
        + "ORDER BY created_at DESC";

        try(PreparedStatement pstmt = con.prepareStatement(sql)){

            pstmt.setString(1, userId);

            try(ResultSet rs = pstmt.executeQuery()){

                while(rs.next()){

                    TempSave tempSave = new TempSave();
                    Contract contract = new Contract();
                    tempSave.setTempSaveId(rs.getString("save_no"));
                    tempSave.setUserId(rs.getString("user_id"));
                    tempSave.setCreatedAt(rs.getObject("created_at", LocalDateTime.class));

                    //氏名、郵便番号、住所、連絡先追加
                    contract.setNameKanji1(rs.getString("NameKanji1"));
                    contract.setNameKanji2(rs.getString("NameKanji2"));
                    contract.setPostcode(rs.getString("Postcode"));
                    contract.setAddressKanji1(rs.getString("AddressKanji1"));
                    contract.setAddressKanji2(rs.getString("AddressKanji2"));
                    contract.setTelephoneNo(rs.getString("TelePhoneNo"));

                    tempSaveList.add(tempSave);
            }
        }
    }
    return tempSaveList;
    } 

        //一時保存再開処理
    public TempSave select(String tempSaveId, String userId)
        throws SQLException {

    String sql =
            "SELECT * "
          + "FROM 途中保存TBL "
          + "WHERE save_no = ? "
          + "AND user_id = ?";

    try (PreparedStatement pstmt = con.prepareStatement(sql)) {

        pstmt.setString(1, tempSaveId);
        pstmt.setString(2, userId);

        try (ResultSet rs = pstmt.executeQuery()) {

            if (rs.next()) {

                TempSave tempSave = new TempSave();
                Contract contract = new Contract();

                tempSave.setTempSaveId(rs.getString("save_no"));
                tempSave.setUserId(rs.getString("user_id"));
                tempSave.setCreatedAt(rs.getObject("created_at",LocalDateTime.class));

                contract.setNameKanji1(rs.getString("NameKanji1"));
                contract.setNameKanji2(rs.getString("NameKanji2"));


                /*
                 * 再開時に必要な契約情報・補償情報を
                 * 全部ここでセットする。
                 */

                return tempSave;
            }
        }
    }

    return null;
}

    //一時保存処理
public int insert(TempSave tempSave) throws SQLException {

     String sql =
            "INSERT INTO 途中保存TBL ("
          + "user_id, save_no, created_at, "
          + "insatsu_renban, pol_no, status_flg, cancel_flg, "
          + "inception_date, inception_time, conclusion_date, conclusion_time, "
          + "payment_method, installment, insured_kbn, "
          + "name_kana1, name_kana2, name_kanji1, name_kanji2, "
          + "postcode, address_kana1, address_kana2, "
          + "address_kanji1, address_kanji2, birthday, gender, "
          + "telephone_no, mobilephone_no, fax_no, "
          + "cover_id, premium_amount, premium_installment, "
          + "maker, car_name, license_no, vehicle_price, "
          + "vehicle_rates, bodily_rates, property_damage_rates, "
          + "accident_rates, license_color, age_limit"
          + ") VALUES ("
          + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
          + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
          + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
          + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?"
          + ")";
    try (PreparedStatement pstmt = con.prepareStatement(sql)) {

        Contract contract = tempSave.getContract();
        Claim claim = tempSave.getClaim();

        int i = 1;

        // TempSave
        pstmt.setString(i++, tempSave.getUserId());
        pstmt.setString(i++, tempSave.getTempSaveId());
        pstmt.setObject(i++, tempSave.getCreatedAt());

        // Contract
        pstmt.setString(i++, contract.getInsatsuRenban());
        pstmt.setString(i++, contract.getPolNo());
        pstmt.setObject(i++, contract.getStatusFlg());
        pstmt.setBoolean(i++, contract.isCancelFlg());

        pstmt.setString(i++, contract.getInceptionDate());
        pstmt.setString(i++, contract.getInceptionTime());
        pstmt.setString(i++, contract.getConclusionDate());
        pstmt.setString(i++, contract.getConclusionTime());

        pstmt.setObject(i++, contract.getPaymentMethod());
        pstmt.setObject(i++, contract.getInstallment());
        pstmt.setObject(i++, contract.getInsuredKbn());

        pstmt.setString(i++, contract.getNameKana1());
        pstmt.setString(i++, contract.getNameKana2());
        pstmt.setString(i++, contract.getNameKanji1());
        pstmt.setString(i++, contract.getNameKanji2());

        pstmt.setString(i++, contract.getPostcode());
        pstmt.setString(i++, contract.getAddressKana1());
        pstmt.setString(i++, contract.getAddressKana2());
        pstmt.setString(i++, contract.getAddressKanji1());
        pstmt.setString(i++, contract.getAddressKanji2());

        pstmt.setString(i++, contract.getBirthday());
        pstmt.setObject(i++, contract.getGender());

        pstmt.setString(i++, contract.getTelephoneNo());
        pstmt.setString(i++, contract.getMobilephoneNo());
        pstmt.setString(i++, contract.getFaxNo());

        // Claim
        pstmt.setObject(i++, claim.getCoverId());
        pstmt.setObject(i++, claim.getPremiumAmount());
        pstmt.setObject(i++, claim.getPremiumInstallment());

        pstmt.setString(i++, claim.getMaker());
        pstmt.setString(i++, claim.getCarName());
        pstmt.setString(i++, claim.getLicenseNo());

        pstmt.setObject(i++, claim.getVehiclePrice());
        pstmt.setObject(i++, claim.getVehicleRates());
        pstmt.setObject(i++, claim.getBodilyRates());
        pstmt.setObject(i++, claim.getPropertyDamageRates());
        pstmt.setObject(i++, claim.getAccidentRates());

        pstmt.setObject(i++, claim.getLicenseColor());
        pstmt.setObject(i++, claim.getAgeLimit());

        return pstmt.executeUpdate();
    }
}

Integer 型について setInt() ではなく、}
