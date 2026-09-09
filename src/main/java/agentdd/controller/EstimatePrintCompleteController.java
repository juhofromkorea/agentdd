package agentdd.controller;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import agentdd.model.data.Contract;
import agentdd.model.data.Claim;
import agentdd.model.util.PrintSerialNumberCalc;
import agentdd.model.dao.ContractDao;
import agentdd.model.dao.ClaimDao;
import agentdd.model.dao.VehicleDao;

@WebServlet("/estimateprint")
public class EstimatePrintCompleteController extends HttpServlet{

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null) {
            response.sendRedirect(request.getContextPath() + "/top");
            return;
        }

        Contract printContract = (Contract) session.getAttribute("printContract");
        Claim printClaim = (Claim) session.getAttribute("printClaim");
        if (printContract != null) {
            session.setAttribute("contract", printContract);
        }
        if (printClaim != null) {
            session.setAttribute("claim", printClaim);
            session.setAttribute("calculated", printClaim.getPremiumAmount() > 0);
        }

        try {
            request.setAttribute("vehicles", new VehicleDao().findAll());
            request.getRequestDispatcher("/WEB-INF/view/estimate/estimate.jsp").forward(request, response);
        } catch (Exception e) {
            throw new ServletException("車両マスタ一覧の取得に失敗しました。", e);
        }
        
        }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 1. セッションから大事なデータを取り出す
        HttpSession session = request.getSession();
        Contract contract = (Contract) session.getAttribute("printContract");
        Claim claim = (Claim) session.getAttribute("printClaim");
        
        try {
            // 2. 印刷連番を新規発行して箱にセット
            PrintSerialNumberCalc serialCalc = new PrintSerialNumberCalc();
            String serialNum = serialCalc.numbercalc();
            contract.setInsatsuRenban(serialNum);
            claim.setInsatsuRenban(serialNum);
            
            // 3. DBへ確定情報を保存 (Dao)
            ContractDao contractDao = new ContractDao();
            ClaimDao claimDao = new ClaimDao();
            contractDao.setEstimate(contract);
            claimDao.setEstimate(claim);
            
            // 4. お片付け（セッションから不要になったデータを消す）
            session.removeAttribute("printContract");
            session.removeAttribute("printClaim");

            // ↓追加：新規試算画面の入力欄に紐づいているデータも完全に破棄する
            session.removeAttribute("contract");
            session.removeAttribute("claim");
            session.removeAttribute("calculated");

            // 5. 完了画面に発行した連番だけを渡して遷移
            request.setAttribute("serialNum", serialNum);
            request.setAttribute("contract", contract);
            request.getRequestDispatcher("/WEB-INF/view/estimate/application-print-complete.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.getRequestDispatcher("/WEB-INF/view/error/error.jsp").forward(request, response);
        }
    }
}
