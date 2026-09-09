package agentdd.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import agentdd.model.constant.ErrorMsgConst;
import agentdd.model.dao.ConnectionManager;
import agentdd.model.dao.ContractDao;
import agentdd.model.data.Contract;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/accountComplete")
public class AccountingSubmitController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // doPost(request, response);

    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 文字コード設定
        request.setCharacterEncoding("UTF-8");

        // 前画面から格納された印刷連番をセッションスコープから取得する
        HttpSession session = request.getSession(false);
        
        // セッション期限切れチェック
        if (session == null) {
            request.setAttribute("error", "セッションの有効期限が切れました。最初からやり直してください。");
            request.getRequestDispatcher("/WEB-INF/view/error/error.jsp")
                .forward(request, response);
            return;
        }

        try {
            Contract contract = (Contract) session.getAttribute("contract");
            String insatsuRenban = contract.getInsatsuRenban();
            Integer statusFlg = contract.getStatusFlg();
            ContractDao contractDao = new ContractDao();

            // 状態フラグと証券番号を更新する(新規)
            if (statusFlg != null && statusFlg == 1) {
                contractDao.updateKeijoStatus(insatsuRenban);
                
            } else if (statusFlg != null && statusFlg == 9) {

                // 状態フラグと解約フラグを更新する(このメソッドは後で追加します)
                contractDao.setCancel(i);
            }

            Contract updateContract = contractDao.getContractForAccount(insatsuRenban);
            request.setAttribute("contract", updateContract);
            request.getRequestDispatcher("/WEB-INF/view/accounting/accounting-complete.jsp")
                    .forward(request, response);

        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", ErrorMsgConst.UNEXPECTED_ERROR);
            request.getRequestDispatcher("/WEB-INF/view/error/error.jsp")
                .forward(request, response);
        }

    }

}