package com.preproject.server.constant;

import com.preproject.server.exception.GeneralException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.Predicate;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    OK(200, HttpStatus.OK, "Ok"),

    BAD_REQUEST(400, HttpStatus.BAD_REQUEST, "Bad Request"),
    SPRING_BAD_REQUEST(400, HttpStatus.BAD_REQUEST, "Spring-detected bad request"),
    VALIDATION_ERROR(400, HttpStatus.BAD_REQUEST, "Validation error"),
    NOT_FOUND(404, HttpStatus.NOT_FOUND, "Requested resource is not found"),
    NOT_FOUND_COOKIE(404, HttpStatus.NOT_FOUND, "Not Found Cookie"),
    NOT_FOUND_MEMBER(404, HttpStatus.NOT_FOUND, "Not Found Member"),
    FAILED_LOGIN(404, HttpStatus.NOT_FOUND, "Login failed Check Your ID & PW"),
    MEMBER_EXISTS(400, HttpStatus.BAD_REQUEST, "Member Exists"),
    NICKNAME_EXISTS(400, HttpStatus.BAD_REQUEST, "Nickname Exists"),
    EXCEEDING_MAXIMUM_OCCUPANCY(404, HttpStatus.NOT_FOUND, "Exceeding maximum occupancy"),

    REQUEST_DELETE_PLACE_DENIED(400, HttpStatus.BAD_REQUEST, "Request Place Delete Denied"),

    INTERNAL_ERROR(500, HttpStatus.INTERNAL_SERVER_ERROR, "Internal error"),
    SPRING_INTERNAL_ERROR(500, HttpStatus.INTERNAL_SERVER_ERROR, "Spring-detected internal error"),
    DATA_ACCESS_ERROR(500, HttpStatus.INTERNAL_SERVER_ERROR, "Data access error"),
    EXPIRED_ACCESS_TOKEN(403, HttpStatus.FORBIDDEN, "EXPIRED ACCESS TOKEN"),
    EXPIRED_REFRESH_TOKEN(403, HttpStatus.FORBIDDEN, "EXPIRED REFRESH TOKEN"),
    UNAUTHORIZED_ACCESS(400, HttpStatus.BAD_REQUEST, "UNAUTHORIZED ACCESS"),
    ACCESS_DENIED(403, HttpStatus.FORBIDDEN, "Access Denied"),
    FREE_LIMIT_EXCEEDED(402, HttpStatus.PAYMENT_REQUIRED, "Free limit exceeded"),
    UNAUTHORIZED(401, HttpStatus.UNAUTHORIZED, "Invalid key or permission"),
    POST_ACCESS_DENIED(403, HttpStatus.FORBIDDEN, "Post Access Denied"),
    OAUTH2_ACCESS_ERROR(500, HttpStatus.INTERNAL_SERVER_ERROR, "OAuth2 Access Error");

    private final Integer code;
    private final HttpStatus httpStatus;
    private final String message;


    public static ErrorCode valueOf(HttpStatus httpStatus) {
        if (httpStatus == null) {
            throw new GeneralException("HttpStatus is null.");
        }

        return Arrays.stream(values())
                .filter(errorCode -> errorCode.getHttpStatus() == httpStatus)
                .findFirst()
                .orElseGet(() -> {
                    if (httpStatus.is4xxClientError()) {
                        return ErrorCode.BAD_REQUEST;
                    } else if (httpStatus.is5xxServerError()) {
                        return ErrorCode.INTERNAL_ERROR;
                    } else {
                        return ErrorCode.OK;
                    }
                });
    }

    public String getMessage(Throwable e) {
        return this.getMessage(this.getMessage() + " - " + e.getMessage());
    }

    public String getMessage(String message) {
        return Optional.ofNullable(message)
                .filter(Predicate.not(String::isBlank))
                .orElse(this.getMessage());
    }

    @Override
    public String toString() {
        return String.format("%s (%d)", this.name(), this.getCode());
    }

}
