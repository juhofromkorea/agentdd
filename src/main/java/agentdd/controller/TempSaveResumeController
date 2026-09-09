package agentdd.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import agentdd.model.constant.ErrorMsgConst;
import agentdd.model.dao.ConnectionManager;
import agentdd.model.dao.TempSaveDao;
import agentdd.model.data.Claim;
import agentdd.model.data.Contract;
import agentdd.model.data.TempSave;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/tempSaveResume")
public class TempSaveResumeController extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(
            HttpServletRequest req,
            HttpServletResponse resp)
            throws ServletException, IOException {

        // 1. リクエストパラメータから一時保存番号を取得
        String tempSaveId = req.getParameter("tempSaveId");

        // 2. セッションからログインユーザーIDを取得
        HttpSession session = req.getSession(false);
        String userId = (String) session.getAttribute("userId");
         

        try (Connection con = ConnectionManager.getConnection()) {

            TempSaveDao tempSaveDao = new TempSaveDao(con);

            // 3. 一時保存情報を取得
            TempSave tempSave =
                    tempSaveDao.select(tempSaveId, userId);

            if (tempSave == null) {
                throw new ServletException(
                        "一時保存情報を取得できません。");
            }

            // 一時保存情報から契約情報・補償情報を取得
            Contract contract = tempSave.getContract();
            Claim claim = tempSave.getClaim();

            // 4. リクエストスコープに設定
            req.setAttribute("contract", contract);
            req.setAttribute("claim", claim);

            // 5. 新規a試算画面JSPへforward
            req.getRequestDispatcher(
                    "/WEB-INF/view/Estimate.jsp")
                    .forward(req, resp);

        } catch (SQLException e) {
            e.printStackTrace();
            req.setAttribute("error", ErrorMsgConst.SYSTEM_ERROR);
            req.getRequestDispatcher(
                    "/WEB-INF/view/Error.jsp");
                    .forward(req, resp);
                }
    }
}