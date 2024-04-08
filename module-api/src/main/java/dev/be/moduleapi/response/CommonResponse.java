package dev.be.moduleapi.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import dev.be.modulecommon.enums.CodeEnum;
import lombok.*;

/**
 * 클라이언트와 서버가 통신하는 공통 JSON 규격으로 사용한다.
 * 각 API마다 공통, 통일화 된 필드에 대한 Response객체
 * @JsonInclude(JsonInclude.Include.NON_NULL) JSON 직렬화 포함기능
 * JsonInclude.Include.NON_NULL: Value가 Null이 아닌 대상 Field만 Json직렬화시 포함시키겠다.
 */
@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
public class CommonResponse<T> {
    private String returnCode; // 성공 실패 등
    private String returnMessage;
    private T info;// 궁극적으로 특정 응답-결과 값(객체)를 담기 위한 Generic 사용

    /**
     * 응답코드값만 결과값으로 반환
     * @param codeEnum
     */
    public CommonResponse(CodeEnum codeEnum) {
        setReturnCode(codeEnum.getCode());
        setReturnMessage(codeEnum.getMessage());
    }

    /**
     * 결과객체값만 결과값으로 반환
     * 결과객체를 넘겼다는 것은 성공으로 간주해도 될테니,
     * 성공코드를 응답코드로 고정하여 반환
     * @param info
     */
    public CommonResponse(T info) {
        setReturnCode(CodeEnum.SUCCESS.getCode());
        setReturnMessage(CodeEnum.SUCCESS.getMessage());
        setInfo(info);
    }

    /**
     * 응답코드, 결과객체 함께 반환
     * @param codeEnum
     * @param info
     */
    public CommonResponse(CodeEnum codeEnum, T info) {
        setReturnCode(codeEnum.getCode());
        setReturnMessage(codeEnum.getMessage());
        setInfo(info);
    }
}
