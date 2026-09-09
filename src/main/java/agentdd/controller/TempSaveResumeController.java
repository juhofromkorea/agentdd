package agentdd.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;

import agentdd.model.constant.ErrorMsgConst;
import agentdd.model.dao.ConnectionManager;
import agentdd.model.dao.TempSaveDao;
import agentdd.model.data.LoginUser;
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
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);

        if (session == null || !(session.getAttribute("loginUser") instanceof LoginUser loginUser)
                || loginUser.getUserId() == null || loginUser.getUserId().isBlank()) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String tempSaveId = req.getParameter("tempSaveId");
        if (tempSaveId == null || tempSaveId.isBlank()) {
            resp.sendRedirect(req.getContextPath()
                    + "/estimatecalc?tab=saved&result=missing");
            return;
        }

        String userId = loginUser.getUserId();
        TempSave tempSave;

        try (Connection con = ConnectionManager.getConnection()) {
            con.setAutoCommit(false);
            try {
                TempSaveDao tempSaveDao = new TempSaveDao(con);
                tempSaveDao.deleteExpired(userId, LocalDateTime.now().minusMonths(1));
                tempSave = tempSaveDao.select(tempSaveId, userId);
                if (tempSave == null) {
                    // 期限切れの掃除だけは確定してから一覧へ戻す。
                    con.commit();
                    resp.sendRedirect(req.getContextPath()
                            + "/estimatecalc?tab=saved&result=missing");
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
        } catch (SQLException e) {
            getServletContext().log("一時保存情報の再開に失敗しました。", e);
            req.setAttribute("error", ErrorMsgConst.SYSTEM_ERROR);
            req.setAttribute("errorBackUrl", "/estimatecalc?tab=saved");
            req.setAttribute("errorBackLabel", "一時保存一覧へ戻る");
            req.getRequestDispatcher("/WEB-INF/view/error/error.jsp")
                    .forward(req, resp);
            return;
        }

        // JSPはリクエストスコープだけでなく、試算・申込書印刷でも同じ値を使う。
        session.setAttribute("contract", tempSave.getContract());
        session.setAttribute("claim", tempSave.getClaim());
        session.setAttribute("calculated", Boolean.FALSE);
        session.removeAttribute("printContract");
        session.removeAttribute("printClaim");

        // GET側で車両マスタと一覧を再取得してから、契約条件タブを表示する。
        resp.sendRedirect(req.getContextPath() + "/estimatecalc?resumed=1");
    }
}