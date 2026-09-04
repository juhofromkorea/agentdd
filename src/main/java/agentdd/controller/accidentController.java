package agentdd.controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/accident/*")
public class accidentController extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String pathInfo = request.getPathInfo();

		if (pathInfo == null || pathInfo.equals("/")) {
			request.getRequestDispatcher("/WEB-INF/jsp/accident/accident.jsp")
					.forward(request, response);
			return;
		}

		if (pathInfo.equals("/detail")) {
			request.getRequestDispatcher("/WEB-INF/jsp/accident/accident-detail.jsp")
					.forward(request, response);
			return;
		}

		response.sendError(HttpServletResponse.SC_NOT_FOUND);
	}
}
