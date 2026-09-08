package agentdd.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.UUID;

import agentdd.model.constant.SystemConst;
import agentdd.model.dao.ConnectionManager;
import agentdd.model.dao.TempSaveDao;
import agentdd.model.data.Claim;
import agentdd.model.data.Contract;
import agentdd.model.data.TempSave;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/tempSave")
public class TempSaveController extends HttpServlet {

    private static final long serialVersionUID = 1L;

    // 一時保存上限件数
    private static final int TEMP_SAVE_LIMIT = 5;

    @Override
    protected void doPost(
            HttpServletRequest req,
            HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding(SystemConst.CHAR_SET);

        String nextJsp = "/WEB-INF/view/Estimate.jsp";

        Connection con = null;

        try {

            /*
             * 1. リクエストパラメータから契約情報を取得
             */
            Contract contract = new Contract();

            contract.setStatusFlg(
                    toInteger(req.getParameter("statusFlg")));

            contract.setCancelFlg(
                    Boolean.parseBoolean(
                            req.getParameter("cancelFlg")));

            contract.setInceptionDate(
                    req.getParameter("inceptionDate"));

            contract.setInceptionTime(
                    req.getParameter("inceptionTime"));

            contract.setConclusionDate(
                    req.getParameter("conclusionDate"));

            contract.setConclusionTime(
                    req.getParameter("conclusionTime"));

            contract.setPaymentMethod(
                    toInteger(
                            req.getParameter("paymentMethod")));

            contract.setInstallment(
                    toInteger(
                            req.getParameter("installment")));

            contract.setInsuredKbn(
                    toInteger(
                            req.getParameter("insuredKbn")));

            contract.setNameKana1(
                    req.getParameter("nameKana1"));

            contract.setNameKana2(
                    req.getParameter("nameKana2"));

            contract.setNameKanji1(
                    req.getParameter("nameKanji1"));

            contract.setNameKanji2(
                    req.getParameter("nameKanji2"));

            contract.setPostcode(
                    req.getParameter("postcode"));

            contract.setAddressKana1(
                    req.getParameter("addressKana1"));

            contract.setAddressKana2(
                    req.getParameter("addressKana2"));

            contract.setAddressKanji1(
                    req.getParameter("addressKanji1"));

            contract.setAddressKanji2(
                    req.getParameter("addressKanji2"));

            contract.setBirthday(
                    req.getParameter("birthday"));

            contract.setGender(
                    toInteger(
                            req.getParameter("gender")));

            contract.setTelephoneNo(
                    req.getParameter("telephoneNo"));

            contract.setMobilephoneNo(
                    req.getParameter("mobilephoneNo"));

            contract.setFaxNo(
                    req.getParameter("faxNo"));


            /*
             * 2. リクエストパラメータから補償情報を取得
             */
            Claim claim = new Claim();

            claim.setMaker(
                    req.getParameter("maker"));

            claim.setCarName(
                    req.getParameter("carName"));

            claim.setLicenseNo(
                    req.getParameter("licenseNo"));

            claim.setVehiclePrice(
                    toInteger(
                            req.getParameter("vehiclePrice")));

            claim.setVehicleRates(
                    toInteger(
                            req.getParameter("vehicleRates")));

            claim.setBodilyRates(
                    toInteger(
                            req.getParameter("bodilyRates")));

            claim.setPropertyDamageRates(
                    toInteger(
                            req.getParameter(
                                    "propertyDamageRates")));

            claim.setAccidentRates(
                    toInteger(
                            req.getParameter("accidentRates")));

            claim.setLicenseColor(
                    toInteger(
                            req.getParameter("licenseColor")));

            claim.setAgeLimit(
                    toInteger(
                            req.getParameter("ageLimit")));


            /*
             * 3. セッションからログインユーザーIDを取得
             */
            HttpSession session =
                    req.getSession(false);

            if (session == null
                    || session.getAttribute("userId") == null) {

                throw new ServletException(
                        "ログインユーザーIDを取得できません。");
            }

            String userId =
                    (String) session.getAttribute("userId");


            /*
             * 4. DB接続
             */
            con = ConnectionManager.getConnection();

            con.setAutoCommit(false);

            TempSaveDao tempSaveDao =
                    new TempSaveDao(con);


            /*
             * 5. ログインユーザーの一時保存件数を取得
             */
            int count =
                    tempSaveDao.countByUserId(userId);


            /*
             * 6. 上限件数チェック
             */
            if (count >= TEMP_SAVE_LIMIT) {

                req.setAttribute(
                        "errorMessage",
                        "一時保存できる件数は5件までです。");

                req.getRequestDispatcher(nextJsp)
                        .forward(req, resp);

                return;
            }


            /*
             * 7. 一時保存情報を設定
             */
            TempSave tempSave = new TempSave();

            // 一時保存番号
            tempSave.setTempSaveId(
                    createTempSaveId());

            // ログインユーザーID
            tempSave.setUserId(userId);

            // 登録日時
            tempSave.setCreatedAt(
                    LocalDateTime.now());

            // 契約情報
            tempSave.setContract(contract);

            // 補償情報
            tempSave.setClaim(claim);


            /*
             * 8. 一時保存情報をDBに登録
             */
            tempSaveDao.insert(tempSave);

            con.commit();

            req.setAttribute(
                    "message",
                    "一時保存しました。");

        } catch (SQLException e) {

            if (con != null) {

                try {
                    con.rollback();

                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }

            throw new ServletException(e);

        } finally {

            if (con != null) {

                try {
                    con.close();

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }


        /*
         * 9. 新規試算画面JSPにforward
         */
        req.getRequestDispatcher(nextJsp)
                .forward(req, resp);
    }


    /*
     * String → Integer変換
     * 未入力の場合はnull
     */
    private Integer toInteger(String value) {

        if (value == null || value.isBlank()) {
            return null;
        }

        return Integer.valueOf(value);
    }


    /*
     * 一時保存番号生成
     */
    private String createTempSaveId() {

        return UUID.randomUUID()
                .toString()
                .replace("-", "")
                .substring(0, 11);
    }
}