package agentdd.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;

import agentdd.model.constant.ErrorMsgConst;
import agentdd.model.dao.ConnectionManager;
import agentdd.model.dao.TempSaveDao;
import agentdd.model.data.LoginUser;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/tempSaveDelete")
public class TempSaveDeleteController extends HttpServlet {

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
        int deleted;

        try (Connection con = ConnectionManager.getConnection()) {
            con.setAutoCommit(false);
            try {
                TempSaveDao tempSaveDao = new TempSaveDao(con);
                tempSaveDao.deleteExpired(userId, LocalDateTime.now().minusMonths(1));
                deleted = tempSaveDao.delete(tempSaveId, userId);
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
            getServletContext().log("一時保存情報の削除に失敗しました。", e);
            req.setAttribute("error", ErrorMsgConst.SYSTEM_ERROR);
            req.setAttribute("errorBackUrl", "/estimatecalc?tab=saved");
            req.setAttribute("errorBackLabel", "一時保存一覧へ戻る");
            req.getRequestDispatcher("/WEB-INF/view/error/error.jsp")
                    .forward(req, resp);
            return;
        } finally {
            con.close();
        }

        String result = deleted == 1 ? "deleted" : "missing";
        resp.sendRedirect(req.getContextPath()
                + "/estimatecalc?tab=saved&result=" + result);
    }
}