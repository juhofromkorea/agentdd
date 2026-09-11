package agentdd.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import agentdd.model.dao.ConnectionManager;
import agentdd.model.constant.ErrorMsgConst;
import agentdd.model.constant.SystemConst;
import agentdd.model.data.Contract;
import agentdd.model.dao.ContractDao;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/cancel/complete")
public class CancelCompleteController extends HttpServlet {
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding(SystemConst.CHAR_SET);

        // セッションスコープから証券番号を取得する
        HttpSession session = request.getSession();
        Contract contract = null;
        ContractDao contractDao = null;
        String polNo = (String) session.getAttribute("polNo");

        try (Connection con = ConnectionManager.getConnection()) {
            con.setAutoCommit(false);
            try {
                // DAOを生成
                contractDao = new ContractDao(con);
                // コントラクトオブジェクトの生成
                contract = contractDao.getContract(polNo);
                if (contract == null) {
                    request.setAttribute("error", ErrorMsgConst.UNEXPECTED_ERROR);
                    request.getRequestDispatcher(
                            "/WEB-INF/view/error/error.jsp").forward(request, response);
                }

                // 証券番号に紐づいた状態フラグを変更する
                if (contract.getStatusFlg() == 0 && contract.isCancelFlg() == false) {
                    contractDao.requestCancel(polNo);
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

            Contract updatedContract = contractDao.getContract(polNo);
            request.setAttribute("contract", updatedContract);

            // 被保険者区分によってJSPを出し分け
            if (Integer.valueOf(2).equals(contract.getInsuredKbn())) {

                // 法人
                request.getRequestDispatcher(
                        "/WEB-INF/view/cancellation/cancellation-complete-corporate.jsp").forward(request, response);

            } else {

                // 個人
                request.getRequestDispatcher(
                        "/WEB-INF/view/cancellation/cancellation-complete.jsp").forward(request, response);
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
