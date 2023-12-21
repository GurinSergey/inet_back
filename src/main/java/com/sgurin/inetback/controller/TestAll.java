package com.sgurin.inetback.controller;

import com.sgurin.inetback.enums.Language;
import com.sgurin.inetback.exeption.CustomWarningException;
import com.sgurin.inetback.response.GenericResponse;
import com.sgurin.inetback.service.LocaleMessageService;
import com.sgurin.inetback.utils.IPHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/test")
public class TestAll {
    private final LocaleMessageService localeMessageService;

    @Autowired
    public TestAll(LocaleMessageService localeMessageService) {
        this.localeMessageService = localeMessageService;
    }

    @GetMapping(value = "/1")
    public ResponseEntity<GenericResponse<String>> test1() {
        throw new CustomWarningException(localeMessageService.localizedMessage("test"));
    }

    @GetMapping(value = "/2")
    public ResponseEntity<GenericResponse<String>> test2() {
        throw new CustomWarningException(localeMessageService.localizedMessage("test", Language.UK));
    }

    @GetMapping(value = "/3")
    public ResponseEntity<GenericResponse<String>> test3(HttpServletRequest request) {
        String ip = IPHandler.extractIp(request);
        return GenericResponse.success(ip);
    }

    @GetMapping(value = "/4")
    public ResponseEntity<GenericResponse<Boolean>> test4(@RequestParam String str) {
        str = null;

        return GenericResponse.success(str.equals(""));
    }
}
