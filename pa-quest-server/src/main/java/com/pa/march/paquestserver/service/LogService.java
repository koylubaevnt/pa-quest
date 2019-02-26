package com.pa.march.paquestserver.service;

import com.pa.march.paquestserver.domain.LogEntry;
import com.pa.march.paquestserver.domain.LogLevel;
import com.pa.march.paquestserver.message.dto.LogEntryDto;
import com.pa.march.paquestserver.repository.LogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service("logService")
public class LogService {

    @Autowired
    private LogRepository logRepository;

    public Boolean saveLog(LogEntryDto logEntryDto) {
        List<String> extraInfos = logEntryDto.getExtraInfo().stream()
                .map(e -> e.toString())
                .collect(Collectors.toList());

        LogEntry logEntry = new LogEntry();
        logEntry.setEntryDate(logEntryDto.getEntryDate());
        logEntry.setMessage(logEntryDto.getMessage());
        logEntry.setLevel(LogLevel.fromValue(logEntryDto.getLevel()));
        logEntry.setExtraInfo(extraInfos);

        if(logRepository.save(logEntry) != null) {
            return true;
        } else {
            return false;
        }
    };

}
