package agentdd.model.datacheck;

import agentdd.model.constant.ErrorMsgConst;
import agentdd.model.data.LoginUser;

public class LoginChecker {
    
    public String check(LoginUser loginUser) {

        if (loginUser == null
            || loginUser.getUserId() == null 
            || loginUser.getUserId().isEmpty()
            || loginUser.getPassword() == null
            || loginUser.getPassword().isEmpty()) {
                
            return ErrorMsgConst.FORM_ERROR;
        }
        return null;
    }
}
