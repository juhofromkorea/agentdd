package agentdd.model.constant;

public class ErrorMsgConst {
    
    /** フォームに未入力項目がある場合のエラーメッセージ */
    public static final String FORM_ERROR = "未入力の項目があります。入力内容をご確認ください。";

    /** システムエラーが発生した場合のエラーメッセージ */
    public static final String SYSTEM_ERROR = "システムエラーが発生しました。システム管理者に連絡してください。";

    /** 想定外のエラーが発生した場合のエラーメッセージ */
    public static final String UNEXPECTED_ERROR = "予期せぬエラーが発生しました。システム管理者に連絡してください。";

    /** セッションが無効になった場合のエラーメッセージ */
    public static final String SESSION_ERROR = "セッションが無効となりました。手続きをやり直してください。";

    public static final String LOGIN_ERROR = "ユーザーIDまたはパスワードが正しくありません。";

    public static final String ACCOUNT_LOCKED = "アカウントがロックされています。管理者にお問い合わせください。";
}
