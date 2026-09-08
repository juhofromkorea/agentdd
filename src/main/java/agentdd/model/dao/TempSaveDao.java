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

//一時保存TBLへの登録・取得・削除を行うDAOクラス
public class TempSaveDao {

        // DB接続情報
        private Connection con;

        // コンストラクタ
        public TempSaveDao(Connection con) {
                this.con = con;
        }

        // 一時保存件数取得
        public int countByUserId(String userId)
                        throws SQLException {

                // ログインユーザーに紐づく一時保存件数を取得
                String sql = "SELECT COUNT(*) "
                                + "FROM tempsave_tbl "
                                + "WHERE `user` = ?";

                try (PreparedStatement pstmt = con.prepareStatement(sql)) {

                        // ユーザーIDを設定
                        pstmt.setString(1, userId);

                        try (ResultSet rs = pstmt.executeQuery()) {

                                // 件数を返却
                                if (rs.next()) {
                                        return rs.getInt(1);
                                }
                        }
                }
                return 0;
        }

        // 一時保存削除
        public int delete(String tempSaveId, String userId)
                        throws SQLException {

                // 一時保存番号とユーザーIDを条件に削除
                String sql = "DELETE FROM tempsave_tbl "
                                + "WHERE save_no = ? "
                                + "AND `user` = ?";

                try (PreparedStatement pstmt = con.prepareStatement(sql)) {

                        // 一時保存番号・ユーザーIDを設定
                        pstmt.setString(1, tempSaveId);
                        pstmt.setString(2, userId);

                        // 削除件数を返却
                        return pstmt.executeUpdate();
                }
        }

