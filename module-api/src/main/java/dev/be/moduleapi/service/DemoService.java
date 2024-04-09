package dev.be.moduleapi.service;

import dev.be.moduleapi.exception.CustomException;
import dev.be.modulecommon.enums.CodeEnum;
import dev.be.modulecommon.service.CommonDemoService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DemoService {

    private final CommonDemoService demoService;
    public String save() {
        System.out.println(CodeEnum.SUCCESS.getCode());
        System.out.println(demoService.commenService());
        return "save";
    }

    public String find() {
        return "find";
    }

    public String exception() {
        if (true) {
//            throw new RuntimeException(CodeEnum.UNKNOWN_ERROR); // 에러 코드나 에러 메시지 등 여러 값을 더 추가하고싶으나 추가할수없다.
            throw new CustomException(CodeEnum.UNKNOWN_ERROR);
        }
        return "excetpion";
    }
}
