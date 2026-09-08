package agentdd.controller;

import java.io.IOException;
import java.util.Map;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * バックエンド実装前にJSPモック画面を確認するための一時的なコントローラー。
 * 各画面の正式なControllerへの接続が完了した後に削除する。
 */
@WebServlet("/mockup/*")
public class MockupPreviewController extends HttpServlet {

    private static final Map<String, String> VIEWS = Map.ofEntries(
            Map.entry("login", "/WEB-INF/jsp/login/login.jsp"),
            Map.entry("top", "/WEB-INF/jsp/top/top.jsp"),
            Map.entry("estimate", "/WEB-INF/jsp/estimate/estimate.jsp"),
            Map.entry("application-print", "/WEB-INF/jsp/estimate/application-print.jsp"),
            Map.entry("application-print-complete", "/WEB-INF/jsp/estimate/application-print-complete.jsp"),
            Map.entry("accounting", "/WEB-INF/jsp/accounting/accounting.jsp"),
            Map.entry("accounting-detail", "/WEB-INF/jsp/accounting/accounting-detail.jsp"),
            Map.entry("accounting-detail-corporate", "/WEB-INF/jsp/accounting/accounting-detail-corporate.jsp"),
            Map.entry("accounting-complete", "/WEB-INF/jsp/accounting/accounting-complete.jsp"),
            Map.entry("accounting-complete-corporate", "/WEB-INF/jsp/accounting/accounting-complete-corporate.jsp"),
            Map.entry("inquiry", "/WEB-INF/jsp/inquiry/inquiry.jsp"),
            Map.entry("inquiry-detail", "/WEB-INF/jsp/inquiry/inquiry-detail.jsp"),
            Map.entry("inquiry-detail-corporate", "/WEB-INF/jsp/inquiry/inquiry-detail-corporate.jsp"),
            Map.entry("cancellation", "/WEB-INF/jsp/cancellation/cancellation.jsp"),
            Map.entry("cancellation-detail", "/WEB-INF/jsp/cancellation/cancellation-detail.jsp"),
            Map.entry("cancellation-detail-corporate", "/WEB-INF/jsp/cancellation/cancellation-detail-corporate.jsp"),
            Map.entry("cancellation-complete", "/WEB-INF/jsp/cancellation/cancellation-complete.jsp"),
            Map.entry("cancellation-complete-corporate", "/WEB-INF/jsp/cancellation/cancellation-complete-corporate.jsp"),
            //Map.entry("accident", "/WEB-INF/jsp/accident/accident.jsp"),
            //Map.entry("accident-detail", "/WEB-INF/jsp/accident/accident-detail.jsp"),
            //Map.entry("accident-detail-corporate", "/WEB-INF/jsp/accident/accident-detail-corporate.jsp"),
            //Map.entry("accident-complete", "/WEB-INF/jsp/accident/accident-complete.jsp"),
            //Map.entry("accident-complete-corporate", "/WEB-INF/jsp/accident/accident-complete-corporate.jsp"),
            //Map.entry("accident-update-complete", "/WEB-INF/jsp/accident/accident-update-complete.jsp"),
            Map.entry("accident-update-complete-corporate", "/WEB-INF/jsp/accident/accident-update-complete-corporate.jsp"),
            Map.entry("error-system", "/WEB-INF/jsp/error/error-system.jsp"),
            Map.entry("error-unexpected", "/WEB-INF/jsp/error/error-unexpected.jsp")
    );

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        showScreen(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        showScreen(request, response);
    }

    private void showScreen(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        String screen = pathInfo == null || pathInfo.equals("/")
                ? "login"
                : pathInfo.substring(1);
        String view = VIEWS.get(screen);

        if (view == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        request.getRequestDispatcher(view).forward(request, response);
    }
}
