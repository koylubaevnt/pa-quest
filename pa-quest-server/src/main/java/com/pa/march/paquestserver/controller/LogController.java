package com.pa.march.paquestserver.controller;

import com.pa.march.paquestserver.message.dto.LogEntryDto;
import com.pa.march.paquestserver.service.LogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("api/log")
@CrossOrigin
@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
public class LogController {

    private static final Logger LOG = LoggerFactory.getLogger(LogController.class);

    @Autowired
    private LogService logService;

    @PostMapping
    public Boolean saveLog(HttpServletRequest request, @RequestBody LogEntryDto logEntry) {
        LOG.debug("logEntry={}", logEntry);
        Boolean saved = logService.saveLog(logEntry);
        LOG.debug("logEntry={} is {}", logEntry, saved ? "saved" : "not saved");
        return saved;
    }

}
