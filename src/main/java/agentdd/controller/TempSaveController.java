package agentdd.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.Map;
import agentdd.model.datacheck.InputChecks;
import agentdd.model.dao.VehicleDao;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import agentdd.model.constant.ErrorMsgConst;
import agentdd.model.constant.SystemConst;
import agentdd.model.dao.ConnectionManager;
import agentdd.model.dao.TempSaveDao;
import agentdd.model.data.Claim;
import agentdd.model.data.Contract;
import agentdd.model.data.LoginUser;
import agentdd.model.data.TempSave;

@WebServlet("/tempSave")
public class TempSaveController extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final int TEMP_SAVE_LIMIT = 5;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
                    throws ServletException, IOException {

        req.setCharacterEncoding(SystemConst.CHAR_SET);

        HttpSession session = req.getSession(false);
        if (session == null || !(session.getAttribute("loginUser") instanceof LoginUser loginUser)
                || loginUser.getUserId() == null || loginUser.getUserId().isBlank()) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String userId = loginUser.getUserId();

        Contract contract = readContract(req);
        Claim claim = readClaim(req);
        TempSave tempSave = new TempSave();
        tempSave.setTempSaveId(createTempSaveId());
        tempSave.setUserId(userId);
        tempSave.setCreatedAt(LocalDateTime.now());
        tempSave.setContract(contract);
        tempSave.setClaim(claim);

        try (Connection con = ConnectionManager.getConnection()) {
            Map<String, String> fieldErrors = InputChecks.estimate(req, true);
            var vehicles = new VehicleDao(con).findAll();
            if (claim.getMaker() != null && !claim.getMaker().isBlank()
                    && vehicles.stream().noneMatch(v -> java.util.Objects.equals(v.get("maker"), claim.getMaker()))) {
                fieldErrors.put("maker", "メーカーを選び直してください。");
            }
            if (claim.getCarName() != null && !claim.getCarName().isBlank()
                    && vehicles.stream().noneMatch(v -> java.util.Objects.equals(v.get("maker"), claim.getMaker())
                    && java.util.Objects.equals(v.get("name"), claim.getCarName()))) {
                fieldErrors.put("carName", "メーカーに対応する車名を選択してください。");
            }
            if (!fieldErrors.isEmpty()) {
                req.setAttribute("fieldErrors", fieldErrors);
                req.setAttribute("contract", contract);
                req.setAttribute("claim", claim);
                req.setAttribute("calculated", false);
                req.setAttribute("vehicles", vehicles);
                req.setAttribute("tempSaveList", new TempSaveDao(con).selectAll(userId));
                req.getRequestDispatcher("/WEB-INF/view/estimate/estimate.jsp").forward(req, resp);
                return;
            }
            TempSaveDao tempSaveDao = new TempSaveDao(con);
            boolean lockHeld = false;
            boolean transactionStarted = false;
            try {
                // countとinsertの間に別リクエストが入らないようにする。
                tempSaveDao.lockUser(userId);
                lockHeld = true;

                con.setAutoCommit(false);
                transactionStarted = true;
                tempSaveDao.deleteExpired(userId, LocalDateTime.now().minusMonths(1));

                if (tempSaveDao.countByUserId(userId) >= TEMP_SAVE_LIMIT) {
                    // 期限切れの掃除だけは確定してから上限画面へ戻す。
                    con.commit();
                    req.setAttribute("fieldErrors", Map.of("_form", "一時保存できる件数は5件までです。一覧から不要な保存を削除してください。"));
                    req.setAttribute("contract", contract);
                    req.setAttribute("claim", claim);
                    req.setAttribute("calculated", false);
                    req.setAttribute("vehicles", vehicles);
                    req.setAttribute("tempSaveList", tempSaveDao.selectAll(userId));
                    req.getRequestDispatcher("/WEB-INF/view/estimate/estimate.jsp").forward(req, resp);
                    return;
                }
                if (tempSaveDao.insert(tempSave) != 1) {
                    throw new SQLException("一時保存情報を登録できませんでした。");
                }
                con.commit();
            } catch (SQLException | RuntimeException e) {
                if (transactionStarted) {
                    try {
                        con.rollback();
                    } catch (SQLException rollbackError) {
                        e.addSuppressed(rollbackError);
                    }
                }
                throw e;
            } finally {
                if (lockHeld) {
                    try {
                        tempSaveDao.unlockUser(userId);
                    } catch (SQLException unlockError) {
                        getServletContext().log(
                                "一時保存処理のロック解放に失敗しました。", unlockError);
                    }
                }
            }
        } catch (SQLException e) {
            getServletContext().log("一時保存情報の登録に失敗しました。", e);
            req.setAttribute("error", ErrorMsgConst.SYSTEM_ERROR);
            req.setAttribute("errorBackUrl", "/estimatecalc");
            req.setAttribute("errorBackLabel", "試算画面へ戻る");
            req.getRequestDispatcher("/WEB-INF/view/error/error.jsp")
                    .forward(req, resp);
            return;
        } finally {
            con.close();
        }
        // POST後に再送信されないよう、保存結果はPRGで返す。
        resp.sendRedirect(req.getContextPath() + "/estimatecalc?result=saved");
    }

    private Contract readContract(HttpServletRequest req) {
        Contract contract = new Contract();

        // 一時保存は新規試算の下書きなので、状態は固定値にする。
        contract.setStatusFlg(1);
        contract.setCancelFlg(false);
        contract.setInceptionDate(normalize(req.getParameter("inceptionDate")));
        contract.setInceptionTime(req.getParameter("inceptionTime"));
        contract.setConclusionDate(normalize(req.getParameter("conclusionDate")));
        contract.setConclusionTime(req.getParameter("conclusionTime"));
        contract.setPaymentMethod(toInteger(req.getParameter("paymentMethod")));
        contract.setInstallment(toInteger(req.getParameter("installment")));
        Integer insuredKbn = toInteger(req.getParameter("insuredKbn"));
        contract.setInsuredKbn(insuredKbn);
        contract.setNameKana1(selectNameParameter(req, "nameKana1", insuredKbn));
        contract.setNameKana2(selectNameParameter(req, "nameKana2", insuredKbn));
        contract.setNameKanji1(selectNameParameter(req, "nameKanji1", insuredKbn));
        contract.setNameKanji2(selectNameParameter(req, "nameKanji2", insuredKbn));
        contract.setPostcode(normalize(req.getParameter("postcode")));
        contract.setAddressKana1(req.getParameter("addressKana1"));
        contract.setAddressKana2(req.getParameter("addressKana2"));
        contract.setAddressKanji1(req.getParameter("addressKanji1"));
        contract.setAddressKanji2(req.getParameter("addressKanji2"));
        contract.setBirthday(normalize(req.getParameter("birthday")));
        contract.setGender(toInteger(req.getParameter("gender")));
        contract.setTelephoneNo(normalize(req.getParameter("telephoneNo")));
        contract.setMobilephoneNo(normalize(req.getParameter("mobilephoneNo")));
        contract.setFaxNo(normalize(req.getParameter("faxNo")));
        return contract;
    }

    private Claim readClaim(HttpServletRequest req) {
        Claim claim = new Claim();
        claim.setMaker(req.getParameter("maker"));
        claim.setCarName(req.getParameter("carName"));
        claim.setLicenseNo(req.getParameter("licenseNo"));
        // 試算後の料率はoutput要素なのでPOSTされない。再開時に再試算する。
        claim.setVehiclePrice(toInteger(req.getParameter("vehiclePrice")));
        claim.setVehicleRates(toInteger(req.getParameter("vehicleRates")));
        claim.setBodilyRates(toInteger(req.getParameter("bodilyRates")));
        claim.setPropertyDamageRates(toInteger(req.getParameter("propertyDamageRates")));
        claim.setAccidentRates(toInteger(req.getParameter("accidentRates")));
        claim.setLicenseColor(toInteger(req.getParameter("licenseColor")));
        claim.setAgeLimit(toInteger(req.getParameter("ageLimit")));
        return claim;
    }

    private String normalize(String value) {
        return value == null ? null : value.replace("/", "").replace("-", "");
    }

    private Integer toInteger(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        try {
            return Integer.valueOf(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private String selectNameParameter(
            HttpServletRequest req, String name, Integer insuredKbn) {
        String[] values = req.getParameterValues(name);
        if (values == null || values.length == 0) {
            return null;
        }
        if (Integer.valueOf(2).equals(insuredKbn) && values.length > 1) {
            return values[values.length - 1];
        }
        return values[0];
    }

    private String createTempSaveId() {
        // 既存tempsave_tblのsave_no長に合わせた形式。列長を確認できる場合はUUID全体でもよい。
        return UUID.randomUUID().toString().replace("-", "").substring(0, 11);
    }
}