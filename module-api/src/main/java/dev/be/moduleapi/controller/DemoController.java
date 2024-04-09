package dev.be.moduleapi.controller;

import dev.be.moduleapi.response.CommonResponse;
import dev.be.modulecommon.enums.CodeEnum;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.be.moduleapi.service.DemoService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class DemoController {

    private final DemoService demoService;

    /**
     * Terminal - curl http://localhost:8080/save
     */
    @GetMapping("/save")
    public String save() {
        return demoService.save();
    }

    /**
     * Terminal - curl http://localhost:8080/find
     */
    @GetMapping("/find")
    public String find() {
        return demoService.find();
    }

    @GetMapping("/exception")
    public String exception() {
        return demoService.exception();
    }

}