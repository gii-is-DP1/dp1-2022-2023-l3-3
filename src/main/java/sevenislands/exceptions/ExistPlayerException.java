package sevenislands.exceptions;

import org.springframework.dao.DataAccessException;

public class ExistPlayerException extends DataAccessException {

    public ExistPlayerException(String msg) {
        super(msg);
        //TODO Auto-generated constructor stub
    }
    
}