        // 一時保存一覧取得
        public List<TempSave> selectAll(String userId) throws SQLException {

                // 取得した一時保存情報を格納するリスト
                List<TempSave> tempSaveList = new ArrayList<>();

                // ログインユーザーの一時保存一覧を登録日時の降順で取得
                String sql = "SELECT save_no, `user`, created_at, "
                                + "name_kanji1, name_kanji2, postcode, "
                                + "address_kanji1, address_kanji2, telephone_no "
                                + "FROM tempsave_tbl "
                                + "WHERE user = ? "
                                + "ORDER BY created_at DESC";
                try (PreparedStatement pstmt = con.prepareStatement(sql)) {

                        // ユーザーIDを設定
                        pstmt.setString(1, userId);

                        try (ResultSet rs = pstmt.executeQuery()) {

                                // 取得結果を1件ずつ処理
                                while (rs.next()) {

                                        TempSave tempSave = new TempSave();
                                        Contract contract = new Contract();
                                        tempSave.setTempSaveId(rs.getString("save_no"));
                                        tempSave.setUserId(rs.getString("user"));
                                        tempSave.setCreatedAt(rs.getObject("created_at", LocalDateTime.class));

                                        // 一覧表示用の契約情報を設定
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

        // 一時保存再開処理
        public TempSave select(String tempSaveId, String userId)
                        throws SQLException {

                // 一時保存番号とユーザーIDを条件に再開対象データを取得
                String sql = "SELECT * "
                                + "FROM tempsave_tbl "
                                + "WHERE save_no = ? "
                                + "AND `user` = ?";

                try (PreparedStatement pstmt = con.prepareStatement(sql)) {

                        // 一時保存番号・ユーザーIDを設定
                        pstmt.setString(1, tempSaveId);
                        pstmt.setString(2, userId);

                        try (ResultSet rs = pstmt.executeQuery()) {

                                if (rs.next()) {

                                        TempSave tempSave = new TempSave();
                                        Contract contract = new Contract();
                                        Claim claim = new Claim();

                                        // 一時保存情報を設定
                                        tempSave.setTempSaveId(rs.getString("save_no"));
                                        tempSave.setUserId(rs.getString("user"));
                                        tempSave.setCreatedAt(rs.getObject("created_at", LocalDateTime.class));

                                        // Contract
                                        contract.setStatusFlg(
                                                        rs.getObject("status_flg", Integer.class));

                                        contract.setCancelFlg(
                                                        rs.getBoolean("cancel_flg"));

                                        contract.setInceptionDate(
                                                        rs.getString("inception_date"));

                                        contract.setInceptionTime(
                                                        rs.getString("inception_time"));

                                        contract.setConclusionDate(
                                                        rs.getString("conclusion_date"));

                                        contract.setConclusionTime(
                                                        rs.getString("conclusion_time"));

                                        contract.setPaymentMethod(
                                                        rs.getObject("payment_method", Integer.class));

                                        contract.setInstallment(
                                                        rs.getObject("installment", Integer.class));

                                        contract.setInsuredKbn(
                                                        rs.getObject("insured_kbn", Integer.class));

                                        contract.setNameKana1(
                                                        rs.getString("name_kana1"));

                                        contract.setNameKana2(
                                                        rs.getString("name_kana2"));

                                        contract.setNameKanji1(
                                                        rs.getString("name_kanji1"));

                                        contract.setNameKanji2(
                                                        rs.getString("name_kanji2"));

                                        contract.setPostcode(
                                                        rs.getString("postcode"));

                                        contract.setAddressKana1(
                                                        rs.getString("address_kana1"));

                                        contract.setAddressKana2(
                                                        rs.getString("address_kana2"));

                                        contract.setAddressKanji1(
                                                        rs.getString("address_kanji1"));

                                        contract.setAddressKanji2(
                                                        rs.getString("address_kanji2"));

                                        contract.setBirthday(
                                                        rs.getString("birthday"));

                                        contract.setGender(
                                                        rs.getObject("gender", Integer.class));

                                        contract.setTelephoneNo(
                                                        rs.getString("telephone_no"));

                                        contract.setMobilephoneNo(
                                                        rs.getString("mobilephone_no"));

                                        contract.setFaxNo(
                                                        rs.getString("fax_no"));

                                        // Claim
                                        claim.setMaker(
                                                        rs.getString("maker"));

                                        claim.setCarName(
                                                        rs.getString("car_name"));

                                        claim.setLicenseNo(
                                                        rs.getString("license_no"));

                                        claim.setVehiclePrice(
                                                        rs.getObject("vehicle_price", Integer.class));

                                        claim.setVehicleRates(
                                                        rs.getObject("vehicle_rates", Integer.class));

                                        claim.setBodilyRates(
                                                        rs.getObject("bodily_rates", Integer.class));

                                        claim.setPropertyDamageRates(
                                                        rs.getObject("property_damage_rates", Integer.class));

                                        claim.setAccidentRates(
                                                        rs.getObject("accident_rates", Integer.class));

                                        claim.setLicenseColor(
                                                        rs.getObject("license_color", Integer.class));

                                        claim.setAgeLimit(
                                                        rs.getObject("age_limit", Integer.class));

                                        // TempSave に契約情報・補償情報を入れる
                                        tempSave.setContract(contract);
                                        tempSave.setClaim(claim);

                                        return tempSave;
                                }
                        }
                }

                // 対象データが存在しない場合
                return null;
        }

        // 一時保存処理
        public int insert(TempSave tempSave) throws SQLException {

                // 一時保存TBLへ契約情報・補償情報を登録
                String sql = "INSERT INTO tempsave_tbl ("
                                + "`user`, save_no, created_at, "
                                + "status_flg, cancel_flg, "
                                + "inception_date, inception_time, conclusion_date, conclusion_time, "
                                + "payment_method, installment, insured_kbn, "
                                + "name_kana1, name_kana2, name_kanji1, name_kanji2, "
                                + "postcode, address_kana1, address_kana2, "
                                + "address_kanji1, address_kanji2, birthday, gender, "
                                + "telephone_no, mobilephone_no, fax_no, "
                                + "maker, car_name, license_no, vehicle_price, "
                                + "vehicle_rates, bodily_rates, property_damage_rates, "
                                + "accident_rates, license_color, age_limit"
                                + ") VALUES ("
                                + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
                                + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
                                + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
                                + "?, ?, ?, ?, ?, ? "
                                + ")";

                try (PreparedStatement pstmt = con.prepareStatement(sql)) {

                        // 一時保存情報から契約情報・補償情報を取得
                        Contract contract = tempSave.getContract();
                        Claim claim = tempSave.getClaim();

                        // SQLの?に設定する位置番号
                        int i = 1;

                        // TempSave
                        pstmt.setString(i++, tempSave.getUserId());
                        pstmt.setString(i++, tempSave.getTempSaveId());
                        pstmt.setObject(i++, tempSave.getCreatedAt());

                        // Contract
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

}