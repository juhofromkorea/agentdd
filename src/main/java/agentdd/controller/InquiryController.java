package agentdd.controller;

import java.io.IOException;
import java.sql.SQLException;

import agentdd.model.constant.SystemConst;
import agentdd.model.data.Claim;
import agentdd.model.data.Contract;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class InquiryController {
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding(SystemConst.CHAR_SET);

        // 照会検索画面から証券番号を取得
        String polNo = request.getParameter("polNo");

        ContractDao contractDao = new ContractDao();
        ClaimDao claimDao = new ClaimDao();

        try {

            // 契約情報を取得
            Contract contract =
                contractDao.getContractForInquiry(polNo);

            // Claim情報を取得
            Claim claim =
                claimDao.getClaim(polNo);
                 // JSPへ渡す
            request.setAttribute("contract", contract);
            request.setAttribute("claim", claim);

            // 被保険者区分によってJSPを出し分け
            if (Integer.valueOf(2).equals(contract.getInsuredKbn())) {

                // 法人
                request.getRequestDispatcher(
                    "/WEB-INF/view/inquiry-detail-corporate.jsp"
                ).forward(request, response);

            } else {

                // 個人
                request.getRequestDispatcher(
                    "/WEB-INF/view/inquiry-detail.jsp"
                ).forward(request, response);
            }

        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }
}