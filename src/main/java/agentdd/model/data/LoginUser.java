package agentdd.model.data;

public class LoginUser {
    
    private String userId;
    private String password;
    private int loginCount;
    private int lockFlag;

    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public int getLoginCount() {
        return loginCount;
    }
    public void setLoginCount(int loginCount) {
        this.loginCount = loginCount;
    }
    public int getLockFlag() {
        return lockFlag;
    }
    public void setLockFlag(int lockFlag) {
        this.lockFlag = lockFlag;
    }
}
