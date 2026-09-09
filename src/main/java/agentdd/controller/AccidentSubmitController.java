package agentdd.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

import agentdd.model.constant.ErrorMsgConst;
import agentdd.model.dao.AccidentDao;
import agentdd.model.dao.ClaimDao;
import agentdd.model.dao.ConnectionManager;
import agentdd.model.dao.ContractDao;
import agentdd.model.data.Accident;
import agentdd.model.data.Claim;
import agentdd.model.data.Contract;
import agentdd.model.exception.BusinessException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/accident/submit")
public class AccidentSubmitController extends HttpServlet {
    private static final String[] INPUT_FIELDS = {
        "accidentDate", "accidentLocationKanji1", "accidentLocationKanji2",
        "accidentLocationKana1", "accidentLocationKana2", "accidentSituation",
        "ratingBlameMyself", "ratingBlameYourself", "damageCarPrice",
        "damageBodilyPrice", "damagePropertyPrice", "damageAccidentPrice",
        "damageCarState", "damageBodilyState", "damagePropertyState", "damageAccidentState"
    };

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");
        if (!"updateStatus".equals(action) && !"completeReceipt".equals(action)) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "操作を選択してください。");
            return;
        }
        // 入力エラー時も、数値を含む入力文字列をそのまま再表示する。
        Map<String, String> input = new LinkedHashMap<>();
        for (String name : INPUT_FIELDS) {
            String value = request.getParameter(name);
            input.put(name, value == null ? "" : value);
        }
        request.setAttribute("accidentInput", input);

        String claimNo = trimmed(request.getParameter("claimNo"));
        boolean isNew = claimNo.isEmpty();
        try (Connection con = ConnectionManager.getConnection()) {
            AccidentDao accidentDao = new AccidentDao(con);
            boolean numberLocked = false;
            try {
                // 最新値で採番するため、DBの通常SELECTより前にロックを取得する。
                if (isNew) {
                    accidentDao.lockClaimNumber();
                    numberLocked = true;
                }
                con.setAutoCommit(false);
                ContractDao contractDao = new ContractDao(con);
                ClaimDao claimDao = new ClaimDao(con);
                Accident accident;
                String polNo;
                if (isNew) {
                    polNo = trimmed(request.getParameter("polNo"));
                    if (!polNo.matches("B[0-9]{9}")) {
                        throw new BusinessException("証券番号が不正です。受付開始画面からやり直してください。");
                    }
                    accident = new Accident();
                } else {
                    if (!claimNo.matches("C[0-9]{7}")) {
                        throw new BusinessException("事故受付番号が不正です。");
                    }
                    accident = accidentDao.getAccidentForUpdate(claimNo);
                    if (accident == null) {
                        throw new BusinessException("更新対象の事故情報が見つかりませんでした。");
                    }
                    if (accident.getClaimStatus() == 9) {
                        throw new BusinessException("この事故受付は既に完了しています。");
                    }
                    polNo = trimmed(accident.getPolNo());
                }

                Contract contract = contractDao.getContract(polNo);
                Claim claim = claimDao.getClaim(polNo);
                if (contract == null || claim == null || claim.getCoverId() == null) {
                    throw new BusinessException("関連する契約・補償情報が見つかりませんでした。");
                }
                if (!isNew && accident.getCoverId() != claim.getCoverId()) {
                    throw new BusinessException("事故と補償情報の関連を確認できませんでした。");
                }
                accident.setPolNo(contract.getPolNo());
                accident.setCoverId(claim.getCoverId());
                request.setAttribute("contract", contract);
                request.setAttribute("claim", claim);
                request.setAttribute("accident", accident);

                bindAndValidate(input, accident, "completeReceipt".equals(action));
                accident.setClaimStatus("completeReceipt".equals(action) ? 9 : 1);
                accident.setPaymentPrice(calculatePaymentAmount(accident));
                if (isNew) {
                    accident.setClaimNo(accidentDao.generateNextClaimNo());
                    accidentDao.insertAccident(accident);
                } else {
                    accidentDao.updateAccident(accident);
                }
                con.commit();

                request.setAttribute("claimNo", accident.getClaimNo());
                request.setAttribute("polNo", contract.getPolNo());
                String name = trimmed(contract.getNameKanji1());
                if (!Integer.valueOf(2).equals(contract.getInsuredKbn())) {
                    name = (name + " " + trimmed(contract.getNameKanji2())).trim();
                }
                request.setAttribute("contractorName", name);
                request.setAttribute("paymentAmount", accident.getPaymentPrice());
                request.setAttribute("completeMessage", accident.getClaimStatus() == 9
                        ? "事故受付が完了しました" : "事故状況を更新しました");
            } catch (SQLException | BusinessException | RuntimeException e) {
                try {
                    con.rollback();
                } catch (SQLException rollbackError) {
                    e.addSuppressed(rollbackError);
                }
                throw e;
            } finally {
                if (numberLocked) {
                    try {
                        accidentDao.unlockClaimNumber();
                    } catch (SQLException releaseError) {
                        // 元の例外や保存成功を上書きしない。
                        // この後try-with-resourcesで物理接続を閉じ、セッションロックを解放する。
                        getServletContext().log("事故受付番号のロック解放に失敗しました。", releaseError);
                    }
                }
            }
        } catch (BusinessException e) {
            Contract contract = (Contract) request.getAttribute("contract");
            if (contract != null) {
                request.setAttribute("errorMessage", e.getMessage());
                String jsp = Integer.valueOf(2).equals(contract.getInsuredKbn())
                        ? "/WEB-INF/view/accident/accident-detail-corporate.jsp"
                        : "/WEB-INF/view/accident/accident-detail.jsp";
                request.getRequestDispatcher(jsp).forward(request, response);
            } else {
                request.setAttribute("error", e.getMessage());
                request.getRequestDispatcher("/WEB-INF/view/error/error.jsp").forward(request, response);
            }
            return;
        } catch (SQLException | RuntimeException e) {
            getServletContext().log("事故情報の保存に失敗しました。", e);
            request.setAttribute("error", ErrorMsgConst.SYSTEM_ERROR);
            request.getRequestDispatcher("/WEB-INF/view/error/error.jsp").forward(request, response);
            return;
        }
        request.getRequestDispatcher("/WEB-INF/view/accident/accident-complete.jsp")
                .forward(request, response);
    }

    private void bindAndValidate(Map<String, String> input, Accident accident, boolean complete)
            throws BusinessException {
        int mine = (int) readNumber(input.get("ratingBlameMyself"), "被保険者の過失割合", 100);
        int theirs = (int) readNumber(input.get("ratingBlameYourself"), "相手方の過失割合", 100);
        // 現行仕様の「状況更新時は合計0または100」を維持する。
        if ((complete && mine + theirs != 100)
                || (!complete && mine + theirs != 0 && mine + theirs != 100)) {
            throw new BusinessException(complete
                    ? "事故受付完了時は過失割合の合計を100にしてください。"
                    : "過失割合の合計を100にしてください。未定の場合は双方を空欄または0にしてください。");
        }
        accident.setRatingBlameMyself(mine);
        accident.setRatingBlameYourself(theirs);
        accident.setDamageCarPrice(readNumber(input.get("damageCarPrice"), "車両損害額", 999_999_999_999_999_999L));
        accident.setDamageBodilyPrice(readNumber(input.get("damageBodilyPrice"), "対人損害額", 999_999_999_999_999_999L));
        accident.setDamagePropertyPrice(readNumber(input.get("damagePropertyPrice"), "対物損害額", 999_999_999_999_999_999L));
        accident.setDamageAccidentPrice(readNumber(input.get("damageAccidentPrice"), "傷害損害額", 999_999_999_999_999_999L));
        accident.setAccidentDate(input.get("accidentDate"));
        accident.setAccidentLocationKanji1(input.get("accidentLocationKanji1"));
        accident.setAccidentLocationKanji2(input.get("accidentLocationKanji2"));
        accident.setAccidentLocationKana1(input.get("accidentLocationKana1"));
        accident.setAccidentLocationKana2(input.get("accidentLocationKana2"));
        accident.setAccidentSituation(input.get("accidentSituation"));
        accident.setDamageCarState(input.get("damageCarState"));
        accident.setDamageBodilyState(input.get("damageBodilyState"));
        accident.setDamagePropertyState(input.get("damagePropertyState"));
        accident.setDamageAccidentState(input.get("damageAccidentState"));
    }

    private long readNumber(String raw, String label, long maximum) throws BusinessException {
        String value = trimmed(raw);
        if (value.isEmpty()) return 0;
        if (!value.matches("[0-9]{1,18}")) {
            throw new BusinessException(label + "は半角数字で入力してください。");
        }
        long number = Long.parseLong(value);
        if (number > maximum) {
            throw new BusinessException(label + "は0～" + maximum + "の範囲で入力してください。");
        }
        return number;
    }

    private long calculatePaymentAmount(Accident accident) {
        // 現行の式を維持し、18桁の金額をdoubleに変換しない。
        long total = accident.getDamageCarPrice() + accident.getDamageBodilyPrice()
                + accident.getDamagePropertyPrice() + accident.getDamageAccidentPrice();
        return BigDecimal.valueOf(total).multiply(BigDecimal.valueOf(accident.getRatingBlameMyself()))
                .divide(BigDecimal.valueOf(100), 0, RoundingMode.HALF_UP).longValueExact();
    }

    private String trimmed(String value) {
        return value == null ? "" : value.trim();
    }
}
