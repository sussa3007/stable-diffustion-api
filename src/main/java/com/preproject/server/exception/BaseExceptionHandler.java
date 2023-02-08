package com.preproject.server.exception;


import com.preproject.server.constant.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.Optional;

// View Excption Handler
@ControllerAdvice
@Slf4j
public class BaseExceptionHandler {
    @ExceptionHandler
    public ModelAndView velidateException(
            MethodArgumentNotValidException e,
            HttpServletRequest request,
            HttpServletResponse response) {
        HttpStatus httpStatus = HttpStatus.valueOf(response.getStatus());
        ErrorCode errorCode = httpStatus.is4xxClientError() ? ErrorCode.BAD_REQUEST : ErrorCode.INTERNAL_ERROR;

        if (httpStatus == HttpStatus.OK) {
            httpStatus = HttpStatus.FORBIDDEN;
            errorCode = ErrorCode.BAD_REQUEST;
        }
        String referer = request.getHeader("REFERER");

        return new ModelAndView(
                "error",
                Map.of(
                        "statusCode", httpStatus.value(),
                        "errorCode", errorCode,
                        "message", errorCode.getMessage(e),
                        "msg", e.getMessage(),
                        "nextPage", referer
                ),
                httpStatus
        );
    }

    @ExceptionHandler
    public ModelAndView velidateBindingException(
            BindException e,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        HttpStatus httpStatus = HttpStatus.valueOf(response.getStatus());
        ErrorCode errorCode = httpStatus.is4xxClientError() ? ErrorCode.BAD_REQUEST : ErrorCode.INTERNAL_ERROR;
        String referer = request.getHeader("REFERER");
        String message;
        Optional<FieldError> fieldError = Optional.ofNullable(e.getFieldError());
        if (fieldError.isPresent()) {
            message = fieldError.get().getDefaultMessage();
        } else {
            message = "Incorrect information entered return to previous page";
        }
        if (httpStatus == HttpStatus.OK) {
            httpStatus = HttpStatus.FORBIDDEN;
            errorCode = ErrorCode.BAD_REQUEST;
        }
        return new ModelAndView(
                "error",
                Map.of(
                        "statusCode", httpStatus.value(),
                        "errorCode", errorCode,
                        "message", message,
                        "msg", message,
                        "nextPage", referer
                ),
                httpStatus
        );
    }

    @ExceptionHandler
    public ModelAndView general(
            GeneralException e,
            HttpServletRequest request) {
        ErrorCode errorCode = e.getErrorCode();
        String referer = request.getHeader("REFERER");
        if (e.getErrorCode().equals(ErrorCode.NOT_FOUND_MEMBER)) {
            referer = "/";
        }
        return new ModelAndView(
                "error",
                Map.of(
                        "statusCode", errorCode.getHttpStatus().value(),
                        "errorCode", errorCode,
                        "message", errorCode.getMessage(),
                        "msg", e.getMessage() + " Return to previous page",
                        "nextPage", referer
                ),
                errorCode.getHttpStatus()
        );
    }

    @ExceptionHandler
    public ModelAndView exception(Exception e, HttpServletResponse response) {
        HttpStatus httpStatus = HttpStatus.valueOf(response.getStatus());
        ErrorCode errorCode = httpStatus.is4xxClientError() ? ErrorCode.BAD_REQUEST : ErrorCode.INTERNAL_ERROR;

        if (httpStatus == HttpStatus.OK) {
            httpStatus = HttpStatus.FORBIDDEN;
            errorCode = ErrorCode.BAD_REQUEST;
        }
        return new ModelAndView(
                "error",
                Map.of(
                        "statusCode", httpStatus.value(),
                        "errorCode", errorCode,
                        "message", errorCode.getMessage(e)
                ),
                httpStatus
        );
    }


}