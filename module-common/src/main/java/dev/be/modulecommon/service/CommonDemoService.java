package dev.be.modulecommon.service;

import org.springframework.stereotype.Service;

/**
 * 컴포넌트 스캔
 * ModuleApiApplication의 위치가 굉장히 중요하다.
 * Application클래스의 main을 실행시키는 패키지를 기준으로 Bean스캔 즉, Component 스캔이 일어난다.
 * 즉, 해당 Application파일은 dev.be라는 공통 패키지 하위의 moduleapi패키지에 있기 때문에
 * modulecommon 패키지에 있는 컴포넌트를 스캔할 수 없어 Bean 객체 주입을 받을수 없게 된다.
 *
 */
@Service
public class CommonDemoService {

    public String commenService() {
        return "commenService";
    }
}