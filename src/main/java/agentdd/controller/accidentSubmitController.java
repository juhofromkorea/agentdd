package agentdd.controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import agentdd.model.dao.AccidentDao;
import agentdd.model.data.Accident; 

@WebServlet("/accident/submit")
public class AccidentSubmitController extends HttpServlet {

    private AccidentDao accidentDao;

    @Override
    public void init() throws ServletException {
        try {
            accidentDao = new AccidentDao();
        } catch (Exception e) {
            throw new ServletException("DAOの初期化に失敗しました", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        try {
            // 1. バリデーション：過失割合の合計が100になるかチェック
            int myFault = (int) parseLong(request.getParameter("ratingBlameMyself"));
            int yourFault = (int) parseLong(request.getParameter("ratingBlameYourself"));
            
            if (myFault + yourFault != 100) {
                // エラー時は元の入力値を保持できるようにオブジェクトに詰めてスコープに戻す
                request.setAttribute("errorMessage", "過失割合の合計が100になるように入力してください。");
                request.getRequestDispatcher("/WEB-INF/view/accident/accident-detail.jsp").forward(request, response);
                return;
            }

            // 2. 先に保険金額（支払金額）を計算する
            long paymentAmount = calculatePaymentAmount(request);

            // 3. ボタンの action パラメータによる分岐とステータスの決定
            String action = request.getParameter("action");
            int claimStatus = "completeReceipt".equals(action) ? 9 : 1; // 1:受付中, 9:完了済み
            String completeMessage = claimStatus == 9 ? "事故受付が完了しました" : "事故状況を更新しました";

            // --- Accident オブジェクトの生成と値のセット ---
            Accident accident = new Accident();
            String claimNo = request.getParameter("claimNo");
            accident.setClaimNo(claimNo);

            accident.setCoverId(1);

            // 画面から送られてきた coverId をセット（隠しフィールド等で保持している前提）
            // accident.setCoverId((int) parseLong(request.getParameter("coverId")));
            
            accident.setClaimStatus(claimStatus);
            accident.setPaymentPrice(paymentAmount);

            // 事故場所・日時・状況
            accident.setAccidentLocationKana1(request.getParameter("accidentLocationKana1"));
            accident.setAccidentLocationKana2(request.getParameter("accidentLocationKana2"));
            accident.setAccidentLocationKanji1(request.getParameter("accidentLocationKanji1"));
            accident.setAccidentLocationKanji2(request.getParameter("accidentLocationKanji2"));
            accident.setAccidentDate(request.getParameter("accidentDate"));
            accident.setAccidentSituation(request.getParameter("accidentSituation"));

            // 過失割合
            accident.setRatingBlameMyself(myFault);
            accident.setRatingBlameYourself(yourFault);

            // 各種損害額
            accident.setDamageCarPrice(parseLong(request.getParameter("damageCarPrice")));
            accident.setDamageBodilyPrice(parseLong(request.getParameter("damageBodilyPrice")));
            accident.setDamagePropertyPrice(parseLong(request.getParameter("damagePropertyPrice")));
            accident.setDamageAccidentPrice(parseLong(request.getParameter("damageAccidentPrice")));

            // 各種損害の状態
            accident.setDamageCarState(request.getParameter("damageCarState"));
            accident.setDamageBodilyState(request.getParameter("damageBodilyState"));
            accident.setDamagePropertyState(request.getParameter("damagePropertyState"));
            accident.setDamageAccidentState(request.getParameter("damageAccidentState"));

            // 4. コントローラー側でデータの存在をチェックし、INSERTかUPDATEを振り分ける
            Accident existingAccident = accidentDao.getAccident(claimNo);
            if (existingAccident == null) {
                // 存在しない場合は新規登録
                accidentDao.insertAccident(accident);
            } else {
                // 存在する場合は更新
                accidentDao.updateAccident(accident);
            }
            // -----------------------------------------------------------

            // 5. 完了画面表示用データのセット
            request.setAttribute("claimNo", claimNo);
            request.setAttribute("polNo", request.getParameter("polNo"));
            request.setAttribute("contractorName", request.getParameter("contractorName"));
            request.setAttribute("paymentAmount", paymentAmount);
            request.setAttribute("completeMessage", completeMessage);

            // 6. 完了画面へフォワード
            request.getRequestDispatcher("/WEB-INF/view/accident/accident-complete.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "システムエラー");
            request.getRequestDispatcher("/WEB-INF/view/error.jsp").forward(request, response);
        }
    }

    /**
     * 各種損害額の合計と、自身の過失割合に基づいて支払金額（保険金額）を計算します。
     */
    private long calculatePaymentAmount(HttpServletRequest request) {
        long vehicleDamage = parseLong(request.getParameter("damageCarPrice"));
        long bodilyDamage = parseLong(request.getParameter("damageBodilyPrice"));
        long propertyDamage = parseLong(request.getParameter("damagePropertyPrice"));
        long injuryDamage = parseLong(request.getParameter("damageAccidentPrice"));

        long totalDamage = vehicleDamage + bodilyDamage + propertyDamage + injuryDamage;
        double faultRatio = parseLong(request.getParameter("ratingBlameMyself")) / 100.0;

        return Math.round(totalDamage * faultRatio);
    }

    /**
     * 文字列を安全に long 型の数値へ変換します。
     */
    private long parseLong(String val) {
        if (val == null || val.trim().isEmpty()) return 0L;
        try { 
            return Long.parseLong(val.trim()); 
        } catch (NumberFormatException e) { 
            return 0L; 
        }
    }
}