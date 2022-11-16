package sevenislands.util.Exceptions;

import org.springframework.dao.DataAccessException;

public class ExistAdminException extends DataAccessException {

    public ExistAdminException(String msg) {
        super(msg);
        //TODO Auto-generated constructor stub
    }
    
}
