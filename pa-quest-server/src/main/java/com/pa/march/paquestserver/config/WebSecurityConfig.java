package com.pa.march.paquestserver.config;

import com.pa.march.paquestserver.jwt.JwtAuthEntryPoint;
import com.pa.march.paquestserver.jwt.JwtAuthTokenFilter;
import com.pa.march.paquestserver.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        prePostEnabled = true
)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Autowired
    private JwtAuthEntryPoint unauthorizedHandler;

    @Bean
    public JwtAuthTokenFilter authenticationJwtTokenFilter() {
        return new JwtAuthTokenFilter();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        final CorsConfiguration configuration = new CorsConfiguration();
        //configuration.setAllowedOrigins(ImmutableList.of("http://localhost:8080","http://localhost:8084"));
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowCredentials(true);
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setExposedHeaders(Arrays.asList("Content-Type", "Content-Length"));

        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
    @Override
    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.headers().frameOptions().sameOrigin();

        http.cors().and().csrf().disable()
                .authorizeRequests()
                    .anyRequest()
                        .permitAll()
                    .antMatchers("/api/**")
                        .authenticated()
                    .antMatchers("/api/auth/**")
                        .permitAll()
                .and()
                    .exceptionHandling()
                        .authenticationEntryPoint(unauthorizedHandler)
                .and()
                    .sessionManagement()
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        /*http.cors().and().csrf().disable()
                .authorizeRequests()
                    .antMatchers("/", "/assets/**", "/h2-console/**", "/api/auth/**")
                        .permitAll()
                    .antMatchers("/*.js", "/*.css", "/*.jpg", "/*.ico", "/*.png")
                        .permitAll()
                .anyRequest()
                    .authenticated()
                .and()
                    .exceptionHandling()
                        .authenticationEntryPoint(unauthorizedHandler)
                .and()
                    .sessionManagement()
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS);*/

        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
    }

}
