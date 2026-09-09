package agentdd.controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 一時保存一覧はestimatecalcの初期表示時に読み込むため、
 * 既存の直接URLは同じ画面の一覧タブへリダイレクトします。
 */
@WebServlet("/tempSaveList")
public class TempSaveListController extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.sendRedirect(req.getContextPath() + "/estimatecalc?tab=saved");
    }
}