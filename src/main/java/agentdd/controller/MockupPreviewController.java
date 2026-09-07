package agentdd.controller;

import java.io.IOException;
import java.util.Map;

import agentdd.model.constant.ErrorMsgConst;
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
            Map.entry("estimate", "/WEB-INF/view/estimate/estimate.jsp"),
            Map.entry("application-print", "/WEB-INF/view/estimate/application-print.jsp"),
            Map.entry("application-print-complete", "/WEB-INF/view/estimate/application-print-complete.jsp"),
            Map.entry("accounting", "/WEB-INF/view/accounting/accounting.jsp"),
            Map.entry("accounting-detail", "/WEB-INF/view/accounting/accounting-detail.jsp"),
            Map.entry("accounting-detail-corporate", "/WEB-INF/view/accounting/accounting-detail-corporate.jsp"),
            Map.entry("accounting-complete", "/WEB-INF/view/accounting/accounting-complete.jsp"),
            Map.entry("accounting-complete-corporate", "/WEB-INF/view/accounting/accounting-complete-corporate.jsp"),
            Map.entry("inquiry", "/WEB-INF/view/inquiry/inquiry.jsp"),
            Map.entry("inquiry-detail", "/WEB-INF/view/inquiry/inquiry-detail.jsp"),
            Map.entry("inquiry-detail-corporate", "/WEB-INF/view/inquiry/inquiry-detail-corporate.jsp"),
            Map.entry("cancellation", "/WEB-INF/view/cancellation/cancellation.jsp"),
            Map.entry("cancellation-detail", "/WEB-INF/view/cancellation/cancellation-detail.jsp"),
            Map.entry("cancellation-detail-corporate", "/WEB-INF/view/cancellation/cancellation-detail-corporate.jsp"),
            Map.entry("cancellation-complete", "/WEB-INF/view/cancellation/cancellation-complete.jsp"),
            Map.entry("cancellation-complete-corporate", "/WEB-INF/view/cancellation/cancellation-complete-corporate.jsp"),
            Map.entry("accident", "/WEB-INF/view/accident/accident.jsp"),
            Map.entry("accident-detail", "/WEB-INF/view/accident/accident-detail.jsp"),
            Map.entry("accident-detail-corporate", "/WEB-INF/view/accident/accident-detail-corporate.jsp"),
            Map.entry("accident-complete", "/WEB-INF/view/accident/accident-complete.jsp"),
            Map.entry("accident-complete-corporate", "/WEB-INF/view/accident/accident-complete-corporate.jsp"),
            Map.entry("accident-update-complete", "/WEB-INF/view/accident/accident-update-complete.jsp"),
            Map.entry("accident-update-complete-corporate", "/WEB-INF/view/accident/accident-update-complete-corporate.jsp"),
            Map.entry("error-system", "/WEB-INF/view/error/Error.jsp"),
            Map.entry("error-unexpected", "/WEB-INF/view/error/Error.jsp")
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

        if ("error-system".equals(screen)) {
            request.setAttribute("error", ErrorMsgConst.SYSTEM_ERROR);
            request.setAttribute("errorBackUrl", "/top");
            request.setAttribute("errorBackLabel", "TOPへ戻る");
        } else if ("error-unexpected".equals(screen)) {
            request.setAttribute("error", ErrorMsgConst.UNEXPECTED_ERROR);
            request.setAttribute("errorBackUrl", "/top");
            request.setAttribute("errorBackLabel", "TOPへ戻る");
        }

        request.getRequestDispatcher(view).forward(request, response);
    }
}
