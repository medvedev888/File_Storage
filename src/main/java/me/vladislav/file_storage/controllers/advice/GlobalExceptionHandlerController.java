package me.vladislav.file_storage.controllers.advice;

import me.vladislav.file_storage.dto.UserDTO;
import me.vladislav.file_storage.exceptions.users.UserAlreadyExistException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GlobalExceptionHandlerController {

    @ExceptionHandler(UserAlreadyExistException.class)
    public ModelAndView userAlreadyExistException(UserAlreadyExistException exception) {
        ModelAndView mav = new ModelAndView("auth/registration");
        mav.setStatus(HttpStatus.CONFLICT);
        mav.addObject("user", new UserDTO());
        mav.addObject("errorMessage", exception.getMessage());
        return mav;
    }

}
