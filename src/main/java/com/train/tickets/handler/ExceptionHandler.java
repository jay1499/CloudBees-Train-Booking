package com.train.tickets.handler;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class ExceptionHandler {
    @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
    public ResponseEntity<CustomProblem> handleException(Exception e) {
        CustomProblem problem = new CustomProblem("Internal Server Error", e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(problem);
    }

    @Getter
    static class CustomProblem {
        private final String title;
        private final String detail;

        public CustomProblem(String title, String detail) {
            this.title = title;
            this.detail = detail;
        }
    }
}


