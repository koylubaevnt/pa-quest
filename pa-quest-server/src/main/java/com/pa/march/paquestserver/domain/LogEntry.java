package com.pa.march.paquestserver.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "log_from_web")
@Data
@NoArgsConstructor
public class LogEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private LocalDateTime entryDate;
    private String message;
    private LogLevel level;
    @ElementCollection
    @CollectionTable(
            name="log_from_web_extra",
            joinColumns=@JoinColumn(name="log_id")
    )
    private List<String> extraInfo;
}