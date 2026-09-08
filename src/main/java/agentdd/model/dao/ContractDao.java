package agentdd.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import agentdd.model.data.Contract;

public class ContractDao {
    private Connection con;

    /**
     * コンストラクタ
     */
    public ContractDao(Connection con){
        this.con = con;
    }

    /** 契約情報一覧 ＜計上＞ */
    public Contract getContractForAccount(String insatsuRenban) throws SQLException{

        ResultSet res = null ;//SQLの結果を入れる
        PreparedStatement stmt = null;

        Contract cnt = null;
        String sql = "SELECT CONTRACTINFO_TBL.*, "
        + "       M_PAYMENT_METHOD_TBL.name AS payment_name, "  
        + "       M_STATUS_TBL.status_name, "
        + "       M_insured_TBL.name AS insured_name, "          
        + "       M_GENDER_TBL.name AS gender_name "             
        + "FROM CONTRACTINFO_TBL "
        + "LEFT JOIN M_PAYMENT_METHOD_TBL "
        + "       ON CONTRACTINFO_TBL.payment_method = M_PAYMENT_METHOD_TBL.code " 
        + "LEFT JOIN M_STATUS_TBL "
        + "       ON CONTRACTINFO_TBL.status_flg = M_STATUS_TBL.status_code "
        + "LEFT JOIN M_insured_TBL "
        + "       ON CONTRACTINFO_TBL.insured_kbn = M_insured_TBL.code "
        + "LEFT JOIN M_GENDER_TBL "
        + "       ON CONTRACTINFO_TBL.gender = M_GENDER_TBL.code "
        + "WHERE CONTRACTINFO_TBL.insatsuRenban = ?";

        try{
            stmt = con.prepareStatement(sql);
            stmt.setString(1,insatsuRenban);
            res = stmt.executeQuery();

            if (res.next()){
                cnt = new Contract();

                cnt.setInsatsuRenban(res.getString("insatsuRenban"));
                cnt.setPolNo(res.getString("polNo"));
                //cnt.setStatusFlg(res.getInt("statusFlg"));
                cnt.setCancelFlg(res.getBoolean("cancel_flg"));
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
                cnt.setAddressKanji2(res.getString("addressKanji2"));
                cnt.setBirthday(res.getString("birthday"));
                cnt.setGender(res.getInt("gender"));
                cnt.setTelephoneNo(res.getString("telephoneNo"));
                cnt.setMobilephoneNo(res.getString("mobilephoneNo"));
                cnt.setFaxNo(res.getString("faxNo"));

                // --- マスタテーブルから取得した「名称（文字列）」をセット ---
            cnt.setPaymentStr(res.getString("payment_name"));
            cnt.setStatusStr(res.getString("status_name"));
            cnt.setInsuredStr(res.getString("insured_name"));
            cnt.setGenderStr(res.getString("gender_name"));
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
        String sql = "SELECT CONTRACTINFO_TBL.*, "
        + "       M_PAYMENT_METHOD_TBL.name AS payment_name, "  
        + "       M_STATUS_TBL.status_name, "
        + "       M_insured_TBL.name AS insured_name, "          
        + "       M_GENDER_TBL.name AS gender_name "             
        + "FROM CONTRACTINFO_TBL "
        + "LEFT JOIN M_PAYMENT_METHOD_TBL "
        + "       ON CONTRACTINFO_TBL.payment_method = M_PAYMENT_METHOD_TBL.code " 
        + "LEFT JOIN M_STATUS_TBL "
        + "       ON CONTRACTINFO_TBL.status_flg = M_STATUS_TBL.status_code "
        + "LEFT JOIN M_insured_TBL "
        + "       ON CONTRACTINFO_TBL.insured_kbn = M_insured_TBL.code "
        + "LEFT JOIN M_GENDER_TBL "
        + "       ON CONTRACTINFO_TBL.gender = M_GENDER_TBL.code "
        + "WHERE CONTRACTINFO_TBL.polNo = ?";

        try{
            stmt = con.prepareStatement(sql);
            stmt.setString(1,polNo);
            res = stmt.executeQuery();

            if (res.next()){
                cnt = new Contract();

                cnt.setInsatsuRenban(res.getString("insatsuRenban"));
                cnt.setPolNo(res.getString("polNo"));
                cnt.setStatusFlg(res.getInt("status_flg"));
                cnt.setCancelFlg(res.getBoolean("cancel_flg"));
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
                cnt.setAddressKanji2(res.getString("addressKanji2"));
                cnt.setBirthday(res.getString("birthday"));
                cnt.setGender(res.getInt("gender"));
                cnt.setTelephoneNo(res.getString("telephoneNo"));
                cnt.setMobilephoneNo(res.getString("mobilephoneNo"));
                cnt.setFaxNo(res.getString("faxNo"));

                // --- マスタテーブルから取得した「名称（文字列）」をセット ---
            cnt.setPaymentStr(res.getString("payment_name"));
            cnt.setStatusStr(res.getString("status_name"));
            cnt.setInsuredStr(res.getString("insured_name"));
            cnt.setGenderStr(res.getString("gender_name"));
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


/**契約情報一覧＜解約・事故受付＞ */
    public Contract getContract(String polNo) throws SQLException{

        ResultSet res = null ;//SQLの結果を入れる
        PreparedStatement stmt = null;

        Contract cnt = null;
        String sql = "SELECT CONTRACTINFO_TBL.*, "
        + "       M_PAYMENT_METHOD_TBL.name AS payment_name, "  
        + "       M_STATUS_TBL.status_name, "
        + "       M_insured_TBL.name AS insured_name, "          
        + "       M_GENDER_TBL.name AS gender_name "             
        + "FROM CONTRACTINFO_TBL "
        + "LEFT JOIN M_PAYMENT_METHOD_TBL "
        + "       ON CONTRACTINFO_TBL.payment_method = M_PAYMENT_METHOD_TBL.code " 
        + "LEFT JOIN M_STATUS_TBL "
        + "       ON CONTRACTINFO_TBL.status_flg = M_STATUS_TBL.status_code "
        + "LEFT JOIN M_insured_TBL "
        + "       ON CONTRACTINFO_TBL.insured_kbn = M_insured_TBL.code "
        + "LEFT JOIN M_GENDER_TBL "
        + "       ON CONTRACTINFO_TBL.gender = M_GENDER_TBL.code "
        + "WHERE CONTRACTINFO_TBL.polNo = ?";

        try{
            stmt = con.prepareStatement(sql);
            stmt.setString(1,polNo);
            res = stmt.executeQuery();

            if (res.next()){
                cnt = new Contract();

                cnt.setInsatsuRenban(res.getString("insatsuRenban"));
                cnt.setPolNo(res.getString("polNo"));
                //cnt.setStatusFlg(res.getInt("statusFlg"));
                cnt.setCancelFlg(res.getBoolean("cancel_flg"));
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
                cnt.setAddressKanji2(res.getString("addressKanji2"));
                cnt.setBirthday(res.getString("birthday"));
                cnt.setGender(res.getInt("gender"));
                cnt.setTelephoneNo(res.getString("telephoneNo"));
                cnt.setMobilephoneNo(res.getString("mobilephoneNo"));
                cnt.setFaxNo(res.getString("faxNo"));

            cnt.setPaymentStr(res.getString("payment_name"));
            cnt.setStatusStr(res.getString("status_name"));
            cnt.setInsuredStr(res.getString("insured_name"));
            cnt.setGenderStr(res.getString("gender_name"));

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


    /** 印刷連番の一番大きい値を取得する */
    public String getEstimate() throws SQLException {
        String sql = "SELECT insatsurenban FROM CONTRACTINFO_TBL ORDER BY insatsurenban DESC LIMIT 1";

        try (PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet res = stmt.executeQuery()) {
            if (res.next()) {
                return res.getString("insatsurenban");
            }
        }
        // 初期値：8桁（英字1桁 + 数字7桁）
        return "A0000001"; 


    }
    /*計上ステータース更新用メソッド */

    public boolean updateKeijoStatus(String insatsurenban) throws SQLException {

    PreparedStatement stmt = null;
    String sql = "UPDATE CONTRACTINFO_TBL SET status_flg = ? WHERE insatsurenban = ?";

    try {
        stmt = con.prepareStatement(sql);
        stmt.setInt(1, 0);
        stmt.setString(2, insatsurenban);

        int updatedRows = stmt.executeUpdate();

        return updatedRows > 0;

    } finally {
        if (stmt != null) {
            stmt.close();
        }
    }
}

/** 
 * 印刷連番発行メソッド (8桁: A0000001〜)
  */
    public String generateNextInsatsurenban() throws SQLException {
        String currentMax = getEstimate(); // 例: "A0000001"
        String prefix = currentMax.substring(0, 1); 
        int number = Integer.parseInt(currentMax.substring(1));
        number++;

        // %s (アルファベット) + %07d (7桁のゼロ埋め数値) => 全計8桁
        return String.format("%s%07d", prefix, number);
    }



/*解約用フラグ変更用メソッド */

public boolean updateCancelFlag(String insatsurenban) throws SQLException {

    PreparedStatement stmt = null;
    
    // cancel_flg を解約状態（1）に更新するSQL
    String sql = "UPDATE CONTRACTINFO_TBL SET cancel_flg = ? WHERE insatsurenban = ?";

    try {
        stmt = con.prepareStatement(sql);
        
        // 1番目の ? に解約を表すフラグ値をセット
        stmt.setInt(1, 1); 
        
        // 2番目の ? に対象の印刷連番をセット
        stmt.setString(2, insatsurenban);

        // SQLを実行し、更新された行数を取得する
        int updatedRows = stmt.executeUpdate();

        // 1件以上更新されていれば成功(true)を返す
        return updatedRows > 0;

    } finally {
        if (stmt != null) {
            stmt.close();
        }
    }
}

/*新規試算登録用メソッド */

public boolean insertContract(Contract cnt) throws SQLException {

    PreparedStatement stmt = null;

    String sql = "INSERT INTO CONTRACTINFO_TBL ("
            + "  insatsuRenban, status_flg, cancel_flg, inceptionDate, inceptionTime, "
            + "  conclusionDate, conclusionTime, paymentMethod, installment, insuredKbn, "
            + "  nameKana1, nameKana2, nameKanji1, nameKanji2, postcode, "
            + "  addressKana1, addressKana2, addressKanji1, addressKanji2, birthday, "
            + "  gender, telephoneNo, mobilephoneNo, faxNo"
            + ") VALUES ("
            + "  ?, ?, ?, ?, ?, "
            + "  ?, ?, ?, ?, ?, "
            + "  ?, ?, ?, ?, ?, "
            + "  ?, ?, ?, ?, ?, "
            + "  ?, ?, ?, ?"
            + ")";

    try {
        stmt = con.prepareStatement(sql);

        stmt.setString(1, cnt.getInsatsuRenban());
        stmt.setInt(2, cnt.getStatusFlg());
        stmt.setBoolean(3, cnt.isCancelFlg()); 
        stmt.setString(4, cnt.getInceptionDate());
        stmt.setString(5, cnt.getInceptionTime());

        stmt.setString(6, cnt.getConclusionDate());
        stmt.setString(7, cnt.getConclusionTime());
        stmt.setInt(8, cnt.getPaymentMethod());
        stmt.setInt(9, cnt.getInstallment());
        stmt.setInt(10, cnt.getInsuredKbn());

        stmt.setString(11, cnt.getNameKana1());
        stmt.setString(12, cnt.getNameKana2());
        stmt.setString(13, cnt.getNameKanji1());
        stmt.setString(14, cnt.getNameKanji2());
        stmt.setString(15, cnt.getPostcode());

        stmt.setString(16, cnt.getAddressKana1());
        stmt.setString(17, cnt.getAddressKana2());
        stmt.setString(18, cnt.getAddressKanji1());
        stmt.setString(19, cnt.getAddressKanji2());
        stmt.setString(20, cnt.getBirthday());

        // 性別の数値フラグ（※DTOのメソッド名に合わせて調整してください）
        stmt.setInt(21, cnt.getGender()); 
        stmt.setString(22, cnt.getTelephoneNo());
        stmt.setString(23, cnt.getMobilephoneNo());
        stmt.setString(24, cnt.getFaxNo());

        int insertedRows = stmt.executeUpdate();
        return insertedRows > 0;

    } finally {
        //  2. stmt のクローズ処理
        if (stmt != null) {
            stmt.close();
        }
    }
}

/** 証券番号の一番大きい値を取得する */
    public String getMaxPolNo() throws SQLException {
        String sql = "SELECT polNo FROM CONTRACTINFO_TBL WHERE polNo IS NOT NULL ORDER BY polNo DESC LIMIT 1";

        try (PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet res = stmt.executeQuery()) {
            if (res.next()) {
                return res.getString("polNo");
            }
        }
        // 初期値：10桁（英字1桁 + 数字9桁）
        return "B000000000"; 
    }

    /** 証券番号発行メソッド (10桁: B000000001) */
    public String generateNextPolNo() throws SQLException {
        String currentMax = getMaxPolNo(); // 例: "B000000001"
        String prefix = currentMax.substring(0, 1); 
        long number = Long.parseLong(currentMax.substring(1)); // 9桁になるためLong型を推奨
        number++;

        // %s (アルファベット) + %09d (9桁のゼロ埋め数値) => 全計10桁
        return String.format("%s%09d", prefix, number);
    }
    }


