package agentdd.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import agentdd.model.constant.ErrorMsgConst;
import agentdd.model.dao.ConnectionManager;
import agentdd.model.dao.ContractDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet ("/accountComplete")
public class AccountingSubmitController extends HttpServlet{
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException{

            //文字コード設定
            request.setCharacterEncoding("UTF-8");

            //前画面から格納された印刷連番をセッションスコープから取得する
            HttpSession session = request.getSession(false);
            String insatsuRenban = (String) session.getAttribute("insatsuRenban");

            //セッション期限切れチェック
            if(session == null){
            request.setAttribute("errMsg", "セッションの有効期限が切れました。最初からやり直してください。");
            request.getRequestDispatcher("/WEB-INF/view/Error.jsp");
            }
    
            try(Connection con = ConnectionManager.getConnection()){

                ContractDao contractDao = new ContractDao(con);

                //証券番号を発行する
                String polNo =contractDao.generateNextPolNo();
                //状態フラグをセッションスコープから取得する
                Integer status_Flg = (Integer)session.getAttribute("polNo");

                //DBで更新処理を行う() *ContractDaoを編集、を解約の場合のメソッドをつける*
                
        
                //状態フラグと証券番号を更新する(新規)
                if(status_Flg != null && status_Flg ==1){
                    contractDao.updateKeijoStatus(insatsuRenban,polNo);
                
                }else if(status_Flg != null && status_Flg ==9){

                    //状態フラグと解約フラグを更新する(このメソッドは後で追加します)
                    contractDao.setCancel(insatsuRenban);
                }

                //JSP表示用
                request.setAttribute("polNo", polNo);

                //計上完了画面へ
                request.getRequestDispatcher("/WEB-INF/view/accounting-complete.jsp")
                .forward(request, response);

            } catch (SQLException e) {
                e.printStackTrace();
                request.setAttribute("errMsg", ErrorMsgConst.UNEXPECTED_ERROR);
                request.getRequestDispatcher("/WEB-INF/view/Error.jsp");
            }

        }

        protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    

}
}