package sevenislands.util.Exceptions;

import org.springframework.dao.DataAccessException;

public class NotFoundException extends DataAccessException {

    public NotFoundException(String msg) {
        super(msg);
        //TODO Auto-generated constructor stub
    }
    
}
