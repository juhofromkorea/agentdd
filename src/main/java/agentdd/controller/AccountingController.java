package agentdd.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
 
import agentdd.model.constant.ErrorMsgConst;
import agentdd.model.constant.SystemConst;
import agentdd.model.dao.ContractDao;
import agentdd.model.data.Claim;
import agentdd.model.data.Contract;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet ("/account")
public class AccountingController extends HttpServlet{

    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding(SystemConst.CHAR_SET);

        // ① 計上画面から印刷連番を取得
        String insatsuRenban = request.getParameter("insatsuRenban");

        //セッションスコープに印刷連番を保存
        HttpSession session = request.getSession();
        session.setAttribute("insatsuRenban",insatsuRenban);
            
        // ② DAOを生成
        Connection con;
        ContractDao contractDao = new ContractDao(con);
        ClaimDao claimDao = new ClaimDao();

        try {

            // ③ 印刷連番に紐づく契約情報を取得し、状態フラグをセッションスコープに格納する
            Contract contract = contractDao.getContractForAccount(insatsuRenban);

        


            // ④ 契約情報が存在しない場合
            if (contract == null) {

                request.setAttribute(
                "errorMessage",
                "該当する契約情報がありません。"
                );

                request.getRequestDispatcher(
                    "/WEB-INF/view/accounting-detail.jsp"
                ).forward(request, response);

                return;
            }

            //状態フラグをセッションスコープに保存
            session.setAttribute("status_Flg", contract.getStatusFlg());

            // ⑤ 印刷連番に紐づく補償情報を取得
            Claim claim = claimDao.getClaim(insatsuRenban);
    

            // ⑥ 補償情報が存在しない場合
            if (claim == null) {

                request.setAttribute(
                "errorMessage",
                "該当する補償情報がありません。"
                );

                request.getRequestDispatcher(
                "/WEB-INF/view/accounting-detail.jsp"
                ).forward(request, response);

                return;
            }

            // ⑦ 契約情報・補償情報をリクエストスコープに格納
            request.setAttribute("contract", contract);
            request.setAttribute("claim", claim);

            // ⑧ 被保険者区分によってJSPを出し分け
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
            e.printStackTrace();
            request.setAttribute("errMsg", ErrorMsgConst.UNEXPECTED_ERROR);
            request.getRequestDispatcher(
                "/WEB-INF/view/Error.jsp"
                ).forward(request, response);

    
        }
    }
}
    
    

