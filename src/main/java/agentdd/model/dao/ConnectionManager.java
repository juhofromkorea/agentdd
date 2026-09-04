package agentdd.model.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import agentdd.model.constant.SystemConst;

/**
 * DBコネクション管理クラス
 */
public final class ConnectionManager {

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

        return DriverManager.getConnection(
                SystemConst.JDBC_URL,
                SystemConst.JDBC_USER,
                SystemConst.JDBC_PASSWORD
        );
    }
}