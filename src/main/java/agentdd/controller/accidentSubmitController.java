package agentdd.controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import agentdd.model.dao.AccidentDao;

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
            int myFault = (int) parseLong(request.getParameter("insuredFaultRatio"));
            int yourFault = (int) parseLong(request.getParameter("ratingBlameYourself"));
            
            if (myFault + yourFault != 100) {
                request.setAttribute("errorMessage", "過失割合の合計が100になるように入力してください。");
                // パスを /WEB-INF/view/ に統一
                request.getRequestDispatcher("/WEB-INF/view/accident/accident-detail.jsp").forward(request, response);
                return;
            }

            // 2. 先に保険金額（支払金額）を計算する
            long paymentAmount = calculatePaymentAmount(request);

            // 3. ボタンの action パラメータによる分岐とDB更新
            String action = request.getParameter("action");
            int claimStatus = "completeReceipt".equals(action) ? 9 : 1; // 1:受付中, 9:完了済み
            String completeMessage = claimStatus == 9 ? "事故受付が完了しました" : "事故状況を更新しました";

            saveAccidentData(request, paymentAmount, claimStatus);

            // 4. 完了画面表示用データのセット
            request.setAttribute("claimNo", request.getParameter("claimNo"));
            request.setAttribute("polNo", request.getParameter("polNo"));
            request.setAttribute("contractorName", request.getParameter("contractorName"));
            request.setAttribute("paymentAmount", paymentAmount);
            request.setAttribute("completeMessage", completeMessage);

            // 5. 完了画面へフォワード（パスを /WEB-INF/view/ に統一）
            request.getRequestDispatcher("/WEB-INF/view/accident/accident-complete.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "システムエラー");
            request.getRequestDispatcher("/WEB-INF/view/error.jsp").forward(request, response);
        }
    }

    private void saveAccidentData(HttpServletRequest request, long paymentAmount, int claimStatus) {
        try {
            java.lang.reflect.Method method = AccidentDao.class.getMethod(
                    "saveAccidentData",
                    HttpServletRequest.class,
                    long.class,
                    int.class
            );
            method.invoke(accidentDao, request, paymentAmount, claimStatus);
            return;
        } catch (NoSuchMethodException e) {
            // フォールバック
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("事故データの保存に失敗しました。", e);
        }

        try {
            Class<?> accidentClass = Class.forName("agentdd.model.data.Accident");
            Object accident = accidentClass.getDeclaredConstructor().newInstance();

            setProperty(accident, "accidentNo", request.getParameter("claimNo"));
            setProperty(accident, "accidentFlag", claimStatus);
            setProperty(accident, "paymentAmount", Math.toIntExact(paymentAmount));
            setProperty(accident, "negligenceInsured", parseLong(request.getParameter("insuredFaultRatio")));
            setProperty(accident, "negligenceOpponent", parseLong(request.getParameter("ratingBlameYourself")));
            setProperty(accident, "damageVehicle", parseLong(request.getParameter("vehicleDamageAmount")));
            setProperty(accident, "damagePerson", parseLong(request.getParameter("bodilyDamageAmount")));
            setProperty(accident, "damageObject", parseLong(request.getParameter("propertyDamageAmount")));
            setProperty(accident, "damageInjury", parseLong(request.getParameter("injuryDamageAmount")));

            java.lang.reflect.Method saveMethod = AccidentDao.class.getMethod("setAccident", accidentClass);
            saveMethod.invoke(accidentDao, accident);
        } catch (ReflectiveOperationException e) {
            // 例外処理
        }
    }

    private void setProperty(Object target, String propertyName, Object value) {
        if (target == null || propertyName == null || propertyName.isBlank()) {
            return;
        }
        try {
            java.lang.reflect.Field field = target.getClass().getDeclaredField(propertyName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (NoSuchFieldException e) {
            try {
                java.lang.reflect.Method setter = target.getClass().getMethod(
                        "set" + Character.toUpperCase(propertyName.charAt(0)) + propertyName.substring(1),
                        value == null ? Object.class : value.getClass()
                );
                setter.invoke(target, value);
            } catch (ReflectiveOperationException ignored) {
            }
        } catch (IllegalAccessException ignored) {
        }
    }

    private long calculatePaymentAmount(HttpServletRequest request) {
        long vehicleDamage = parseLong(request.getParameter("vehicleDamageAmount"));
        long bodilyDamage = parseLong(request.getParameter("bodilyDamageAmount"));
        long propertyDamage = parseLong(request.getParameter("propertyDamageAmount"));
        long injuryDamage = parseLong(request.getParameter("injuryDamageAmount"));

        long totalDamage = vehicleDamage + bodilyDamage + propertyDamage + injuryDamage;
        double faultRatio = parseLong(request.getParameter("insuredFaultRatio")) / 100.0;

        return Math.round(totalDamage * faultRatio);
    }

    private long parseLong(String val) {
        if (val == null || val.trim().isEmpty()) return 0L;
        try { 
            return Long.parseLong(val.trim()); 
        } catch (NumberFormatException e) { 
            return 0L; 
        }
    }
}