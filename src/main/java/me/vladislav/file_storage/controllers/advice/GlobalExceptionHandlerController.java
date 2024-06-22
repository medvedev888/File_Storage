package me.vladislav.file_storage.controllers.advice;

import jakarta.servlet.http.HttpServletResponse;
import me.vladislav.file_storage.exceptions.folders.FolderCreationException;
import me.vladislav.file_storage.exceptions.folders.FolderDeletionException;
import me.vladislav.file_storage.exceptions.users.UserAlreadyExistException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

@ControllerAdvice
public class GlobalExceptionHandlerController {

    @ExceptionHandler(UserAlreadyExistException.class)
    public RedirectView userAlreadyExistException(UserAlreadyExistException exception, RedirectAttributes redirectAttributes, HttpServletResponse response) {
        redirectAttributes.addFlashAttribute("errorMessage", exception.getMessage());
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        return new RedirectView("/registration", true);
    }

    @ExceptionHandler(FolderCreationException.class)
    public RedirectView handleInvalidFileRequests(FolderCreationException exception, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("errorMessage", exception.getMessage());
        return new RedirectView("/", true);
    }

    @ExceptionHandler(FolderDeletionException.class)
    public RedirectView handleInvalidFileRequests(FolderDeletionException exception, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("errorMessage", exception.getMessage());
        return new RedirectView("/", true);
    }

}
