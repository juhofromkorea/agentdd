package agentdd.controller;

import java.io.IOException;

import agentdd.model.constant.SystemConst;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/tempSave")
public class TempSaveController extends HttpServlet {
    private static final long serialVersionUID = 1L;

@Override
protected void doPost(
    HttpServletRequest req, 
    HttpServletResponse resp)
    throws ServletException, IOException{

    request.setCharactrEncding(SystemConst.CHAR_SET);
    response.getWriter().write("success");

}
}
