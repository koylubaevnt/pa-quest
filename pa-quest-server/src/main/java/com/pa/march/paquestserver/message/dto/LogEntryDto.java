package com.pa.march.paquestserver.message.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LogEntryDto {

    private LocalDateTime entryDate;
    private String message;
    private Integer level;
    private List<Object> extraInfo;
}
