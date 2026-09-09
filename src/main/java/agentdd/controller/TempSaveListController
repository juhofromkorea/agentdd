package agentdd.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import agentdd.model.constant.ErrorMsgConst;
import agentdd.model.dao.ConnectionManager;
import agentdd.model.dao.TempSaveDao;
import agentdd.model.data.TempSave;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/tempSaveList")
public class TempSaveListController extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(
            HttpServletRequest req,
            HttpServletResponse resp)
            throws ServletException, IOException {

        // 1. セッションからログインユーザーIDを取得
        HttpSession session = req.getSession(false);

        String userId =
                (String) session.getAttribute("userId");

        try (Connection con =
                ConnectionManager.getConnection()) {

            // 2. ログインユーザーの一時保存情報を取得
            TempSaveDao tempSaveDao =
                    new TempSaveDao(con);

            List<TempSave> tempSaveList =
                    tempSaveDao.selectAll(userId);

            // 3. 一時保存情報一覧をリクエストスコープに設定
            req.setAttribute(
                    "tempSaveList",
                    tempSaveList);

            // 4. 新規試算画面へforward
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