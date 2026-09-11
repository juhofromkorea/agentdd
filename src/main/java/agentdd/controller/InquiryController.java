package agentdd.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Connection;

import agentdd.model.dao.ConnectionManager;
import agentdd.model.constant.ErrorMsgConst;
import agentdd.model.constant.SystemConst;
import agentdd.model.data.Claim;
import agentdd.model.data.Contract;
import agentdd.model.dao.ClaimDao;
import agentdd.model.dao.ContractDao;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.RequestDispatcher;

@WebServlet("/inquiry")

public class InquiryController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        RequestDispatcher rd = request.getRequestDispatcher(
                "/WEB-INF/view/inquiry/inquiry.jsp");
        rd.forward(request, response);

    }

    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding(SystemConst.CHAR_SET);

        // 照会検索画面から証券番号を取得
        String polNo = request.getParameter("polNo");
        polNo = polNo == null ? "" : polNo.trim();
        if (!polNo.matches("B[0-9]{9}")) {
            request.setAttribute("fieldErrors", java.util.Map.of("polNo", "証券番号はBと半角数字9桁で入力してください。"));
            request.getRequestDispatcher("/WEB-INF/view/inquiry/inquiry.jsp").forward(request, response);
            return;
        }

        Contract contract = null;
        Claim claim = null;
        ContractDao contractDao = null;
        ClaimDao claimDao = null;

        try (Connection con = ConnectionManager.getConnection()) {
            con.setAutoCommit(false);

            try {
                contractDao = new ContractDao(con);
                claimDao = new ClaimDao(con);

                // 契約情報を取得
                contract = contractDao.getContract(polNo);

                // 契約情報が存在しない場合
                if (contract == null) {

                    request.setAttribute("fieldErrors", java.util.Map.of("polNo", "該当する契約情報がありません。"));
                    con.rollback();

                    request.getRequestDispatcher(
                            "/WEB-INF/view/inquiry/inquiry.jsp").forward(request, response);

                    return;
                }

                // Claim情報を取得
                claim = claimDao.getClaim(polNo);

                // 補償情報が存在しない場合
                if (claim == null) {

                    request.setAttribute("fieldErrors", java.util.Map.of("polNo", "該当する補償情報がありません。"));
                    con.rollback();

                    request.getRequestDispatcher(
                            "/WEB-INF/view/inquiry/inquiry.jsp").forward(request, response);

                    return;
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

            // JSPへ渡す
            request.setAttribute("contract", contract);
            request.setAttribute("claim", claim);

            // 被保険者区分によってJSPを出し分け
            if (Integer.valueOf(2).equals(contract.getInsuredKbn())) {

                // 法人
                request.getRequestDispatcher(
                        "/WEB-INF/view/inquiry/inquiry-detail-corporate.jsp").forward(request, response);

            } else {

                // 個人
                request.getRequestDispatcher(
                        "/WEB-INF/view/inquiry/inquiry-detail.jsp").forward(request, response);
            }

        } catch (SQLException e) {
            getServletContext().log("DB更新に失敗しました。", e);
            request.setAttribute("error", ErrorMsgConst.UNEXPECTED_ERROR);
            request.getRequestDispatcher(
                    "/WEB-INF/view/error/error.jsp").forward(request, response);
        } finally {
            con.close();
        }
    }
}
