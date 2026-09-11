package agentdd.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Map;

import agentdd.model.constant.ErrorMsgConst;
import agentdd.model.constant.SystemConst;
import agentdd.model.dao.ConnectionManager;
import agentdd.model.dao.LoginDao;
import agentdd.model.data.LoginUser;
import agentdd.model.datacheck.InputChecks;
import agentdd.model.exception.BusinessException;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/login") 
public class LoginController extends HttpServlet {

    private static final long serialVersionUID = 1L;
    
    public LoginController() {
        super();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
        throws ServletException, IOException {
            
        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/view/login/login.jsp");
        rd.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
        throws ServletException, IOException {

        request.setCharacterEncoding(SystemConst.CHAR_SET);

        String userId = request.getParameter("userId");
        String password = request.getParameter("password");

        Map<String, String> fieldErrors = InputChecks.login(userId, password);

        if (!fieldErrors.isEmpty()) {
            request.setAttribute("userId", userId);
            request.setAttribute("fieldErrors", fieldErrors);
            request.getRequestDispatcher(
                    "/WEB-INF/view/login/login.jsp"
            ).forward(request, response);
            return;
        }

        LoginUser loginUser = new LoginUser();
        loginUser.setUserId(userId);
        loginUser.setPassword(password);

        try (Connection con = ConnectionManager.getConnection()) {
            con.setAutoCommit(false);

            try {
                LoginDao loginDao = new LoginDao(con);
                LoginUser dbUser = loginDao.findByUserId(userId);

                if (dbUser == null) {
                    con.rollback();
                    throw new BusinessException(ErrorMsgConst.LOGIN_ERROR);
                }

                if (dbUser.getLockFlag() == 1) {
                    con.rollback();
                    throw new BusinessException(ErrorMsgConst.ACCOUNT_LOCKED);
                }

                if (!Objects.equals(dbUser.getPassword(), password)) {

                    int newLoginCount = Math.min(dbUser.getLoginCount() + 1, 5);

                    loginDao.increaseLoginCount(userId);
                    con.commit();

                    if (newLoginCount >= 5) {
                        throw new BusinessException(ErrorMsgConst.ACCOUNT_LOCKED);
                    }

                    throw new BusinessException(ErrorMsgConst.LOGIN_ERROR);
                }

                loginDao.resetLoginCount(userId);
                con.commit();

                dbUser.setPassword(null);
                dbUser.setLoginCount(0);

                HttpSession oldSession = request.getSession(false);

                if (oldSession != null) {
                    oldSession.invalidate();
                }

                HttpSession newSession = request.getSession(true);

                newSession.setAttribute("loginUser", dbUser);

                response.sendRedirect(request.getContextPath() + "/top");
                return;

            } catch (SQLException e) {
                con.rollback();
                throw e;
            }
        } catch (BusinessException e) {
            request.setAttribute("userId", userId);
            request.setAttribute("error", e.getMessage());

            RequestDispatcher rd = request.getRequestDispatcher(
                    "/WEB-INF/view/login/login.jsp");
            rd.forward(request, response);

        } catch (SQLException e) {
            e.printStackTrace();

            request.setAttribute(
                    "error", ErrorMsgConst.SYSTEM_ERROR);

            RequestDispatcher rd = request.getRequestDispatcher(
                    "/WEB-INF/view/error/error.jsp");
            rd.forward(request, response);
        } finally {
            con.close();
        }
    }
}
