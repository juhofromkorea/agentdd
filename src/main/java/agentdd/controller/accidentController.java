package agentdd.controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import agentdd.model.dao.AccidentDao;
import agentdd.model.dao.ContractDao;
import agentdd.model.data.Accident;
import agentdd.model.data.Contract;

@WebServlet("/accident/*")
public class AccidentController extends HttpServlet {

    private AccidentDao accidentDao;
    private ContractDao contractDao;

    @Override
    public void init() throws ServletException {
        try {
            accidentDao = new AccidentDao();
            contractDao = new ContractDao();
        } catch (Exception e) {
            throw new ServletException("DAOの初期化に失敗しました", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        handleRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        handleRequest(request, response);
    }

    private void handleRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        
        String pathInfo = request.getPathInfo();

        try {
            // 1. 初期表示
            if (pathInfo == null || pathInfo.equals("/")) {
                request.getRequestDispatcher("/WEB-INF/view/accident/accident.jsp")
                        .forward(request, response);
                return;
            }

            // 2. 事故受付開始画面からの遷移
            if ("/detail".equals(pathInfo)) {
                String polNo = request.getParameter("polNo");    
                String claimNo = request.getParameter("claimNo"); 
                
                boolean hasPolNo = polNo != null && !polNo.trim().isEmpty();
                boolean hasClaimNo = claimNo != null && !claimNo.trim().isEmpty();

                // 入力チェック（両方空）
                if (!hasPolNo && !hasClaimNo) {
                    request.setAttribute("errorMessage", "証券番号または事故受付番号を入力してください。");
                    request.getRequestDispatcher("/WEB-INF/view/accident/accident.jsp").forward(request, response);
                    return;
                }

                // 排他チェック（両方入力された場合はエラーとする）
                if (hasPolNo && hasClaimNo) {
                    request.setAttribute("errorMessage", "新規受付の場合は証券番号のみ、更新の場合は事故受付番号のみを入力してください。");
                    request.getRequestDispatcher("/WEB-INF/view/accident/accident.jsp").forward(request, response);
                    return;
                }

                // A: 事故受付番号が入力された場合 (既存データの更新・再開)
                if (hasClaimNo) {
                    Accident accidentData = accidentDao.getAccident(claimNo);
                    
                    if (accidentData == null) {
                        request.setAttribute("errorMessage", "該当する事故受付番号が見つかりませんでした。");
                        request.getRequestDispatcher("/WEB-INF/view/accident/accident.jsp").forward(request, response);
                        return;
                    }
                    request.setAttribute("accident", accidentData);
                } 
                // B: 証券番号が入力された場合 (新規受付)
                else if (hasPolNo) {
                    Contract contractData = contractDao.findContractByPolNo(polNo);
                    
                    if (contractData == null) {
                        request.setAttribute("errorMessage", "該当する証券番号（契約情報）が見つかりませんでした。");
                        request.getRequestDispatcher("/WEB-INF/view/accident/accident.jsp").forward(request, response);
                        return;
                    }
                    
                    // 新規事故受付番号の発行
                    String newClaimNo = accidentDao.generateNextClaimNo();
                    
                    // 新規受付用の事故データオブジェクトを作成してセット
                    Accident newAccident = new Accident();
                    newAccident.setAccidentNo(newClaimNo);
                    
                    request.setAttribute("accident", newAccident);
                    request.setAttribute("contract", contractData);
                }

                // 詳細画面へフォワード
                request.getRequestDispatcher("/WEB-INF/view/accident/accident-detail.jsp").forward(request, response);
                return;
            }

            response.sendError(HttpServletResponse.SC_NOT_FOUND);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "システムエラーが発生しました。管理者にお問い合わせください。");
            request.getRequestDispatcher("/WEB-INF/view/error.jsp").forward(request, response);
        }
    }
}