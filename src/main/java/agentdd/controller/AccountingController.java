package agentdd.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import agentdd.model.constant.ErrorMsgConst;
import agentdd.model.constant.SystemConst;
import agentdd.model.dao.ContractDao;
import agentdd.model.dao.ClaimDao;
import agentdd.model.data.Claim;
import agentdd.model.data.Contract;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.RequestDispatcher;

@WebServlet("/account")
public class AccountingController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        RequestDispatcher rd = request.getRequestDispatcher(
                "/WEB-INF/view/accounting/accounting.jsp");
        rd.forward(request, response);

    }

    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding(SystemConst.CHAR_SET);

        // ① 計上画面から印刷連番を取得
        String insatsuRenban = request.getParameter("insatsuRenban");

        // セッションスコープに印刷連番を保存
        HttpSession session = request.getSession();
        session.setAttribute("insatsuRenban", insatsuRenban);

        try {
            ContractDao contractDao = new ContractDao();
            ClaimDao claimDao = new ClaimDao();
            // ③ 印刷連番に紐づく契約情報を取得し、状態フラグをセッションスコープに格納する
            Contract contract = contractDao.getContractForAccount(insatsuRenban);

            // ④ 契約情報が存在しない場合
            if (contract == null) {

                request.setAttribute(
                        "errorMessage",
                        "該当する契約情報がありません。");

                request.getRequestDispatcher(
                        "/WEB-INF/view/error/error.jsp").forward(request, response);

                return;
            }

            // 状態フラグをセッションスコープに保存
            session.setAttribute("status_Flg", contract.getStatusFlg());

            // ⑤ 印刷連番に紐づく補償情報を取得
            Claim claim = claimDao.getClaim(insatsuRenban);

            // ⑥ 補償情報が存在しない場合
            if (claim == null) {

                request.setAttribute(
                        "error",
                        "該当する補償情報がありません。");

                request.getRequestDispatcher(
                        "/WEB-INF/view/error/error.jsp").forward(request, response);

                return;
            }

            // ⑦ 契約情報・補償情報をリクエストスコープに格納
            request.setAttribute("contract", contract);
            request.setAttribute("claim", claim);

            // ⑧ 被保険者区分によってJSPを出し分け
            if (Integer.valueOf(2).equals(contract.getInsuredKbn())) {

                // 法人
                request.getRequestDispatcher(
                        "/WEB-INF/view/accounting/accounting-detail-corporate.jsp").forward(request, response);

            } else {

                // 個人
                request.getRequestDispatcher(
                        "/WEB-INF/view/accounting/accounting-detail.jsp").forward(request, response);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("errMsg", ErrorMsgConst.UNEXPECTED_ERROR);
            request.getRequestDispatcher(
                    "/WEB-INF/view/error/error.jsp").forward(request, response);

        }
    }
}
