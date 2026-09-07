package agentdd.controller;

import java.io.IOException;
import java.util.Map;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import agentdd.model.dao.AccidentStub;

@WebServlet("/accident/*")
public class AccidentController extends HttpServlet {

    private final AccidentStub accidentStub = new AccidentStub();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        handleRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // POST送信（検索など）が来た場合も受け取れるようにdoGetと同等の処理を実行
        handleRequest(request, response);
    }

    private void handleRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String pathInfo = request.getPathInfo();

        // 1. 初期表示（/ へのアクセス）
        if (pathInfo == null || pathInfo.equals("/")) {
            request.getRequestDispatcher("/WEB-INF/jsp/accident/accident.jsp")
                    .forward(request, response);
            return;
        }

        // 2. 事故受付開始画面からの検索・遷移（/detail へのアクセス）
        if (pathInfo.equals("/detail")) {
            String polNo = request.getParameter("polNo");     // 証券番号
            String claimNo = request.getParameter("claimNo"); // 事故受付番号

            // 入力チェック（両方空の場合は元の画面へ）
            if ((polNo == null || polNo.trim().isEmpty()) && (claimNo == null || claimNo.trim().isEmpty())) {
                request.setAttribute("errorMessage", "証券番号または事故受付番号を入力してください。");
                request.getRequestDispatcher("/WEB-INF/jsp/accident/accident.jsp")
                        .forward(request, response);
                return;
            }

            // スタブからダミーデータを取得して入力画面へ遷移
            Map<String, Object> accidentData = accidentStub.findAccidentDetail(polNo, claimNo);
            request.setAttribute("accident", accidentData);
            request.getRequestDispatcher("/WEB-INF/jsp/accident/accident-detail.jsp")
                    .forward(request, response);
            return;
        }

        response.sendError(HttpServletResponse.SC_NOT_FOUND);
    }
}