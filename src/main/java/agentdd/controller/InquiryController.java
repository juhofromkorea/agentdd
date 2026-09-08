package agentdd.controller;

import java.io.IOException;
import java.sql.SQLException;

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

@WebServlet("/inquiry")

public class InquiryController extends HttpServlet {

    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding(SystemConst.CHAR_SET);

        // 照会検索画面から証券番号を取得
        String polNo = request.getParameter("polNo");

        try {
            ContractDao contractDao = new ContractDao();
            ClaimDao claimDao = new ClaimDao();

            // 契約情報を取得
            Contract contract = contractDao.getContract(polNo);

            // 契約情報が存在しない場合
            if (contract == null) {

                request.setAttribute(
                        "errorMessage",
                        "該当する契約情報がありません。");

                request.getRequestDispatcher(
                        "/WEB-INF/view/inquiry.jsp").forward(request, response);

                return;
            }

            // Claim情報を取得
            Claim claim = claimDao.getClaim(polNo);

            // 補償情報が存在しない場合
            if (claim == null) {

                request.setAttribute(
                        "errorMessage",
                        "該当する補償情報がありません。");

                request.getRequestDispatcher(
                        "/WEB-INF/view/inquiry/inquiry.jsp").forward(request, response);

                return;
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
            request.setAttribute("error", ErrorMsgConst.UNEXPECTED_ERROR);
            request.getRequestDispatcher(
                    "/WEB-INF/view/error/error.jsp").forward(request, response);

        }
    }
}
