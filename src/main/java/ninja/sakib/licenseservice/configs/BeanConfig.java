package ninja.sakib.licenseservice.configs;

import ninja.sakib.licenseservice.daos.ContentDao;
import ninja.sakib.licenseservice.daos.UserDao;
import ninja.sakib.licenseservice.services.ContentService;
import ninja.sakib.licenseservice.services.ContentServiceImpl;
import ninja.sakib.licenseservice.services.UserService;
import ninja.sakib.licenseservice.services.UserServiceImpl;
import ninja.sakib.licenseservice.shared.security.JwtAuthFilterService;
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

    @Bean
    public UserService userService(UserDao userDao, SecurityService securityService) {
        return new UserServiceImpl(userDao, securityService);
    }

    @Bean
    public ContentService contentService(ContentDao contentDao) {
        return new ContentServiceImpl(contentDao);
    }

    @Bean
    public JwtAuthFilterService jwtAuthFilterService(SecurityService securityService, UserService userService) {
        return new JwtAuthFilterService(securityService, userService);
    }
}
