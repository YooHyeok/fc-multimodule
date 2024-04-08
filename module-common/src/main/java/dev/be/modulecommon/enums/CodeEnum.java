package dev.be.modulecommon.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 사전에 클라이언트와 정의해 놓은 Return 메시지를 정의하는 클래스
 */
@Getter
@AllArgsConstructor
public enum CodeEnum {
    SUCCESS("0000", "SUCCESS"),
    UNKNOWN_ERROR("9999", "UNKNOWN_ERROR");

    private String code;
    private String message;
}