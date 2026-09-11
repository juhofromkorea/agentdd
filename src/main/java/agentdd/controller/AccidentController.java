package agentdd.controller;

import java.io.IOException;
import java.sql.Connection;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import agentdd.model.constant.ErrorMsgConst;
import agentdd.model.dao.AccidentDao;
import agentdd.model.dao.ClaimDao;
import agentdd.model.dao.ConnectionManager;
import agentdd.model.dao.ContractDao;
import agentdd.model.data.Accident;
import agentdd.model.data.Claim;
import agentdd.model.data.Contract;

@WebServlet("/accident/*")
public class AccidentController extends HttpServlet {

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

            try (Connection con = ConnectionManager.getConnection()) {
                AccidentDao accidentDao = new AccidentDao(con);
                ContractDao contractDao = new ContractDao(con);
                ClaimDao claimDao = new ClaimDao(con);

                // 2. 事故受付開始画面からの遷移
                if ("/detail".equals(pathInfo)) {
                String polNo = request.getParameter("polNo");
                polNo = polNo == null ? "" : polNo.trim();
                String claimNo = request.getParameter("claimNo");
                claimNo = claimNo == null ? "" : claimNo.trim();

                boolean hasPolNo = polNo != null && !polNo.trim().isEmpty();
                boolean hasClaimNo = claimNo != null && !claimNo.trim().isEmpty();

                // 入力チェック（両方空）
                if (!hasPolNo && !hasClaimNo) {
                    request.setAttribute("fieldErrors", java.util.Map.of("polNo", "証券番号または事故受付番号を入力してください。"));
                    request.getRequestDispatcher("/WEB-INF/view/accident/accident.jsp").forward(request, response);
                    return;
                }

                // 排他チェック（両方入力された場合はエラーとする）
                if (hasPolNo && hasClaimNo) {
                    request.setAttribute("fieldErrors", java.util.Map.of("polNo", "新規受付の場合は証券番号のみ、更新の場合は事故受付番号のみを入力してください。"));
                    request.getRequestDispatcher("/WEB-INF/view/accident/accident.jsp").forward(request, response);
                    return;
                }

                // A: 事故受付番号が入力された場合 (既存データの更新・再開)
                if (hasClaimNo) {
                    Accident accidentData = accidentDao.getAccident(claimNo);

                    if (accidentData == null) {
                        request.setAttribute("fieldErrors", java.util.Map.of("claimNo", "該当する事故受付番号が見つかりませんでした。"));
                        request.getRequestDispatcher("/WEB-INF/view/accident/accident.jsp").forward(request, response);
                        return;
                    }

                    // すでに完了（事故受付フラグが9）している場合は accident.jsp に戻す
                    if (accidentData.getClaimStatus() == 9) {
                        request.setAttribute("fieldErrors", java.util.Map.of("claimNo", "この事故受付は完了しています。"));
                        request.getRequestDispatcher("/WEB-INF/view/accident/accident.jsp").forward(request, response);
                        return;
                    }

                    // 事故データに紐づく証券番号から契約情報および補償情報を取得
                    Claim claimData = null;
                    Contract contractData = null;

                    String polNoFromAccident = accidentData.getPolNo();
                    if (polNoFromAccident != null && !polNoFromAccident.trim().isEmpty()) {
                        contractData = contractDao.getContract(polNoFromAccident);
                        claimData = claimDao.getClaim(polNoFromAccident);
                    }

                    request.setAttribute("accident", accidentData);
                    request.setAttribute("contract", contractData);
                    request.setAttribute("claim", claimData);

                    if (contractData != null) {
                        request.setAttribute("nameKanji1", contractData.getNameKanji1());
                    }
                }
                // B: 証券番号が入力された場合 (新規受付)
                else if (hasPolNo) {

                    // すでにこの証券番号で事故受付が登録されていないかチェック
                    Accident existingAccident = accidentDao.getAccidentByPolNo(polNo);
                    if (existingAccident != null) {
                            request.setAttribute("fieldErrors", java.util.Map.of("polNo",
                                "既にこの証券番号の事故受付番号が存在します。（受付番号: " + existingAccident.getClaimNo() + "）"));
                            request.getRequestDispatcher("/WEB-INF/view/accident/accident.jsp").forward(request, response);
                            return;
                    }

                    Contract contractData = contractDao.getContract(polNo);

                    if (contractData == null) {
                        request.setAttribute("fieldErrors", java.util.Map.of("polNo", "該当する証券番号（契約情報）が見つかりませんでした。"));
                        request.getRequestDispatcher("/WEB-INF/view/accident/accident.jsp").forward(request, response);
                        return;
                    }

                    // 証券番号に紐づく補償情報を取得
                    Claim claimData = claimDao.getClaim(polNo);

                    // 新規受付用の事故データオブジェクトを作成してセット
                    Accident newAccident = new Accident();
                    newAccident.setPolNo(polNo);

                    if (claimData != null) {
                        newAccident.setCoverId(claimData.getCoverId());
                    }

                    //request.setAttribute("nameKanji1", contractData.getNameKanji1());
                    request.setAttribute("accident", newAccident);
                    request.setAttribute("contract", contractData);
                    request.setAttribute("claim", claimData);
                }

                Contract contractData = (Contract) request.getAttribute("contract");
                Claim claimData = (Claim) request.getAttribute("claim");
                Accident accidentData = (Accident) request.getAttribute("accident");
                if (contractData == null || claimData == null || claimData.getCoverId() == null
                        || accidentData == null || accidentData.getCoverId() != claimData.getCoverId()) {
                    request.setAttribute("fieldErrors", java.util.Map.of(hasClaimNo ? "claimNo" : "polNo",
                            "関連する契約・補償情報が見つかりませんでした。"));
                    request.getRequestDispatcher("/WEB-INF/view/accident/accident.jsp")
                            .forward(request, response);
                    return;
                }
                String jsp = Integer.valueOf(2).equals(contractData.getInsuredKbn())
                        ? "/WEB-INF/view/accident/accident-detail-corporate.jsp"
                        : "/WEB-INF/view/accident/accident-detail.jsp";
                request.getRequestDispatcher(jsp).forward(request, response);
                return;
            }
            }

            response.sendError(HttpServletResponse.SC_NOT_FOUND);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", ErrorMsgConst.SYSTEM_ERROR);
            request.getRequestDispatcher("/WEB-INF/view/error/error.jsp").forward(request, response);
        }
    }
}