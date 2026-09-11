package agentdd.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import agentdd.model.dao.ConnectionManager;
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
        insatsuRenban = insatsuRenban == null ? "" : insatsuRenban.trim();
        if (!insatsuRenban.matches("A[0-9]{7}")) {
            request.setAttribute("fieldErrors", java.util.Map.of("insatsuRenban", "印刷連番はAと半角数字7桁で入力してください。"));
            request.getRequestDispatcher("/WEB-INF/view/accounting/accounting.jsp").forward(request, response);
            return;
        }
        Contract contract = new Contract();
        Claim claim = new Claim();

        // セッションスコープに印刷連番を保存
        HttpSession session = request.getSession();
        session.setAttribute("insatsuRenban", insatsuRenban);

        try (Connection con = ConnectionManager.getConnection()) {
            con.setAutoCommit(false);

            try {
                ContractDao contractDao = new ContractDao(con);
                ClaimDao claimDao = new ClaimDao(con);
                // ③ 印刷連番に紐づく契約情報を取得し、状態フラグをセッションスコープに格納する
                contract = contractDao.getContractForAccount(insatsuRenban);

                // ④ 契約情報が存在しない場合
                if (contract == null) {

                    request.setAttribute("fieldErrors", java.util.Map.of("insatsuRenban", "該当する契約情報がありません。"));
                    con.rollback();

                    request.getRequestDispatcher(
                            "/WEB-INF/view/accounting/accounting.jsp").forward(request, response);

                    return;
                }

                if (!Integer.valueOf(1).equals(contract.getStatusFlg())
                        && !Integer.valueOf(9).equals(contract.getStatusFlg())) {
                    request.setAttribute("fieldErrors", java.util.Map.of("insatsuRenban", "計上可能な契約ではありません。計上済み・解約申請中でないか確認してください。"));
                    con.rollback();
                    request.getRequestDispatcher("/WEB-INF/view/accounting/accounting.jsp").forward(request, response);
                    return;
                }

                // 状態フラグをセッションスコープに保存
                session.setAttribute("status_Flg", contract.getStatusFlg());

                // ⑤ 印刷連番に紐づく補償情報を取得
                claim = claimDao.getClaimForAccount(insatsuRenban);

                // ⑥ 補償情報が存在しない場合
                if (claim == null) {

                    request.setAttribute("fieldErrors", java.util.Map.of("insatsuRenban", "該当する補償情報がありません。"));
                    con.rollback();

                    request.getRequestDispatcher(
                            "/WEB-INF/view/accounting/accounting.jsp").forward(request, response);

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
                        "/WEB-INF/view/accounting/accounting-detail-corporate.jsp").forward(request, response);

            } else {

                // 個人
                request.getRequestDispatcher(
                        "/WEB-INF/view/accounting/accounting-detail.jsp").forward(request, response);
            }

        } catch (SQLException e) {
            getServletContext().log("DB更新に失敗しました。", e);
            request.setAttribute("error", ErrorMsgConst.UNEXPECTED_ERROR);
            request.getRequestDispatcher(
                    "/WEB-INF/view/error/error.jsp").forward(request, response);
        }
    }
}
