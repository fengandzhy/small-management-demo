package org.frank.springboot.small.management.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * 这里一定要加上csrf().disable()
     * 从Spring Security3.2 开始，默认会启用CSRF防护。它将会拦截状态变化的请求（例如：非GET、HEAD、OPTIONS和TRACE的请求）并检查CSRF token。
     * */
    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http.authorizeRequests()
                .anyRequest().permitAll()
                .and()
                .headers().frameOptions().sameOrigin()
                .and()
                .csrf()
                .disable();
    }
}
