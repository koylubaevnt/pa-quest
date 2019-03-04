package com.pa.march.paquestserver.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Mail {

    private String from;
    private String to;
    private String subject;
    private String content;
    private Map<String, Object> model;

}
