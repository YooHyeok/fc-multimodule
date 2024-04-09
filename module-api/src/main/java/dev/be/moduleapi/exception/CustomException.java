package dev.be.moduleapi.exception;

import dev.be.modulecommon.enums.CodeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Exception을 Custom하게 Wrapping해서 상황에 맞게 핸들링한 후 사용자에게 던지기위한 클래스이다. <br/>
 * 실제 IT 회사 현업에서는 위와 같은 방식으로 Exception을 핸들링한다. <br/>
 * 일반적으로 throw new RuntimeException(CodeEnum.UNKNOWN_ERROR); <br/>
 * 과 같이 RuntimeE 하게되면 에러 코드나 에러 메시지 등 여러 값을 더 추가하고싶으나 추가할수없다. <br/>
 * 절대 언어나 프레임워크에서 제공해주는 아래와 같이 row한 방식에 해당하는 Exception을 던져주지 않는다. <br/>
 * if (exception == IllegalArgumentException.class) { <br/>
 *      throw new IllegalArgumentException("~~"); <br/>
 * }
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomException extends RuntimeException {
    private String returnCode;
    private String returnMessage;

    public CustomException(CodeEnum codeEnum) {
        super(codeEnum.getMessage()); // RuntimeException도 Exception을 상속받으면서 부모에게 message를 넘겨준다.
        setReturnCode(codeEnum.getCode());
        setReturnMessage(codeEnum.getMessage());
    }
}
