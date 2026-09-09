package agentdd.controller;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.sql.Connection;
import java.sql.SQLException;

import agentdd.model.constant.ErrorMsgConst;
import agentdd.model.dao.ConnectionManager;
import agentdd.model.data.Contract;
import agentdd.model.data.Claim;
import agentdd.model.util.PrintSerialNumberCalc;
import agentdd.model.dao.ContractDao;
import agentdd.model.dao.ClaimDao;
import agentdd.model.dao.VehicleDao;

@WebServlet("/estimateprint")
public class EstimatePrintCompleteController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null) {
            response.sendRedirect(request.getContextPath() + "/top");
            return;
        }

        Contract printContract = (Contract) session.getAttribute("printContract");
        Claim printClaim = (Claim) session.getAttribute("printClaim");
        if (printContract != null) {
            session.setAttribute("contract", printContract);
        }
        if (printClaim != null) {
            session.setAttribute("claim", printClaim);
            session.setAttribute("calculated", printClaim.getPremiumAmount() > 0);
        }

        try (Connection con = ConnectionManager.getConnection()) {
            con.setAutoCommit(false);
            try {
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
            request.getRequestDispatcher("/WEB-INF/view/estimate/estimate.jsp").forward(request, response);
        } catch (SQLException e) {
            getServletContext().log("車両テーブル取得に失敗しました。", e);
            request.setAttribute("error", ErrorMsgConst.UNEXPECTED_ERROR);
            request.getRequestDispatcher("/WEB-INF/view/error/error.jsp")
                    .forward(request, response);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 1. セッションから大事なデータを取り出す
        HttpSession session = request.getSession();
        Contract contract = (Contract) session.getAttribute("printContract");
        Claim claim = (Claim) session.getAttribute("printClaim");
        String serialNum;

        try (Connection con = ConnectionManager.getConnection()) {
            con.setAutoCommit(false);

            try {
                PrintSerialNumberCalc serialCalc = new PrintSerialNumberCalc();
                serialNum = serialCalc.numbercalc(con);

                contract.setInsatsuRenban(serialNum);
                claim.setInsatsuRenban(serialNum);

                ContractDao contractDao = new ContractDao(con);
                ClaimDao claimDao = new ClaimDao(con);

                if (contractDao.setEstimate(contract) != 1
                        || claimDao.setEstimate(claim) != 1) {
                    throw new SQLException("契約・補償情報の登録に失敗しました。");
                }

                con.commit();
            } catch (SQLException | RuntimeException e) {
                try {
                    con.rollback();
                } catch (SQLException rollbackError) {
                    e.addSuppressed(rollbackError);
                }
                throw e;
            }

            // 4. お片付け（セッションから不要になったデータを消す）
            session.removeAttribute("printContract");
            session.removeAttribute("printClaim");

            // ↓追加：新規試算画面の入力欄に紐づいているデータも完全に破棄する
            session.removeAttribute("contract");
            session.removeAttribute("claim");
            session.removeAttribute("calculated");

            // 5. 完了画面に発行した連番だけを渡して遷移
            request.setAttribute("serialNum", serialNum);
            request.setAttribute("contract", contract);
            request.getRequestDispatcher("/WEB-INF/view/estimate/application-print-complete.jsp").forward(request,
                    response);

        } catch (Exception e) {
            getServletContext().log("DB更新に失敗しました。", e);
            request.getRequestDispatcher("/WEB-INF/view/error/error.jsp").forward(request, response);
        }
    }
}
