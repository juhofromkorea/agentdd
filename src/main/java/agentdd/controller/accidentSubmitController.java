package agentdd.controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import agentdd.model.dao.AccidentStub;

@WebServlet("/accident/submit")
public class AccidentSubmitController extends HttpServlet {

    private final AccidentStub accidentStub = new AccidentStub();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        // ボタンの action パラメータ（"updateStatus" または "completeReceipt"）を取得
        String action = request.getParameter("action");

        if ("updateStatus".equals(action)) {
            // 状況更新処理
            accidentStub.updateAccident(request.getParameterMap());
            request.setAttribute("completeMessage", "事故状況を更新しました");

        } else if ("completeReceipt".equals(action)) {
            // 事故受付完了処理
            accidentStub.completeAccident(request.getParameterMap());
            request.setAttribute("completeMessage", "事故受付が完了しました");
        }

        // 保険金額（支払金額）の計算
        long paymentAmount = calculatePaymentAmount(request);

        // 完了画面表示用データのセット
        request.setAttribute("claimNo", request.getParameter("claimNo"));
        request.setAttribute("polNo", request.getParameter("polNo"));
        request.setAttribute("contractorName", request.getParameter("contractorName"));
        request.setAttribute("paymentAmount", paymentAmount);

        // 完了画面（accident-complete.jsp）へフォワード
        request.getRequestDispatcher("/WEB-INF/jsp/accident/accident-complete.jsp")
                .forward(request, response);
    }

    /**
     * 保険金（支払金額）算出処理
     * 計算式: 過失割合（被保険者） × (損害額・車両 + 損害額・対人 + 損害額・対物 + 損害額・傷害)
     */
    private long calculatePaymentAmount(HttpServletRequest request) {
        long vehicleDamage = parseLong(request.getParameter("vehicleDamageAmount"));
        long bodilyDamage = parseLong(request.getParameter("bodilyDamageAmount"));
        long propertyDamage = parseLong(request.getParameter("propertyDamageAmount"));
        long injuryDamage = parseLong(request.getParameter("injuryDamageAmount"));

        long totalDamage = vehicleDamage + bodilyDamage + propertyDamage + injuryDamage;
        double faultRatio = parseDouble(request.getParameter("insuredFaultRatio")) / 100.0;

        return Math.round(totalDamage * faultRatio);
    }

    private long parseLong(String val) {
        if (val == null || val.trim().isEmpty()) return 0L;
        try { return Long.parseLong(val.trim()); } catch (NumberFormatException e) { return 0L; }
    }

    private double parseDouble(String val) {
        if (val == null || val.trim().isEmpty()) return 0.0;
        try { return Double.parseDouble(val.trim()); } catch (NumberFormatException e) { return 0.0; }
    }
}