package agentdd.model.constant;

public class SystemConst {
    
    // インスタンス生成を禁止する。
    private SystemConst() {
        
    }   

    // JDBCドライバー名
    public static final String JDBC_DRIVER_NAME = "com.mysql.cj.jdbc.Driver";

    // JDBC接続文字列
    public static final String JDBC_URL = getEnvironmentValue(
        "AGENTDD_DB_URL",
        "jdbc:mysql://localhost:3306/final_exercise_db"
        + "?useUnicode=true"
        + "&characterEncoding=UTF-8"
        + "&serverTimezone=Asia/Tokyo"
        + "&useSSL=false"
        + "&allowPublicKeyRetrieval=true"
    );
    
    // DBユーザー名
    public static final String JDBC_USER = getEnvironmentValue(
        "AGENTDD_DB_USER",
        "root"
    );

    // DBパスワード名
    public static final String JDBC_PASSWORD = getEnvironmentValue(
        "AGENTDD_DB_PASSWORD",
        "mysql"
    );

    // 文字コード
    public static final String CHAR_SET = "UTF-8";

    /**
     * 環境変数を取得する。
     * 環境変数が設定されていない場合は、デフォルト値を返却する。
     *
     * @param environmentName 環境変数名
     * @param defaultValue デフォルト値
     * @return 設定値
     */
    private static String getEnvironmentValue(
            String environmentName,
            String defaultValue) {

        String value = System.getenv(environmentName);

        if (value == null || value.isBlank()) {
            return defaultValue;
        }

        return value;
    } 
}