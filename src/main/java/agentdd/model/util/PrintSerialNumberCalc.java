package agentdd.model.util;

import agentdd.model.dao.ContractDao;

public class PrintSerialNumberCalc {
    public String numbercalc(ContractDao contractDao) throws Exception {
        
        String latestNumber = contractDao.getEstimate();
        
        // ① 先頭の "A" を取り除いて数値に変換し、1を足す
        int number = Integer.parseInt(latestNumber.substring(1));
        number++;
        
        // ② "A" ＋ 7桁のゼロ埋め文字列（%07d）にフォーマットして返す
        String newSerialNumber = String.format("A%07d", number);
        
        return newSerialNumber;
    }
}
