package agentdd.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import agentdd.model.data.Claim;
import agentdd.model.data.Contract;
import agentdd.model.data.TempSave;

/**
 * 一時保存テーブルへのDB操作を担当するDAOです。
 * 接続の生成・終了とcommit/rollbackは呼び出し元のコントローラーが管理します。
 */
public class TempSaveDao {

    private static final String TEMP_SAVE_LOCK_PREFIX = "agentdd.tempsave.";

    private final Connection con;

    public TempSaveDao(Connection con) {
        this.con = Objects.requireNonNull(con);
    }

    /**
     * 同じユーザーの保存処理を直列化します。
     * 件数の確認とINSERTの間に別リクエストが割り込むと6件目が入るため、
     * MySQLの接続ロックを使い、テーブル変更なしで上限チェックを原子的にします。
     */
    public void lockUser(String userId) throws SQLException {
        String lockName = lockName(userId);
        try (PreparedStatement ps = con.prepareStatement("SELECT GET_LOCK(?, 5)")) {
            ps.setString(1, lockName);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next() || rs.getInt(1) != 1 || rs.wasNull()) {
                    throw new SQLException("一時保存処理が混み合っています。再度お試しください。");
                }
            }
        }
    }

    /** commit/rollbackでは接続ロックは解放されないため、明示的に解放します。 */
    public void unlockUser(String userId) throws SQLException {
        String lockName = lockName(userId);
        try (PreparedStatement ps = con.prepareStatement("SELECT RELEASE_LOCK(?)")) {
            ps.setString(1, lockName);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next() || rs.getInt(1) != 1 || rs.wasNull()) {
                    throw new SQLException("一時保存処理のロックを解放できませんでした。");
                }
            }
        }
    }

    private String lockName(String userId) {
        return TEMP_SAVE_LOCK_PREFIX + userId;
    }

    /** 作成日時が1か月より前の、指定ユーザーの一時保存を削除します。 */
    public int deleteExpired(String userId, LocalDateTime cutoff) throws SQLException {
        String sql = "DELETE FROM tempsave_tbl "
                + "WHERE `user` = ? AND created_at < ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, userId);
            ps.setTimestamp(2, Timestamp.valueOf(cutoff));
            return ps.executeUpdate();
        }
    }

    public int countByUserId(String userId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM tempsave_tbl WHERE `user` = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        }
    }

    public int delete(String tempSaveId, String userId) throws SQLException {
        String sql = "DELETE FROM tempsave_tbl "
                + "WHERE save_no = ? AND `user` = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, tempSaveId);
            ps.setString(2, userId);
            return ps.executeUpdate();
        }
    }

    /** 指定ユーザーの一時保存一覧を新しい順で取得します。 */
    public List<TempSave> selectAll(String userId) throws SQLException {
        List<TempSave> tempSaveList = new ArrayList<>();
        String sql = "SELECT save_no, `user`, created_at, "
                + "name_kanji1, name_kanji2, postcode, "
                + "address_kanji1, address_kanji2, telephone_no "
                + "FROM tempsave_tbl "
                + "WHERE `user` = ? "
                + "ORDER BY created_at DESC";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    TempSave tempSave = new TempSave();
                    Contract contract = new Contract();

                    tempSave.setTempSaveId(rs.getString("save_no"));
                    tempSave.setUserId(rs.getString("user"));
                    tempSave.setCreatedAt(readDateTime(rs, "created_at"));

                    contract.setNameKanji1(rs.getString("name_kanji1"));
                    contract.setNameKanji2(rs.getString("name_kanji2"));
                    contract.setPostcode(rs.getString("postcode"));
                    contract.setAddressKanji1(rs.getString("address_kanji1"));
                    contract.setAddressKanji2(rs.getString("address_kanji2"));
                    contract.setTelephoneNo(rs.getString("telephone_no"));
                    tempSave.setContract(contract);

                    tempSaveList.add(tempSave);
                }
            }
        }
        return tempSaveList;
    }

    /** ユーザーIDも条件に含めて一時保存を再開します。 */
    public TempSave select(String tempSaveId, String userId) throws SQLException {
        String sql = "SELECT * FROM tempsave_tbl "
                + "WHERE save_no = ? AND `user` = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, tempSaveId);
            ps.setString(2, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }

                TempSave tempSave = new TempSave();
                Contract contract = new Contract();
                Claim claim = new Claim();

                tempSave.setTempSaveId(rs.getString("save_no"));
                tempSave.setUserId(rs.getString("user"));
                tempSave.setCreatedAt(readDateTime(rs, "created_at"));

                contract.setStatusFlg(getInteger(rs, "status_flg"));
                contract.setCancelFlg(rs.getBoolean("cancel_flg"));
                contract.setInceptionDate(rs.getString("inception_date"));
                contract.setInceptionTime(rs.getString("inception_time"));
                contract.setConclusionDate(rs.getString("conclusion_date"));
                contract.setConclusionTime(rs.getString("conclusion_time"));
                contract.setPaymentMethod(getInteger(rs, "payment_method"));
                contract.setInstallment(getInteger(rs, "installment"));
                contract.setInsuredKbn(getInteger(rs, "insured_kbn"));
                contract.setNameKana1(rs.getString("name_kana1"));
                contract.setNameKana2(rs.getString("name_kana2"));
                contract.setNameKanji1(rs.getString("name_kanji1"));
                contract.setNameKanji2(rs.getString("name_kanji2"));
                contract.setPostcode(rs.getString("postcode"));
                contract.setAddressKana1(rs.getString("address_kana1"));
                contract.setAddressKana2(rs.getString("address_kana2"));
                contract.setAddressKanji1(rs.getString("address_kanji1"));
                contract.setAddressKanji2(rs.getString("address_kanji2"));
                contract.setBirthday(rs.getString("birthday"));
                contract.setGender(getInteger(rs, "gender"));
                contract.setTelephoneNo(rs.getString("telephone_no"));
                contract.setMobilephoneNo(rs.getString("mobilephone_no"));
                contract.setFaxNo(rs.getString("fax_no"));

                claim.setMaker(rs.getString("maker"));
                claim.setCarName(rs.getString("car_name"));
                claim.setLicenseNo(rs.getString("license_no"));
                claim.setVehiclePrice(getInteger(rs, "vehicle_price"));
                claim.setVehicleRates(getInteger(rs, "vehicle_rates"));
                claim.setBodilyRates(getInteger(rs, "bodily_rates"));
                claim.setPropertyDamageRates(getInteger(rs, "property_damage_rates"));
                claim.setAccidentRates(getInteger(rs, "accident_rates"));
                claim.setLicenseColor(getInteger(rs, "license_color"));
                claim.setAgeLimit(getInteger(rs, "age_limit"));

                tempSave.setContract(contract);
                tempSave.setClaim(claim);
                return tempSave;
            }
        }
    }

    /** ContractとClaimを一時保存テーブルへ登録します。 */
    public int insert(TempSave tempSave) throws SQLException {
        String sql = "INSERT INTO tempsave_tbl ("
                + "`user`, save_no, created_at, status_flg, cancel_flg, "
                + "inception_date, inception_time, conclusion_date, conclusion_time, "
                + "payment_method, installment, insured_kbn, "
                + "name_kana1, name_kana2, name_kanji1, name_kanji2, "
                + "postcode, address_kana1, address_kana2, address_kanji1, address_kanji2, "
                + "birthday, gender, telephone_no, mobilephone_no, fax_no, "
                + "maker, car_name, license_no, vehicle_price, vehicle_rates, bodily_rates, "
                + "property_damage_rates, accident_rates, license_color, age_limit"
                + ") VALUES ("
                + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
                + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?"
                + ")";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            Contract contract = tempSave.getContract();
            Claim claim = tempSave.getClaim();
            int i = 1;

            ps.setString(i++, tempSave.getUserId());
            ps.setString(i++, tempSave.getTempSaveId());
            setDateTime(ps, i++, tempSave.getCreatedAt());

            setInteger(ps, i++, contract.getStatusFlg());
            ps.setBoolean(i++, contract.isCancelFlg());
            ps.setString(i++, contract.getInceptionDate());
            ps.setString(i++, contract.getInceptionTime());
            ps.setString(i++, contract.getConclusionDate());
            ps.setString(i++, contract.getConclusionTime());
            setInteger(ps, i++, contract.getPaymentMethod());
            setInteger(ps, i++, contract.getInstallment());
            setInteger(ps, i++, contract.getInsuredKbn());
            ps.setString(i++, contract.getNameKana1());
            ps.setString(i++, contract.getNameKana2());
            ps.setString(i++, contract.getNameKanji1());
            ps.setString(i++, contract.getNameKanji2());
            ps.setString(i++, contract.getPostcode());
            ps.setString(i++, contract.getAddressKana1());
            ps.setString(i++, contract.getAddressKana2());
            ps.setString(i++, contract.getAddressKanji1());
            ps.setString(i++, contract.getAddressKanji2());
            ps.setString(i++, contract.getBirthday());
            setInteger(ps, i++, contract.getGender());
            ps.setString(i++, contract.getTelephoneNo());
            ps.setString(i++, contract.getMobilephoneNo());
            ps.setString(i++, contract.getFaxNo());

            ps.setString(i++, claim.getMaker());
            ps.setString(i++, claim.getCarName());
            ps.setString(i++, claim.getLicenseNo());
            setInteger(ps, i++, claim.getVehiclePrice());
            setInteger(ps, i++, claim.getVehicleRates());
            setInteger(ps, i++, claim.getBodilyRates());
            setInteger(ps, i++, claim.getPropertyDamageRates());
            setInteger(ps, i++, claim.getAccidentRates());
            setInteger(ps, i++, claim.getLicenseColor());
            setInteger(ps, i++, claim.getAgeLimit());

            return ps.executeUpdate();
        }
    }

    private LocalDateTime readDateTime(ResultSet rs, String column) throws SQLException {
        Timestamp timestamp = rs.getTimestamp(column);
        return timestamp == null ? null : timestamp.toLocalDateTime();
    }

    private Integer getInteger(ResultSet rs, String column) throws SQLException {
        int value = rs.getInt(column);
        return rs.wasNull() ? null : value;
    }

    private void setDateTime(PreparedStatement ps, int index, LocalDateTime value)
            throws SQLException {
        if (value == null) {
            ps.setNull(index, Types.TIMESTAMP);
        } else {
            ps.setTimestamp(index, Timestamp.valueOf(value));
        }
    }

    private void setInteger(PreparedStatement ps, int index, Integer value)
            throws SQLException {
        if (value == null) {
            ps.setNull(index, Types.INTEGER);
        } else {
            ps.setInt(index, value);
        }
    }
}
