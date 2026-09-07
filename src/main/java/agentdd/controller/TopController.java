package agentdd.controller;

import java.io.IOException;

import agentdd.model.constant.ErrorMsgConst;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/top")
public class TopController extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("loginUser") == null) {
            request.setAttribute("error", ErrorMsgConst.SESSION_ERROR);

            RequestDispatcher rd = request.getRequestDispatcher(
                    "/WEB-INF/view/error/error.jsp");
            rd.forward(request, response);
            return;
        }

        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);

        RequestDispatcher rd = request.getRequestDispatcher(
                "/WEB-INF/view/top/top.jsp");
        rd.forward(request, response);
    }
}
