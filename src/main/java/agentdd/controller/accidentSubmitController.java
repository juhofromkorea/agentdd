package agentdd.controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/accident/submit/*")
public class accidentSubmitController extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		showResultScreen(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		showResultScreen(request, response);
	}

	private void showResultScreen(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String pathInfo = request.getPathInfo();
		String view = "/complete".equals(pathInfo)
				? "/WEB-INF/jsp/accident/accident-complete.jsp"
				: "/WEB-INF/jsp/accident/accident-update-complete.jsp";

		request.getRequestDispatcher(view)
				.forward(request, response);
	}
}
