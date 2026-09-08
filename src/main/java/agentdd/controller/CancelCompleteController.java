package agentdd.controller;

import java.io.IOException;
import java.sql.SQLException;

import agentdd.model.constant.ErrorMsgConst;
import agentdd.model.constant.SystemConst;
import agentdd.model.data.Contract;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/cancelcomplete")
public class CancelCompleteController extends HttpServlet {
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding(SystemConst.CHAR_SET);

        // セッションスコープから証券番号を取得する
        HttpSession session = request.getSession();
        String polNo = (String) session.getAttribute("polNo");

        // コントラクトオブジェクトの生成
        Contract contract = new Contract();

        // DAOを生成
        ContractDao contractDao = new ContractDao();

        try {

            // 証券番号に紐づいた状態フラグを変更する
            contractDao.setcancel();

            // 被保険者区分によってJSPを出し分け
            if (Integer.valueOf(2).equals(contract.getInsuredKbn())) {

                // 法人
                request.getRequestDispatcher(
                        "/WEB-INF/view/cancellation-complete-corporate.jsp").forward(request, response);

            } else {

                // 個人
                request.getRequestDispatcher(
                        "/WEB-INF/view/cancellation-complete.jsp").forward(request, response);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("errMsg", ErrorMsgConst.UNEXPECTED_ERROR);
            request.getRequestDispatcher(
                    "/WEB-INF/view/Error.jsp").forward(request, response);

        }

    }
}
