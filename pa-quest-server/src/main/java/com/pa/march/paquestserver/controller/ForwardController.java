package com.pa.march.paquestserver.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@Slf4j
public class ForwardController {

    @RequestMapping("/**/{path:\\b(?!api\\b)[^\\.]+}")
    public String forward(HttpServletRequest request, HttpServletResponse response, @PathVariable("path") String path) {
        log.debug("path={}", path);
        return "forward:/";
    }
}
