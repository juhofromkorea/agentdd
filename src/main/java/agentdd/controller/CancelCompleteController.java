package agentdd.controller;

import java.io.IOException;
import java.sql.SQLException;

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
        String polNo = (String) session.getAttribute("polNo");

        try {
            // DAOを生成
            ContractDao dao = new ContractDao();
            // コントラクトオブジェクトの生成
            Contract contract = dao.getContract(polNo);
            if (contract == null) {
                request.setAttribute("error", ErrorMsgConst.UNEXPECTED_ERROR);
                request.getRequestDispatcher(
                        "/WEB-INF/view/error/error.jsp").forward(request, response);
            }

            // 証券番号に紐づいた状態フラグを変更する
            if (contract.getStatusFlg() == 0 && contract.isCancelFlg() == false) {
                dao.requestCancel(polNo);
            }

            Contract updatedContract = dao.getContract(polNo);
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
            e.printStackTrace();
            request.setAttribute("error", ErrorMsgConst.UNEXPECTED_ERROR);
            request.getRequestDispatcher(
                    "/WEB-INF/view/error/error.jsp").forward(request, response);

        }

    }
}
