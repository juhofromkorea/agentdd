package agentdd.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ContractDao {
    private Connection con;

    /**
     * コンストラクタ
     */public ContractDao(Connection con){
        this.con = con;
    }

    /*契約情報一覧＜計上＞ */
    public Contract getContractForAccount(String insatsuRenban) throws SQLException{

        ResultSet res = null ;//SQLの結果を入れる
        PreparedStatement stmt = null;

        Contract cnt = null;
        String sql = "select * from contract where insatsurenban = ?";
        try{
            stmt = con.prepareStatement(sql);
            stmt.setString(1,insatsuRenban);
            res = stmt.executeQuery();

            if (res.next()){
                cnt = new Contract();

                cnt.setInsatsuRenban(res.getString("insatsuRenban"));
                cnt.setPolNo(res.getString("polNo"));
                cnt.setStatusFlg(res.getInt("statusFlg"));
                cnt.setCancelFlg(res.getBoolean("cancelFlg"));
                cnt.setInceptionDate(res.getString("inceptionDate"));
                cnt.setInceptionTime(res.getString("inceptionTime"));
                cnt.setConclusionDate(res.getString("conclusionDate"));
                cnt.setConclusionTime(res.getString("conclusionTime"));
                cnt.setPaymentMethod(res.getInt("paymentMethod"));
                cnt.setInstallment(res.getInt("installment"));
                cnt.setInsuredKbn(res.getInt("insuredKbn"));
                cnt.setNameKana1(res.getString("nameKana1"));
                cnt.setNameKana2(res.getString("nameKana2"));
                cnt.setNameKanji1(res.getString("nameKanji1"));
                cnt.setNameKanji2(res.getString("nameKanji2"));
                cnt.setPostcode(res.getString("postcode"));
                cnt.setAddressKana1(res.getString("addressKana1"));
                cnt.setAddressKana2(res.getString("addressKana2"));
                cnt.setAddressKanji1(res.getString("addressKanji1"));
                cnt.setAddressKana2(res.getString("addressKanji2"));
                cnt.setBirthday(res.getString("birthday"));
                cnt.setGender(cnt.genderMaster(res.getInt("gender")));
                cnt.seTelephoneNo(res.getString("telephoneNo"));
                cnt.setMobilephoneNo(res.getString("mobilephoneNo"));
                cnt.setFaxNo(res.getString("faxNo"));

            }
        }finally{
            if(res!=null){
                res.close();
            }
            if(stmt !=null){
                stmt.close();
            }
        }
        return cnt;
    }


    /*契約情報一覧＜照会＞ */
    public Contract getContractForInquiry(String polNo) throws SQLException{

        ResultSet res = null ;//SQLの結果を入れる
        PreparedStatement stmt = null;

        Contract cnt = null;
        String sql = "select * from contract where polNo = ?";
        try{
            stmt = con.prepareStatement(sql);
            stmt.setString(1,polNo);
            res = stmt.executeQuery();

            if (res.next()){
                cnt = new Contract();

                cnt.setInsatsuRenban(res.getString("insatsuRenban"));
                cnt.setPolNo(res.getString("polNo"));
                cnt.setStatusFlg(res.getInt("statusFlg"));
                cnt.setCancelFlg(res.getBoolean("cancelFlg"));
                cnt.setInceptionDate(res.getString("inceptionDate"));
                cnt.setInceptionTime(res.getString("inceptionTime"));
                cnt.setConclusionDate(res.getString("conclusionDate"));
                cnt.setConclusionTime(res.getString("conclusionTime"));
                cnt.setPaymentMethod(res.getInt("paymentMethod"));
                cnt.setInstallment(res.getInt("installment"));
                cnt.setInsuredKbn(res.getInt("insuredKbn"));
                cnt.setNameKana1(res.getString("nameKana1"));
                cnt.setNameKana2(res.getString("nameKana2"));
                cnt.setNameKanji1(res.getString("nameKanji1"));
                cnt.setNameKanji2(res.getString("nameKanji2"));
                cnt.setPostcode(res.getString("postcode"));
                cnt.setAddressKana1(res.getString("addressKana1"));
                cnt.setAddressKana2(res.getString("addressKana2"));
                cnt.setAddressKanji1(res.getString("addressKanji1"));
                cnt.setAddressKana2(res.getString("addressKanji2"));
                cnt.setBirthday(res.getString("birthday"));
                cnt.setGender(cnt.genderMaster(res.getInt("gender")));
                cnt.seTelephoneNo(res.getString("telephoneNo"));
                cnt.setMobilephoneNo(res.getString("mobilephoneNo"));
                cnt.setFaxNo(res.getString("faxNo"));

            }
        }finally{
            if(res!=null){
                res.close();
            }
            if(stmt !=null){
                stmt.close();
            }
        }
        return cnt;
    }























    }








