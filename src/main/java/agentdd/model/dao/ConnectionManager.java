package agentdd.model.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import agentdd.model.constant.SystemConst;

/**
 * DBコネクション管理クラス
 */
public final class ConnectionManager {

    static {
        try {
            Class.forName(SystemConst.JDBC_DRIVER_NAME);
        } catch (ClassNotFoundException e) {
            throw new ExceptionInInitializerError(e);
        }
    }
    
    // インスタンス生成を禁止する。
    private ConnectionManager() {

    }

    /**
     * DBコネクションを取得する。
     *
     * @return DBコネクション
     * @throws SQLException DB接続に失敗した場合
     */
    public static Connection getConnection()
            throws SQLException {

                try {
            // ★超重要：MySQL 8系に対応したドライバクラスを明示的に読み込ませる！
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            // ドライバのjarファイル自体が見つからない場合の絶望的エラー
            throw new SQLException("MySQL JDBC Driverが見つかりません。", e);
        }

        return DriverManager.getConnection(
                SystemConst.JDBC_URL,
                SystemConst.JDBC_USER,
                SystemConst.JDBC_PASSWORD
        );
    }
}