package agentdd.controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import agentdd.model.data.Claim;
import agentdd.model.data.Contract;
import agentdd.model.data.LoginUser;
import agentdd.model.datacheck.InputChecks;
@WebServlet("/estimatestatus")
public class EstimateStatusController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession(false);
        if (session == null || !(session.getAttribute("loginUser") instanceof LoginUser loginUser)
                || loginUser.getUserId() == null || loginUser.getUserId().isBlank()) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        Contract contract = (Contract) session.getAttribute("contract");
        Claim claim = (Claim) session.getAttribute("claim");

        boolean calculated = Boolean.TRUE.equals(session.getAttribute("calculated"))
                && contract != null && claim != null && session.getAttribute("estimateSnapshot") != null;
        boolean unchanged = calculated && InputChecks.estimateSnapshot(request).equals(session.getAttribute("estimateSnapshot"));
        
        if (!calculated || !unchanged) {
            request.setAttribute("estimatePrintError", calculated
                    ? "入力内容が変わりました。保険料を再試算してから印刷してください。"
                    : "先に保険料試算を実行してください。");
            // POST入力を維持して同じ入力画面へ。属性があるため試算は実行しない。
            request.getRequestDispatcher("/estimatecalc").forward(request, response);
            return;
        }

        session.setAttribute("printContract", contract);
        session.setAttribute("printClaim", claim);
        request.getRequestDispatcher("/WEB-INF/view/estimate/application-print.jsp").forward(request, response);
    }
}