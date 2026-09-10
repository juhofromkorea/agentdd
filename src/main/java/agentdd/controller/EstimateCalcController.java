package agentdd.controller;

import java.sql.Connection;
import java.sql.SQLException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.RequestDispatcher;

import agentdd.model.constant.ErrorMsgConst;
import agentdd.model.data.Contract;
import agentdd.model.data.Claim;
import agentdd.model.data.LoginUser;
import agentdd.model.util.InsuranceCalc;
import agentdd.model.dao.TempSaveDao;
import agentdd.model.dao.VehicleDao;
import agentdd.model.dao.RatesDao;
import agentdd.model.dao.ConnectionManager;
import agentdd.model.datacheck.InputChecks;

@WebServlet("/estimatecalc")
public class EstimateCalcController extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (session == null || !(session.getAttribute("loginUser") instanceof LoginUser loginUser)
                || loginUser.getUserId() == null || loginUser.getUserId().isBlank()) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        if ("1".equals(request.getParameter("new"))) {
            session.removeAttribute("contract");
            session.removeAttribute("claim");
            session.removeAttribute("calculated");
            session.removeAttribute("estimateSnapshot");
            session.removeAttribute("printContract");
            session.removeAttribute("printClaim");
        }

        try (Connection con = ConnectionManager.getConnection()) {
            con.setAutoCommit(false);
            try {
                TempSaveDao tempSaveDao = new TempSaveDao(con);
                // バッチを追加しなくても、画面を開いたタイミングで期限切れを掃除する。
                tempSaveDao.deleteExpired(
                        loginUser.getUserId(), LocalDateTime.now().minusMonths(1));
                request.setAttribute(
                        "tempSaveList", tempSaveDao.selectAll(loginUser.getUserId()));
                request.setAttribute("vehicles", new VehicleDao(con).findAll());
                con.commit();
            } catch (SQLException | RuntimeException e) {
                try {
                    con.rollback();
                } catch (SQLException rollbackError) {
                    e.addSuppressed(rollbackError);
                }
                throw e;
            }

            String result = request.getParameter("result");
            if ("saved".equals(result)) {
                request.setAttribute("message", "一時保存しました。");
            } else if ("deleted".equals(result)) {
                request.setAttribute("message", "一時保存を削除しました。");
            } else if ("limit".equals(result)) {
                request.setAttribute("errorMessage", "一時保存できる件数は5件までです。");
            } else if ("recalculate".equals(result)) {
                request.setAttribute("fieldErrors", Map.of("_form", "先に保険料試算・申込書印刷確認を実行してください。"));
            } else if ("missing".equals(result)) {
                request.setAttribute("errorMessage", "一時保存情報が見つかりません。");
            }
            request.setAttribute("openSaved", "saved".equals(request.getParameter("tab")));
            request.getRequestDispatcher(
                    "/WEB-INF/view/estimate/estimate.jsp")
                    .forward(request, response);

        } catch (SQLException e) {
            getServletContext().log(
                    "新規試算画面の車両マスタ取得に失敗しました。", e);

            request.setAttribute("error", ErrorMsgConst.SYSTEM_ERROR);
            request.setAttribute("errorBackUrl", "/top");
            request.setAttribute("errorBackLabel", "トップへ戻る");

            request.getRequestDispatcher(
                    "/WEB-INF/view/error/error.jsp")
                    .forward(request, response);
            return;
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);
        if (session == null || !(session.getAttribute("loginUser") instanceof LoginUser loginUser)
                || loginUser.getUserId() == null || loginUser.getUserId().isBlank()) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        session.removeAttribute("estimateSnapshot");
        session.removeAttribute("calculated");
        session.removeAttribute("printContract");
        session.removeAttribute("printClaim");

        Contract contract = new Contract();
        Claim claim = new Claim();

        // 1. 個人用と法人用で同じname属性を使うため、法人は後方の値を採用する。
        Integer insuredKbn = parseInt(request.getParameter("insuredKbn"), 0);
        contract.setInsuredKbn(insuredKbn);
        contract.setNameKana1(selectNameParameter(request, "nameKana1", insuredKbn));
        contract.setNameKana2(selectNameParameter(request, "nameKana2", insuredKbn));
        contract.setNameKanji1(selectNameParameter(request, "nameKanji1", insuredKbn));
        contract.setNameKanji2(selectNameParameter(request, "nameKanji2", insuredKbn));
        contract.setAddressKana1(request.getParameter("addressKana1"));
        contract.setAddressKana2(request.getParameter("addressKana2"));
        contract.setAddressKanji1(request.getParameter("addressKanji1"));
        contract.setAddressKanji2(request.getParameter("addressKanji2"));
        contract.setInceptionTime(request.getParameter("inceptionTime"));
        contract.setConclusionTime(request.getParameter("conclusionTime"));
        contract.setPaymentMethod(parseInt(request.getParameter("paymentMethod"), 0));
        contract.setInstallment(parseInt(request.getParameter("installment"), 1));
        contract.setGender(parseInt(request.getParameter("gender"), 0));

        // 初期値設定
        contract.setStatusFlg(1);
        contract.setCancelFlg(false);
        contract.setPolNo(null);

        // 3. 記号（/ や -）を消し去ってからセットする項目（日付・郵便番号・電話番号）
        String rawInception = request.getParameter("inceptionDate");
        if (rawInception != null)
            contract.setInceptionDate(rawInception.replace("/", "").replace("-", ""));

        String rawConclusion = request.getParameter("conclusionDate");
        if (rawConclusion != null)
            contract.setConclusionDate(rawConclusion.replace("/", "").replace("-", ""));

        String rawBirthday = request.getParameter("birthday");
        if (rawBirthday != null)
            contract.setBirthday(rawBirthday.replace("/", "").replace("-", ""));

        String rawPost = request.getParameter("postcode");
        if (rawPost != null)
            contract.setPostcode(rawPost.replace("-", ""));

        String rawTel = request.getParameter("telephoneNo");
        if (rawTel != null)
            contract.setTelephoneNo(rawTel.replace("-", ""));

        String rawMobile = request.getParameter("mobilephoneNo");
        if (rawMobile != null)
            contract.setMobilephoneNo(rawMobile.replace("-", ""));

        String rawFax = request.getParameter("faxNo");
        if (rawFax != null)
            contract.setFaxNo(rawFax.replace("-", ""));

        claim.setMaker(request.getParameter("maker"));
        claim.setCarName(request.getParameter("carName"));
        claim.setLicenseNo(request.getParameter("licenseNo"));
        claim.setLicenseColor(parseInt(request.getParameter("licenseColor"), 0)); // parseIntを使う！
        claim.setAgeLimit(parseInt(request.getParameter("ageLimit"), 0));

        VehicleDao vehicleDao = null;
        try (Connection con = ConnectionManager.getConnection()) {
            con.setAutoCommit(false);

            try {
                Map<String, String> fieldErrors = InputChecks.estimate(request, false);
                Object printError = request.getAttribute("estimatePrintError");
                if (printError != null) fieldErrors.put("_form", printError.toString());
                var vehicles = new VehicleDao(con).findAll();
                boolean vehicleExists = vehicles.stream().anyMatch(v ->
                        java.util.Objects.equals(v.get("maker"), claim.getMaker())
                        && java.util.Objects.equals(v.get("name"), claim.getCarName()));
                if (!vehicleExists) fieldErrors.putIfAbsent("carName", "メーカー・車名を選び直してください。");
                if (!fieldErrors.isEmpty()) {
                    request.setAttribute("fieldErrors", fieldErrors);
                    request.setAttribute("contract", contract);
                    request.setAttribute("claim", claim);
                    request.setAttribute("calculated", false);
                    request.setAttribute("vehicles", vehicles);
                    request.setAttribute("tempSaveList", new TempSaveDao(con).selectAll(loginUser.getUserId()));
                    con.rollback();
                    request.getRequestDispatcher("/WEB-INF/view/estimate/estimate.jsp").forward(request, response);
                    return;
                }

                // 2. マスタ情報の取得 (メーカーと車名をキーにする)
                vehicleDao = new VehicleDao(con);
                vehicleDao.getVehicle(claim);

                RatesDao rateDao = new RatesDao(con);

                // 3. マスタの料率IDを RatesDao で実際の数字に変換
                // ※ RateDao が double などの数値を返す前提のコードです
                Double vRate = claim.getVehicleRates() == null ? null : rateDao.getRate(claim.getVehicleRates());
                Double bRate = claim.getBodilyRates() == null ? null : rateDao.getRate(claim.getBodilyRates());
                Double pRate = claim.getPropertyDamageRates() == null ? null : rateDao.getRate(claim.getPropertyDamageRates());
                Double aRate = claim.getAccidentRates() == null ? null : rateDao.getRate(claim.getAccidentRates());

                if (vRate == null || bRate == null || pRate == null || aRate == null
                        || !Double.isFinite(vRate) || !Double.isFinite(bRate)
                        || !Double.isFinite(pRate) || !Double.isFinite(aRate)
                        || vRate < 0 || bRate < 0 || pRate < 0 || aRate < 0
                        || claim.getVehiclePrice() == null || claim.getVehiclePrice() < 0) {
                    request.setAttribute("fieldErrors", Map.of("carName", "この車両の補償条件を確認できません。管理者にマスタ登録を確認してください。"));
                    request.setAttribute("contract", contract);
                    request.setAttribute("claim", claim);
                    request.setAttribute("calculated", false);
                    request.setAttribute("vehicles", vehicles);
                    request.setAttribute("tempSaveList", new TempSaveDao(con).selectAll(loginUser.getUserId()));
                    con.rollback();
                    request.getRequestDispatcher("/WEB-INF/view/estimate/estimate.jsp").forward(request, response);
                    return;
                }

                // 5. 保険料のガチ計算
                InsuranceCalc calc = new InsuranceCalc();
                int totalPremium = calc.insurancecalc(contract, claim, vRate, bRate, pRate, aRate);

                // 6. 計算結果をメインの箱にセットして画面へ返す
                claim.setPremiumAmount(totalPremium);
                claim.setPremiumInstallment(totalPremium / contract.getInstallment());

                session.setAttribute("contract", contract);
                session.setAttribute("claim", claim);
                session.setAttribute("calculated", true);
                session.setAttribute("estimateSnapshot", InputChecks.estimateSnapshot(request));

                TempSaveDao tempSaveDao = new TempSaveDao(con);
                tempSaveDao.deleteExpired(
                        loginUser.getUserId(), LocalDateTime.now().minusMonths(1));
                request.setAttribute(
                        "tempSaveList", tempSaveDao.selectAll(loginUser.getUserId()));

                con.commit();

            } catch (SQLException | RuntimeException e) {
                try {
                    con.rollback();
                } catch (SQLException rollbackError) {
                    e.addSuppressed(rollbackError);
                }
                throw e;
            }

            request.setAttribute("vehicles", vehicleDao.findAll());
            request.getRequestDispatcher("/WEB-INF/view/estimate/estimate.jsp").forward(request, response);

        } catch (Exception e) {
            session.removeAttribute("calculated");
            session.removeAttribute("estimateSnapshot");
            e.printStackTrace();
            request.setAttribute("error", ErrorMsgConst.SYSTEM_ERROR);
            request.setAttribute("errorBackUrl", "/estimatecalc");
            request.setAttribute("errorBackLabel", "試算画面へ戻る");
            request.getRequestDispatcher("/WEB-INF/view/error/error.jsp").forward(request, response);
        }
    }

    private int parseInt(String value, int defaultValue) {
        if (value == null || value.trim().isEmpty()) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    private String selectNameParameter(
            HttpServletRequest request, String name, Integer insuredKbn) {
        String[] values = request.getParameterValues(name);
        if (values == null || values.length == 0) {
            return null;
        }
        if (Integer.valueOf(2).equals(insuredKbn) && values.length > 1) {
            return values[values.length - 1];
        }
        return values[0];
    }
}