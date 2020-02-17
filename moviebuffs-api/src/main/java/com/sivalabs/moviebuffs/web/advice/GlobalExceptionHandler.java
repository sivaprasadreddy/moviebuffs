package com.sivalabs.moviebuffs.web.advice;

import com.sivalabs.moviebuffs.exception.ApplicationException;
import com.sivalabs.moviebuffs.exception.BadRequestException;
import com.sivalabs.moviebuffs.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.NativeWebRequest;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;
import org.zalando.problem.spring.web.advice.ProblemHandling;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    private ExceptionTranslator translator = new ExceptionTranslator();

    @ExceptionHandler(value = ResourceNotFoundException.class)
    ResponseEntity<Problem> handleResourceNotFoundException(
            ResourceNotFoundException exception,
            NativeWebRequest request
    ) {
        log.error(exception.getLocalizedMessage(), exception);
        return translator.create(Status.NOT_FOUND, exception, request);
    }

    @ExceptionHandler(value = ApplicationException.class)
    ResponseEntity<Problem> handleApplicationException(ApplicationException exception,
                                                       NativeWebRequest request) {
        log.error(exception.getLocalizedMessage(), exception);
        return translator.create(Status.BAD_REQUEST, exception, request);
    }

    @ExceptionHandler(value = BadRequestException.class)
    ResponseEntity<Problem> handleBadRequestException(BadRequestException exception,
                                                       NativeWebRequest request) {
        log.error(exception.getLocalizedMessage(), exception);
        return translator.create(Status.BAD_REQUEST, exception, request);
    }
}

class ExceptionTranslator implements ProblemHandling { }
