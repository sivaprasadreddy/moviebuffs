package com.sivalabs.moviebuffs.web.advice;

import com.sivalabs.moviebuffs.exception.ApplicationException;
import com.sivalabs.moviebuffs.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.NativeWebRequest;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(value = ResourceNotFoundException.class)
    ResponseEntity<Problem> handleResourceNotFoundException(
            ResourceNotFoundException exception,
            NativeWebRequest request
    ) {
        log.error(exception.getLocalizedMessage(), exception);
        Problem problem = Problem.builder().withStatus(Status.NOT_FOUND).build();
        return ResponseEntity.ok(problem);
    }

    @ExceptionHandler(value = ApplicationException.class)
    ResponseEntity<Problem> handleApplicationException(ApplicationException exception,
                                                       NativeWebRequest request) {
        log.error(exception.getLocalizedMessage(), exception);
        Problem problem = Problem.builder().withStatus(Status.BAD_REQUEST).build();
        return ResponseEntity.ok(problem);
    }
}
