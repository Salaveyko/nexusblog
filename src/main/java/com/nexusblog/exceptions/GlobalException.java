package com.nexusblog.exceptions;

import com.google.common.base.VerifyException;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.nio.file.attribute.UserPrincipalNotFoundException;

@ControllerAdvice
public class GlobalException extends ResponseEntityExceptionHandler {

    @ExceptionHandler(CommentNotFoundException.class)
    public String handleCommentNotFoundException(
            CommentNotFoundException ex, Model model) {

        model.addAttribute("error", ex.getMessage());

        return "error";
    }
    @ExceptionHandler(AuthorizationServiceException.class)
    public String handleAuthorizationServiceException(
            AuthorizationServiceException ex, Model model) {

        model.addAttribute("error", ex.getMessage());

        return "error";
    }
    @ExceptionHandler(UserPrincipalNotFoundException.class)
    public String handleUserPrincipalNotFoundException(
            UserPrincipalNotFoundException ex, Model model) {

        model.addAttribute("error", ex.getMessage());

        return "error";
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public String handleUsernameNotFoundException(
            UsernameNotFoundException ex, Model model) {

        model.addAttribute("error", ex.getMessage());

        return "error";
    }

    @ExceptionHandler(PostNotFoundException.class)
    public String handlePostNotFoundException(
            UsernameNotFoundException ex, Model model) {

        model.addAttribute("error", ex.getMessage());

        return "error";
    }
    @ExceptionHandler(VerifyException.class)
    public String handleVerifyException(
            UsernameNotFoundException ex, Model model) {

        model.addAttribute("error", ex.getMessage());

        return "error";
    }
}
