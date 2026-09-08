package agentdd.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import agentdd.model.data.Contract;

public class ContractDao {

    private final Connection con;

    private static final String SELECT_CONTRACT =
            "SELECT c.*, p.name AS payment_name, s.name AS status_name, "
            + "i.name AS insured_name, g.name AS gender_name "
            + "FROM CONTRACTINFO_TBL c "
            + "LEFT JOIN M_PAYMENT_METHOD_TBL p ON c.payment_method = p.id "
            + "LEFT JOIN M_CONTRACTSTATUS_TBL s ON c.status_flg = s.id "
            + "LEFT JOIN M_INSURED_TBL i ON c.insured_kbn = i.id "
            + "LEFT JOIN M_GENDER_TBL g ON c.gender = g.id ";

    public ContractDao(Connection con) {
        this.con = con;
    }

    public Contract getContractForAccount(String insatsuRenban) throws SQLException {
        return selectContract(SELECT_CONTRACT + "WHERE c.insatsu_renban = ?", insatsuRenban);
    }

    /*
    public Contract getContractForInquiry(String polNo) throws SQLException {
        return selectContract(SELECT_CONTRACT + "WHERE c.pol_no = ?", polNo);
    }
    */

    public Contract getContract(String polNo) throws SQLException {
        return selectContract(SELECT_CONTRACT + "WHERE c.pol_no = ?", polNo);
    }

