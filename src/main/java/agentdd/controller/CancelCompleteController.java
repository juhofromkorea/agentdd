package agentdd.controller;

import java.io.IOException;

import agentdd.model.constant.SystemConst;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/cancelcomplete")
public class CancelCompleteController extends HttpServlet{
     protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

                request.setCharacterEncoding(SystemConst.CHAR_SET); 
            }



    
}
