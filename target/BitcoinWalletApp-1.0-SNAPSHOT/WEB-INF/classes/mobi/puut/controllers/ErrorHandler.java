package mobi.puut.controllers;


import org.springframework.dao.DataAccessException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


/**
 * Created by Chaklader on 6/16/17.
 */
@ControllerAdvice
public class ErrorHandler {

    /**
     *
     * @param ex takes DataAccessException as argument
     * when it has issue to conenct with the database
     *
     * @return returns the designated error page
     */
    @ExceptionHandler(DataAccessException.class)
    public String handleDatabaseException(DataAccessException ex) {

        ex.printStackTrace();
        return "error";
    }

    @ExceptionHandler(AccessDeniedException.class)
    public String handleAccessException(AccessDeniedException ex) {
        return "denied";
    }
}

