package agentdd.controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.sql.Connection;
import java.sql.SQLException;
import agentdd.model.constant.ErrorMsgConst;
import agentdd.model.dao.ConnectionManager;
import agentdd.model.dao.AccidentDao;
import agentdd.model.dao.ClaimDao;
import agentdd.model.dao.ContractDao;
import agentdd.model.data.Accident;

/**
 * 事故情報の登録および更新処理を制御するコントローラーサーブレット。
 */
@WebServlet("/accident/submit")
public class AccidentSubmitController extends HttpServlet {

    /**
     * 事故情報の登録・更新リクエスト（POST）を受け取り、バリデーションとDB保存処理を行う。
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // リクエストパラメータの文字エンコーディングをUTF-8に指定
        request.setCharacterEncoding("UTF-8");

        try (Connection con = ConnectionManager.getConnection()) {
            AccidentDao accidentDao = new AccidentDao(con);
            ContractDao contractDao = new ContractDao(con);
            ClaimDao claimDao = new ClaimDao(con);
            // 0. 押されたボタンの種類（action: "completeReceipt" 等）を先に取得
            String action = request.getParameter("action");

            // 1. バリデーション：過失割合のチェック
            int myFault = (int) parseLong(request.getParameter("ratingBlameMyself"));
            int yourFault = (int) parseLong(request.getParameter("ratingBlameYourself"));

            boolean isInvalidRatio = false;
            if ("completeReceipt".equals(action)) {
                // 【事故受付完了時】必ず過失割合の合計が100でなければならない
                if (myFault + yourFault != 100) {
                    isInvalidRatio = true;
                }
            } else {
                // 【状況更新時】未入力（合計0）はOKだが、入力するなら合計100にしなければならない
                if (myFault + yourFault != 100 && (myFault + yourFault != 0)) {
                    isInvalidRatio = true;
                }
            }

            // 過失割合が不正な場合、エラーメッセージを設定して入力画面へ差し戻す
            if (isInvalidRatio) {
                request.setAttribute("accident", createAccidentFromRequest(request));
                restoreRelatedData(request, contractDao, claimDao);
                request.setAttribute("errorMessage", "過失割合の合計が100になるように入力してください。（未定の場合は空欄でも可能です）");
                request.getRequestDispatcher("/WEB-INF/view/accident/accident-detail.jsp").forward(request, response);
                return;
            }

            // 2. 保険金額（支払金額）を計算する
            long paymentAmount = calculatePaymentAmount(request);

            // 3. ステータスとメッセージの決定
            int claimStatus = "completeReceipt".equals(action) ? 9 : 1; // 1:受付中, 9:完了済み
            String completeMessage = claimStatus == 9 ? "事故受付が完了しました" : "事故状況を更新しました";

            // --- Accident オブジェクトの生成とリクエストパラメータの値のセット ---
            Accident accident = new Accident();
            String claimNo = request.getParameter("claimNo");
            accident.setClaimNo(claimNo);
            accident.setPolNo(request.getParameter("polNo"));
            accident.setCoverId(parseInt(request.getParameter("coverId")));

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

            // 4. 既存データの存在をチェックし、INSERTかUPDATEを自動振り分け
            con.setAutoCommit(false);

            try {
                Accident existingAccident = accidentDao.getAccident(claimNo);
                if (existingAccident == null) {
                    accidentDao.insertAccident(accident);
                } else {
                    accidentDao.updateAccident(accident);
                }
                con.commit();
            } catch (SQLException | RuntimeException e) {
                try {
                    con.rollback();
                } catch (SQLException rollbackError) {
                    e.addSuppressed(rollbackError);
                }
                throw e;
            }

            // 5. 完了画面の表示用に必要なデータをリクエストスコープにセット
            request.setAttribute("claimNo", claimNo);
            request.setAttribute("polNo", request.getParameter("polNo"));
            request.setAttribute("contractorName", request.getParameter("contractorName"));
            request.setAttribute("paymentAmount", paymentAmount);
            request.setAttribute("completeMessage", completeMessage);

            // 6. 完了画面へフォワード
            request.getRequestDispatcher("/WEB-INF/view/accident/accident-complete.jsp").forward(request, response);

        } catch (Exception e) {
            // 予期せぬ例外が発生した場合はスタックトレースを出力し、エラー画面へ遷移
            e.printStackTrace();
            request.setAttribute("error", ErrorMsgConst.SYSTEM_ERROR);
            request.getRequestDispatcher("/WEB-INF/view/error/error.jsp")
                    .forward(request, response);
        }
    }

    /**
     * 各種損害額の合計に対し、自己の過失割合を掛け合わせた保険支払金額を計算する。
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
     * リクエストパラメータから取得した値をもとに、Accidentオブジェクトを生成して返す。
     * バリデーションエラー時に画面へ入力値を復元する際などに使用。
     */
    private Accident createAccidentFromRequest(HttpServletRequest request) {
        Accident accident = new Accident();
        accident.setClaimNo(request.getParameter("claimNo"));
        accident.setPolNo(request.getParameter("polNo"));
        accident.setCoverId(parseInt(request.getParameter("coverId")));
        accident.setAccidentLocationKana1(request.getParameter("accidentLocationKana1"));
        accident.setAccidentLocationKana2(request.getParameter("accidentLocationKana2"));
        accident.setAccidentLocationKanji1(request.getParameter("accidentLocationKanji1"));
        accident.setAccidentLocationKanji2(request.getParameter("accidentLocationKanji2"));
        accident.setAccidentDate(request.getParameter("accidentDate"));
        accident.setAccidentSituation(request.getParameter("accidentSituation"));
        accident.setRatingBlameMyself((int) parseLong(request.getParameter("ratingBlameMyself")));
        accident.setRatingBlameYourself((int) parseLong(request.getParameter("ratingBlameYourself")));
        accident.setDamageCarPrice(parseLong(request.getParameter("damageCarPrice")));
        accident.setDamageBodilyPrice(parseLong(request.getParameter("damageBodilyPrice")));
        accident.setDamagePropertyPrice(parseLong(request.getParameter("damagePropertyPrice")));
        accident.setDamageAccidentPrice(parseLong(request.getParameter("damageAccidentPrice")));
        accident.setDamageCarState(request.getParameter("damageCarState"));
        accident.setDamageBodilyState(request.getParameter("damageBodilyState"));
        accident.setDamagePropertyState(request.getParameter("damagePropertyState"));
        accident.setDamageAccidentState(request.getParameter("damageAccidentState"));
        return accident;
    }

    /**
     * バリデーションエラー等で入力画面に戻る際に、関連する契約・事故請求データを再取得してセットする。
     */
    private void restoreRelatedData(
            HttpServletRequest request,
            ContractDao contractDao,
            ClaimDao claimDao) throws Exception {

        String polNo = request.getParameter("polNo");

        if (polNo == null || polNo.isBlank()) {
            return;
        }

        request.setAttribute("contract", contractDao.getContract(polNo.trim()));
        request.setAttribute("claim", claimDao.getClaim(polNo.trim()));
    }

    /**
     * 文字列を安全にlong型にパースする。nullや空文字、数値フォーマットエラー時は0Lを返す。
     */
    private long parseLong(String val) {
        if (val == null || val.trim().isEmpty())
            return 0L;
        try {
            return Long.parseLong(val.trim());
        } catch (NumberFormatException e) {
            return 0L;
        }
    }

    /**
     * 文字列を安全にint型にパースする。nullや空文字、数値フォーマットエラー時は0を返す。
     */
    private int parseInt(String val) {
        if (val == null || val.trim().isEmpty())
            return 0;
        try {
            return Integer.parseInt(val.trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}