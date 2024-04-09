package dev.be.moduleapi.exceptionhandler;

import dev.be.moduleapi.exception.CustomException;
import dev.be.moduleapi.response.CommonResponse;
import dev.be.modulecommon.enums.CodeEnum;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @RestControllerAdvice 여러 컨트롤러에 대해 전역적으로 ExceptionHandler를 적용해 준다.
 * 비즈니스 계층에서 Throw한 Exception을 현재 클래스에서 잡는다.
 * @ExceptionHandler 특정 Exception을 지정, 예외 발생시 핸들링 후 반환되는 값을 JSON 직렬화 후 View로 반환
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(CustomException.class) // 어떤 Exception Class만 핸들링할 지 정의해준다.
    public CommonResponse handlerCustomException(CustomException e) {
        return CommonResponse.builder()
                .returnCode(e.getReturnCode())
                .returnMessage(e.getReturnMessage())
                .build();
    }

    /**
     * CustomException으로 Wraping하지 못하고 놓친것에 대한 Exception들을 핸들링 한다.
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    public CommonResponse handlerException(Exception e) {
        return CommonResponse.builder()
                .returnCode(CodeEnum.UNKNOWN_ERROR.getCode())
                .returnMessage(CodeEnum.UNKNOWN_ERROR.getMessage())
                .build();
    }
}
