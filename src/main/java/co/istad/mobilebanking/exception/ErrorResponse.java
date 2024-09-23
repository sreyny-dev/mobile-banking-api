package co.istad.mobilebanking.exception;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

//using generic making datatype dynamic
@Setter
@Getter
@Builder
public class ErrorResponse<T> {
    private T error;
}
