package co.istad.mobilebanking.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class ApiException {

    //return all errors
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    ErrorResponse<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {

        List<Map<String, String>> errorDetails=new ArrayList<>();

        e.getFieldErrors()
                .forEach(fieldError-> {
                            Map<String, String> errorDetialMap=new HashMap<>();
                            errorDetialMap.put("field", fieldError.getField());
                            errorDetialMap.put("message", fieldError.getDefaultMessage());
                            errorDetails.add(errorDetialMap);
                        }
                        );
        ErrorDetailResponse<?> errorDetailResponse=ErrorDetailResponse.builder()
                .code(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .description(errorDetails)
                .build();
        return ErrorResponse.builder()
                .error(errorDetailResponse)
                .build();
    }

    //return one error
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
//        ErrorDetailResponse<?> errorDetailResponse = ErrorDetailResponse.builder()
//                .code(e.getStatusCode().toString())
//                .description(e.getBindingResult().getFieldError().getDefaultMessage())
//                .build();
//        return new ResponseEntity<>(ErrorResponse.builder()
//                .error(errorDetailResponse)
//                .build(), e.getStatusCode());
//    }
}
