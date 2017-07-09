package org.thisarattr.auspost.addressvalidator.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.thisarattr.auspost.addressvalidator.services.CacheService;

@Configuration
public class AppConfig {

    @Autowired
    private CacheService cacheService;
    @Value("${security.jwt.secret}")
    private String secretKey;


    /*This bean is not really necessary spring will anyway pick up the datasource config in this instance.
    * But it will make it easier to read and understand the code. Or if you have custom db configuration. */
    @Bean
    @Primary
    @ConfigurationProperties("spring.datasource")
    public DataSourceProperties dataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    public FilterRegistrationBean jwtTokenFilter() {
        final FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setFilter(new JwtTokenFilter(cacheService, secretKey));
        registrationBean.addUrlPatterns("/admin/*");

        return registrationBean;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
