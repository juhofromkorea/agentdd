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

    /** 解約申込中の契約で事故受付を行った場合 */
    public static final String ACCIDENT_CANCEL_PENDING =
            "この契約は解約申込中のため、事故受付できません。";

    /** 解約済みの契約で事故受付を行った場合 */
    public static final String ACCIDENT_CANCELLED =
            "この契約は解約済みのため、事故受付できません。";
}
