package agentdd.controller;

import java.io.IOException;
import java.sql.SQLException;

import agentdd.model.constant.SystemConst;
import agentdd.model.data.Claim;
import agentdd.model.data.Contract;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class cancel {

    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding(SystemConst.CHAR_SET);

        // ① 照会検索画面から証券番号を取得
        String polNo = request.getParameter("polNo");

        // ② DAOを生成
        ContractDao contractDao = new ContractDao();
        ClaimDao claimDao = new claimDao();

        try {

            // ③ 証券番号に紐づく契約情報を取得
            Contract contract =
                contractDao.getContract(polNo);

            // ④ 契約情報が存在しない場合
            if (contract == null) {

                request.setAttribute(
                    "errorMessage",
                    "該当する契約情報がありません。"
                );
                
            }

            try {

            // ③ 証券番号に紐づく契約情報を取得
            Claim claim =
                claimDao.getClaim(polNo);

            // ④ 契約情報が存在しない場合
            if (claim == null) {

                request.setAttribute(
                    "errorMessage",
                    "該当する契約情報がありません。"
                );
                
            }

            // ⑤ 契約情報をリクエストスコープに格納
            request.setAttribute("contract", contract);
            request.setAttribute("claim", claim);

            // ⑥ 被保険者区分によってJSPを出し分け
            if (Integer.valueOf(2).equals(contract.getInsuredKbn())) {

                // 法人
                request.getRequestDispatcher(
                    "/WEB-INF/view/cancellation-detail-corporate.jsp"
                ).forward(request, response);

            } else {

                // 個人
                request.getRequestDispatcher(
                    "/WEB-INF/view/cancellation-detail.jsp"
                ).forward(request, response);
            }

        } catch (SQLException e) {

            throw new ServletException(e);
        }
    }
}