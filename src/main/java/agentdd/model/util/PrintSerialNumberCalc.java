package agentdd.model.util;

import java.sql.Connection;
import java.sql.SQLException;

import agentdd.model.dao.ContractDao;

public class PrintSerialNumberCalc {

    public String numbercalc(Connection con) throws SQLException {
        return new ContractDao(con).generateNextInsatsurenban();
    }
}