package co.istad.mobilebanking.exception;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class ErrorDetailResponse <T>{
    private String code;
    private T description;
}

