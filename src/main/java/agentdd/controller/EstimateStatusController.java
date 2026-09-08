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
import agentdd.model.dao.RatesDao;
import agentdd.model.dao.VehicleDao;
import agentdd.model.util.InsuranceCalc;

@WebServlet("/estimatestatus")
public class EstimateStatusController extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession session = request.getSession();

        Contract contract = (Contract) session.getAttribute("contract");
        Claim claim = (Claim) session.getAttribute("claim");
        if (contract == null) {
            contract = new Contract();
        }
        if (claim == null) {
            claim = new Claim();
        }

        updateContractFromRequest(request, contract);
        updateClaimFromRequest(request, claim);
        calculateClaimIfReady(contract, claim);
        session.setAttribute("contract", contract);
        session.setAttribute("claim", claim);
        
        // 2. 確認画面と完了画面にデータを持ち越すため、セッションに保存する
        session.setAttribute("printContract", contract);
        session.setAttribute("printClaim", claim);
        
        // 3. 申込書印刷確認画面へフォワード
        request.getRequestDispatcher("/WEB-INF/view/estimate/application-print.jsp").forward(request, response);
    }

    private void updateContractFromRequest(HttpServletRequest request, Contract contract) {
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
        contract.setPaymentMethod(parseInt(request.getParameter("paymentMethod"), contract.getPaymentMethod()));
        contract.setInstallment(parseInt(request.getParameter("installment"), contract.getInstallment()));
        contract.setInsuredKbn(parseInt(request.getParameter("insuredKbn"), contract.getInsuredKbn()));
        contract.setGender(parseInt(request.getParameter("gender"), contract.getGender()));
        contract.setInceptionDate(normalize(request.getParameter("inceptionDate"), contract.getInceptionDate()));
        contract.setConclusionDate(normalize(request.getParameter("conclusionDate"), contract.getConclusionDate()));
        contract.setBirthday(normalize(request.getParameter("birthday"), contract.getBirthday()));
        contract.setPostcode(normalize(request.getParameter("postcode"), contract.getPostcode()));
        contract.setTelephoneNo(normalize(request.getParameter("telephoneNo"), contract.getTelephoneNo()));
        contract.setMobilephoneNo(normalize(request.getParameter("mobilephoneNo"), contract.getMobilephoneNo()));
        contract.setFaxNo(normalize(request.getParameter("faxNo"), contract.getFaxNo()));
    }

    private void updateClaimFromRequest(HttpServletRequest request, Claim claim) {
        String maker = request.getParameter("maker");
        String carName = request.getParameter("carName");
        if (maker != null) {
            claim.setMaker(maker);
        }
        if (carName != null) {
            claim.setCarName(carName);
        }
        claim.setLicenseNo(normalize(request.getParameter("licenseNo"), claim.getLicenseNo()));
        claim.setLicenseColor(parseInt(request.getParameter("licenseColor"), claim.getLicenseColor()));
        claim.setAgeLimit(parseInt(request.getParameter("ageLimit"), claim.getAgeLimit()));
    }

    private void calculateClaimIfReady(Contract contract, Claim claim) throws ServletException {
        if (isBlank(claim.getMaker()) || isBlank(claim.getCarName())) {
            return;
        }
        try {
            new VehicleDao().getVehicle(claim);
            RatesDao ratesDao = new RatesDao();
            double vehicleRate = ratesDao.getRate(claim.getVehicleRates());
            double bodilyRate = ratesDao.getRate(claim.getBodilyRates());
            double propertyDamageRate = ratesDao.getRate(claim.getPropertyDamageRates());
            double accidentRate = ratesDao.getRate(claim.getAccidentRates());
            int totalPremium = new InsuranceCalc().insurancecalc(
                    contract,
                    claim,
                    vehicleRate,
                    bodilyRate,
                    propertyDamageRate,
                    accidentRate);
            claim.setPremiumAmount(totalPremium);
            int installment = contract.getInstallment() > 0 ? contract.getInstallment() : 1;
            claim.setPremiumInstallment(totalPremium / installment);
        } catch (Exception e) {
            throw new ServletException("申込書印刷前の保険料計算に失敗しました。", e);
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    private String normalize(String value, String currentValue) {
        if (value == null) {
            return currentValue;
        }
        return value.replace("/", "").replace("-", "");
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
