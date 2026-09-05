package agentdd.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import agentdd.model.data.LoginUser;

public class LoginDao {
    
    private Connection con;

    public LoginDao(Connection con) {
        this.con = con;
    }

    public LoginUser findByUserId(String userId) throws SQLException {
        
        String sql = """
                SELECT `user`, `pass`, login_count, lock_flag
                FROM login_user
                WHERE `user` = ?
                FOR UPDATE
                """;

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }
                LoginUser loginUser = new LoginUser();
                loginUser.setUserId(rs.getString("user"));
                loginUser.setPassword(rs.getString("pass"));
                loginUser.setLoginCount(rs.getInt("login_count"));
                loginUser.setLockFlag(rs.getInt("lock_flag"));
                
                return loginUser;
            }
        }
    }    
    
    public void increaseLoginCount(String userId) throws SQLException {

        String sql = """
                UPDATE login_user
                SET lock_flag = CASE WHEN login_count >= 4 THEN 1 ELSE lock_flag END,
                    login_count = LEAST(login_count + 1, 5)
                WHERE `user` = ?
                """;
        
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, userId);

            int updatedRows = ps.executeUpdate();

            if (updatedRows != 1) {
                throw new SQLException("ログイン失敗回数の更新に失敗しました。");
            }
        }
    }

    public void resetLoginCount(String userId)
            throws SQLException {

        String sql = """
                UPDATE login_user
                SET login_count = 0
                WHERE `user` = ?
                """;

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, userId);

            int updatedRows = ps.executeUpdate();

            if (updatedRows != 1) {
                throw new SQLException("ログイン失敗回数の初期化に失敗しました。");
            }
        }
    }
}
