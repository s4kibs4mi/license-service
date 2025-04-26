package ninja.sakib.licenseservice.configs;

import ninja.sakib.licenseservice.shared.security.SecurityService;
import ninja.sakib.licenseservice.shared.security.SecurityServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {

    @Bean
    public SecurityService securityService() {
        return new SecurityServiceImpl();
    }
}
