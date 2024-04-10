package dev.be.moduleapi.service;

import dev.be.moduleapi.exception.CustomException;
import dev.be.modulecommon.domain.Member;
import dev.be.modulecommon.enums.CodeEnum;
import dev.be.modulecommon.repository.MemberRepository;
import dev.be.modulecommon.service.CommonDemoService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DemoService {
    /**
     * IntelliJ 우측 상단의 start debug/run configuration - edit - Active Profiles에 {env} 값을 지정한다.
     * java -jar 명령
     * 1. cd module
     * 2. java -jar "-Dspring.profiles.active=local" module-api-0.0.1-SNAPSHOT.jar
     * 2. java -jar "-Dspring.profiles.active=beta" module-api-0.0.1-SNAPSHOT.jar
     */
    @Value("${profile-name}")
    private String name;

    private final CommonDemoService demoService;
    private final MemberRepository memberRepository;

    public String save() {
//        System.out.println(CodeEnum.SUCCESS.getCode());
//        System.out.println(demoService.commenService());
        System.out.println("name = " + name);
        memberRepository.save(Member.builder()
                .name(Thread.currentThread().getName())
                .build());
        return "save";
    }

    public String find() {
        int size = memberRepository.findAll().size();
        System.out.println("DB size = " + size);
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
