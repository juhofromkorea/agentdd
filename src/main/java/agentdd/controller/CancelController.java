package agentdd.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Connection;

import agentdd.model.constant.ErrorMsgConst;
import agentdd.model.constant.SystemConst;
import agentdd.model.data.Claim;
import agentdd.model.data.Contract;
import agentdd.model.dao.ClaimDao;
import agentdd.model.dao.ContractDao;
import agentdd.model.dao.ConnectionManager;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.RequestDispatcher;

@WebServlet("/cancel")
public class CancelController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        RequestDispatcher rd = request.getRequestDispatcher(
                "/WEB-INF/view/cancellation/cancellation.jsp");
        rd.forward(request, response);

    }

    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding(SystemConst.CHAR_SET);

        // ①‐1解約画面から証券番号を取得
        Contract contract = null;
        Claim claim = null;
        ContractDao contractDao = null;
        ClaimDao claimDao = null;
        String polNo = request.getParameter("polNo");
        // ①-2セッションスコープに証券番号を格納
        HttpSession session = request.getSession();
        session.setAttribute("polNo", polNo);

        try (Connection con = ConnectionManager.getConnection()) {
            con.setAutoCommit(false);
            try {
                // ② DAOを生成
                contractDao = new ContractDao(con);
                claimDao = new ClaimDao(con);
                // ③ 証券番号に紐づく契約情報を取得
                contract = contractDao.getContract(polNo);

                // ④ 契約情報が存在しない場合
                if (contract == null) {

                    request.setAttribute(
                            "errorMessage",
                            "該当する契約情報がありません。");

                    request.getRequestDispatcher(
                            "/WEB-INF/view/cancellation/cancellation.jsp").forward(request, response);

                    return;

                }

                // ⑤ 証券番号に紐づく補償情報を取得
                claim = claimDao.getClaim(polNo);

                // ⑥ 補償情報が存在しない場合
                if (claim == null) {

                    request.setAttribute(
                            "error",
                            "該当する補償情報がありません。");
                    request.getRequestDispatcher(
                            "/WEB-INF/view/cancellation/cancellation.jsp").forward(request, response);

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

            // ⑦ 契約情報・補償情報をリクエストスコープに格納
            request.setAttribute("contract", contract);
            request.setAttribute("claim", claim);

            // ⑧ 被保険者区分によってJSPを出し分け
            if (Integer.valueOf(2).equals(contract.getInsuredKbn())) {

                // 法人
                request.getRequestDispatcher(
                        "/WEB-INF/view/cancellation/cancellation-detail-corporate.jsp").forward(request, response);

            } else {

                // 個人
                request.getRequestDispatcher(
                        "/WEB-INF/view/cancellation/cancellation-detail.jsp").forward(request, response);
            }

        } catch (SQLException e) {
            getServletContext().log("DB更新に失敗しました。", e);
            request.setAttribute("error", ErrorMsgConst.UNEXPECTED_ERROR);
            request.getRequestDispatcher(
                    "/WEB-INF/view/error/error.jsp").forward(request, response);

        }
    }
}