    private Contract selectContract(String sql, String value) throws SQLException {
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, value);
            try (ResultSet res = stmt.executeQuery()) {
                if (res.next()) {
                    return createContract(res);
                }
            }
        }
        return null;
    }

    public String getEstimate() throws SQLException {
        String sql = "SELECT insatsu_renban FROM CONTRACTINFO_TBL "
                + "ORDER BY insatsu_renban DESC LIMIT 1";
        if (!con.getAutoCommit()) {
            sql += " FOR UPDATE";
        }
        return selectMaxNumber(sql, "insatsu_renban", "A0000000");
    }

    public void setAccount(String insatsuRenban) throws SQLException {
        boolean ownTransaction = con.getAutoCommit();
        if (ownTransaction) {
            con.setAutoCommit(false);
        }
        SQLException failure = null;
        try {
            String polNo = generateNextPolNo();
            String sql = "UPDATE CONTRACTINFO_TBL "
                    + "SET status_flg = 0, pol_no = ? "
                    + "WHERE insatsu_renban = ? AND status_flg = 1 "
                    + "AND cancel_flg = b'0' AND pol_no IS NULL";
            try (PreparedStatement stmt = con.prepareStatement(sql)) {
                stmt.setString(1, polNo);
                stmt.setString(2, insatsuRenban);
                if (stmt.executeUpdate() != 1) {
                    throw new SQLException("計上対象が存在しないか、計上可能な状態ではありません。");
                }
            }
            if (ownTransaction) {
                con.commit();
            }
        } catch (SQLException e) {
            failure = e;
            if (ownTransaction) {
                try {
                    con.rollback();
                } catch (SQLException rollbackError) {
                    e.addSuppressed(rollbackError);
                    // ロールバック失敗時は自動コミットに戻さず、呼び出し元で接続を閉じる。
                    ownTransaction = false;
                }
            }
            throw e;
        } finally {
            if (ownTransaction) {
                try {
                    con.setAutoCommit(true);
                } catch (SQLException resetError) {
                    if (failure != null) {
                        failure.addSuppressed(resetError);
                    } else {
                        throw resetError;
                    }
                }
            }
        }
    }

    public void setCancel(String polNo) throws SQLException {
        if (updateCancellation("pol_no", polNo) != 1) {
            throw new SQLException("解約対象が存在しないか、解約可能な状態ではありません。");
        }
    }

    public int setEstimate(Contract contract) throws SQLException {
        if (contract == null || contract.getInsatsuRenban() == null
                || !contract.getInsatsuRenban().matches("A[0-9]{7}")
                || "A0000000".equals(contract.getInsatsuRenban())) {
            throw new SQLException("有効な印刷連番を設定してください。");
        }
        if (!Integer.valueOf(1).equals(contract.getStatusFlg()) || contract.isCancelFlg()
                || contract.getPolNo() != null) {
            throw new SQLException("新規試算は状態フラグ1、解約フラグfalse、証券番号nullで登録してください。");
        }
        String sql = "INSERT INTO CONTRACTINFO_TBL ("
                + "insatsu_renban, status_flg, cancel_flg, inception_date, "
                + "inception_time, conclusion_date, conclusion_time, payment_method, "
                + "installment, insured_kbn, name_kana1, name_kana2, "
                + "name_kanji1, name_kanji2, postcode, address_kana1, "
                + "address_kana2, address_kanji1, address_kanji2, birthday, "
                + "gender, telephone_no, mobilephone_no, fax_no"
                + ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, contract.getInsatsuRenban());
            stmt.setObject(2, contract.getStatusFlg(), Types.INTEGER);
            stmt.setBoolean(3, contract.isCancelFlg());
            stmt.setString(4, contract.getInceptionDate());
            stmt.setString(5, contract.getInceptionTime());
            stmt.setString(6, contract.getConclusionDate());
            stmt.setString(7, contract.getConclusionTime());
            stmt.setObject(8, contract.getPaymentMethod(), Types.INTEGER);
            stmt.setObject(9, contract.getInstallment(), Types.INTEGER);
            stmt.setObject(10, contract.getInsuredKbn(), Types.INTEGER);
            stmt.setString(11, contract.getNameKana1());
            stmt.setString(12, contract.getNameKana2());
            stmt.setString(13, contract.getNameKanji1());
            stmt.setString(14, contract.getNameKanji2());
            stmt.setString(15, contract.getPostcode());
            stmt.setString(16, contract.getAddressKana1());
            stmt.setString(17, contract.getAddressKana2());
            stmt.setString(18, contract.getAddressKanji1());
            stmt.setString(19, contract.getAddressKanji2());
            stmt.setString(20, contract.getBirthday());
            stmt.setObject(21, contract.getGender(), Types.INTEGER);
            stmt.setString(22, contract.getTelephoneNo());
            stmt.setString(23, contract.getMobilephoneNo());
            stmt.setString(24, contract.getFaxNo());
            return stmt.executeUpdate();
        }
    }

    public String generateNextInsatsurenban() throws SQLException {
        requireTransaction();
        return nextNumber(getEstimate(), 'A', 7);
    }

    public String getMaxPolNo() throws SQLException {
        // NULL行も含めてロックするためWHERE pol_no IS NOT NULLは付けない。
        // InnoDBの同一トランザクション内で採番から契約更新まで使用する。
        String sql = "SELECT pol_no FROM CONTRACTINFO_TBL ORDER BY pol_no DESC";
        if (!con.getAutoCommit()) {
            sql += " FOR UPDATE";
        }
        return selectMaxNumber(sql, "pol_no", "B000000000");
    }

    /** 次の証券番号を生成する（B000000001～B999999999）。単独呼び出しでは保存されない。 */
    public String generateNextPolNo() throws SQLException {
        requireTransaction();
        return nextNumber(getMaxPolNo(), 'B', 9);
    }

    private String selectMaxNumber(String sql, String column, String initial) throws SQLException {
        try (PreparedStatement stmt = con.prepareStatement(sql);
                ResultSet res = stmt.executeQuery()) {
            if (res.next()) {
                String value = res.getString(column);
                if (value != null) {
                    return value;
                }
            }
        }
        return initial;
    }

    private String nextNumber(String current, char prefix, int digits) throws SQLException {
        if (!current.matches(prefix + "[0-9]{" + digits + "}")) {
            throw new SQLException("採番元の番号形式が不正です：" + current);
        }
        long number = Long.parseLong(current.substring(1));
        long maximum = digits == 7 ? 9_999_999L : 999_999_999L;
        if (number >= maximum) {
            throw new SQLException("採番可能な番号の上限に達しました。");
        }
        return String.format(java.util.Locale.ROOT, "%c%0" + digits + "d", prefix, number + 1);
    }

    private void requireTransaction() throws SQLException {
        if (con.getAutoCommit()) {
            throw new SQLException("採番前にsetAutoCommit(false)を設定し、登録・更新まで同一接続で実行してください。");
        }
    }

    private int updateCancellation(String keyColumn, String value) throws SQLException {
        // keyColumnは本クラス内の固定値のみ使用し、入力値はプレースホルダーに設定する。
        String sql = "UPDATE CONTRACTINFO_TBL SET cancel_flg = b'1', status_flg = 9 "
                + "WHERE " + keyColumn + " = ? AND cancel_flg = b'0' AND status_flg = 0";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, value);
            return stmt.executeUpdate();
        }
    }

    public boolean updateKeijoStatus(String insatsurenban) throws SQLException {
        setAccount(insatsurenban);
        return true;
    }

    /** 互換用。従来どおり印刷連番を受け取り、解約フラグと状態フラグの両方を変更する。 */
    public boolean updateCancelFlag(String insatsurenban) throws SQLException {
        return updateCancellation("insatsu_renban", insatsurenban) == 1;
    }

    /** 互換用。新規試算を登録し、登録できた場合はtrueを返す。 */
    public boolean insertContract(Contract contract) throws SQLException {
        return setEstimate(contract) == 1;
    }

    private Contract createContract(ResultSet res) throws SQLException {
        Contract contract = new Contract();
        contract.setInsatsuRenban(res.getString("insatsu_renban"));
        contract.setPolNo(res.getString("pol_no"));
        contract.setStatusFlg(res.getObject("status_flg", Integer.class));
        contract.setCancelFlg(res.getBoolean("cancel_flg"));
        contract.setInceptionDate(res.getString("inception_date"));
        contract.setInceptionTime(res.getString("inception_time"));
        contract.setConclusionDate(res.getString("conclusion_date"));
        contract.setConclusionTime(res.getString("conclusion_time"));
        contract.setPaymentMethod(res.getObject("payment_method", Integer.class));
        contract.setInstallment(res.getObject("installment", Integer.class));
        contract.setInsuredKbn(res.getObject("insured_kbn", Integer.class));
        contract.setNameKana1(res.getString("name_kana1"));
        contract.setNameKana2(res.getString("name_kana2"));
        contract.setNameKanji1(res.getString("name_kanji1"));
        contract.setNameKanji2(res.getString("name_kanji2"));
        contract.setPostcode(res.getString("postcode"));
        contract.setAddressKana1(res.getString("address_kana1"));
        contract.setAddressKana2(res.getString("address_kana2"));
        contract.setAddressKanji1(res.getString("address_kanji1"));
        contract.setAddressKanji2(res.getString("address_kanji2"));
        contract.setBirthday(res.getString("birthday"));
        contract.setGender(res.getObject("gender", Integer.class));
        contract.setTelephoneNo(res.getString("telephone_no"));
        contract.setMobilephoneNo(res.getString("mobilephone_no"));
        contract.setFaxNo(res.getString("fax_no"));
        contract.setPaymentStr(res.getString("payment_name"));
        contract.setStatusStr(res.getString("status_name"));
        contract.setInsuredStr(res.getString("insured_name"));
        contract.setGenderStr(res.getString("gender_name"));
        return contract;
    }
}