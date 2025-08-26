package org.example.springapie.exception;


import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class GlobalHandlerException {

    @ExceptionHandler(Exception.class)
    public String handleGenericException(Exception ex, Model model) {
        log.error("Unexpected error", ex);
        model.addAttribute("errorMessage", "Unexpected error: " + ex.getMessage());
        return "error/500";
    }

    @ExceptionHandler(NullEntityReferenceException.class)
    public String handleNullPointerException(Exception ex, Model model) {
        log.error("Null Entity Reference error", ex);
        model.addAttribute("errorMessage", "Unexpected error: " + ex.getMessage());
        return "error/404";
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public String handleEntityNotFoundException(Exception ex, Model model) {
        log.error("Entity not found error", ex);
        model.addAttribute("errorMessage", "Unexpected error: " + ex.getMessage());
        return "error/404";
    }

}
