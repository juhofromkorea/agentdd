package agentdd.controller;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import agentdd.model.data.Contract;
import agentdd.model.data.Claim;
import agentdd.model.util.InsuranceCalc;
import agentdd.model.dao.VehicleDao;
import agentdd.model.dao.RatesDao;

@WebServlet("/estimatecalc")
public class EstimateCalcController extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        
        Contract contract = new Contract();
        Claim claim = new Claim();

        // 1. そのまま文字列として受け取る項目

        contract.setPolNo(request.getParameter("polNo"));
        contract.setNameKana1(request.getParameter("nameKana1"));
        contract.setNameKana2(request.getParameter("nameKana2"));
        contract.setNameKanji1(request.getParameter("nameKanji1"));
        contract.setNameKanji2(request.getParameter("nameKanji2"));
        contract.setAddressKana1(request.getParameter("addressKana1"));
        contract.setAddressKana2(request.getParameter("addressKana2"));
        contract.setAddressKanji1(request.getParameter("addressKanji1"));
        contract.setAddressKanji2(request.getParameter("addressKanji2"));
        contract.setInceptionTime(request.getParameter("inceptionTime"));
        contract.setConclusionTime(request.getParameter("conclusionTime"));

        contract.setStatusFlg(parseInt(request.getParameter("statusFlg"), 0));
        contract.setPaymentMethod(parseInt(request.getParameter("paymentMethod"), 0));
        contract.setInstallment(parseInt(request.getParameter("installment"), 1));
        contract.setInsuredKbn(parseInt(request.getParameter("insuredKbn"), 0));
        contract.setGender(parseInt(request.getParameter("gender"), 0));

        // 3. 記号（/ や -）を消し去ってからセットする項目（日付・郵便番号・電話番号）
        String rawInception = request.getParameter("inceptionDate");
        if (rawInception != null) contract.setInceptionDate(rawInception.replace("/", "").replace("-", ""));

        String rawConclusion = request.getParameter("conclusionDate");
        if (rawConclusion != null) contract.setConclusionDate(rawConclusion.replace("/", "").replace("-", ""));

        String rawBirthday = request.getParameter("birthday");
        if (rawBirthday != null) contract.setBirthday(rawBirthday.replace("/", "").replace("-", ""));

        String rawPost = request.getParameter("postcode");
        if (rawPost != null) contract.setPostcode(rawPost.replace("-", ""));

        String rawTel = request.getParameter("telephoneNo");
        if (rawTel != null) contract.setTelephoneNo(rawTel.replace("-", ""));

        String rawMobile = request.getParameter("mobilephoneNo");
        if (rawMobile != null) contract.setMobilephoneNo(rawMobile.replace("-", ""));

        String rawFax = request.getParameter("faxNo");
        if (rawFax != null) contract.setFaxNo(rawFax.replace("-", ""));


        claim.setMaker(request.getParameter("maker"));
        claim.setCarName(request.getParameter("carName")); 
        claim.setLicenseNo(request.getParameter("licenseNo"));
        claim.setLicenseColor(parseInt(request.getParameter("licenseColor"), 0)); // parseIntを使う！
        claim.setAgeLimit(parseInt(request.getParameter("ageLimit"), 0));

        try {
            // 2. マスタ情報の取得 (メーカーと車名をキーにする)
            VehicleDao vehicleDao = new VehicleDao();
            vehicleDao.getVehicle(claim);
    
            RatesDao rateDao = new RatesDao();
    
            // 3. マスタの料率IDを RatesDao で実際の数字に変換
            double vRate = rateDao.getRate((int) claim.getVehicleRates());
            double bRate = rateDao.getRate((int) claim.getBodilyRates());
            double pRate = rateDao.getRate((int) claim.getPropertyDamageRates());
            double aRate = rateDao.getRate((int) claim.getAccidentRates());

            // 4. 情報の統合（変換した数字をメインの claim にセット）
            // claim.setVehiclePrice(vehicleInfo.getVehiclePrice());
            // ※ Claim クラスに係数を保持するフィールドがあれば以下もセット
            // claim.setLicenseColorRate(licenseColorRate);
            // claim.setAgeLimitRate(ageLimitRate);

            // 5. 保険料のガチ計算
            InsuranceCalc calc = new InsuranceCalc();
            int totalPremium = calc.insurancecalc(contract, claim, vRate, bRate, pRate, aRate);

            // 6. 計算結果をメインの箱にセットして画面へ返す
            claim.setPremiumAmount(totalPremium);
            claim.setPremiumInstallment(totalPremium/contract.getInstallment());

            HttpSession session = request.getSession();
            session.setAttribute("contract", contract);
            session.setAttribute("claim", claim);
            session.setAttribute("calculated", true);
            
            request.getRequestDispatcher("/WEB-INF/view/estimate/estimate.jsp").forward(request, response);
    
        } catch (Exception e) {
            e.printStackTrace();
            request.getRequestDispatcher("/WEB-INF/view/error/Error.jsp").forward(request, response);
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
}