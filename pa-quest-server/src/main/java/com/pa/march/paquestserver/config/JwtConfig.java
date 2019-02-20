package com.pa.march.paquestserver.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "com.pa.8march.jwt")
@Data
public class JwtConfig {

    private String tokenSigningKey;

    private int tokenExpirationTime;

    private int refreshTokenExpTime;

    private String tokenIssuer;

}
