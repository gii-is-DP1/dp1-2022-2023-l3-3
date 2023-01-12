package sevenislands.configuration;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ExceptionHandlerConfiguration {

    @ExceptionHandler(Exception.class)
    public String defaultErrorHandler(HttpServletRequest request, Exception ex) {
        request.setAttribute("javax.servlet.error.request_uri", request.getPathInfo());
        request.setAttribute("javax.servlet.error.status_code", 400);
        if(ex.getMessage().length() > 100)
            request.setAttribute("exception", ex.getMessage().substring(0, 200)+"...");
        else {
            request.setAttribute("exception", ex.getMessage());
        }
        return "exception";
    }
}