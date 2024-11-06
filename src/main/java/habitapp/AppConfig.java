package habitapp;

import com.fasterxml.jackson.databind.ObjectMapper;
import habitapp.dbconnection.ConnectionManager;
import habitapp.jwt.JwtUtil;
import habitapp.repositories.CompletedDaysRepositoryImpl;
import habitapp.repositories.HabitsRepositoryImpl;
import habitapp.repositories.UsersRepositoryImpl;
import habitapp.services.HabitsServiceImpl;
import habitapp.services.UsersServiceImpl;
import habitapp.validators.UserValidator;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "habitapp")
@PropertySource("classpath:application.yml")
@Import(SwaggerConfig.class)
public class AppConfig {

    @Bean
    @Scope("prototype")
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public UsersServiceImpl usersServiceImpl() {
        return new UsersServiceImpl(userValidator(), usersRepositoryImpl(), jwtUtil());
    }

    @Bean
    public UserValidator userValidator() {
        return new UserValidator();
    }

    @Bean
    public UsersRepositoryImpl usersRepositoryImpl() {
        return new UsersRepositoryImpl(connectionManager());
    }

    @Bean
    ConnectionManager connectionManager() {
        return new ConnectionManager();
    }

    @Bean
    public HabitsServiceImpl habitsServiceImpl() {
        return new HabitsServiceImpl(usersRepositoryImpl(), habitsRepositoryImpl(), completedDaysRepositoryImpl(), jwtUtil());
    }

    @Bean
    HabitsRepositoryImpl habitsRepositoryImpl() {
        return new HabitsRepositoryImpl(connectionManager());
    }

    @Bean
    CompletedDaysRepositoryImpl completedDaysRepositoryImpl() {
        return new CompletedDaysRepositoryImpl(connectionManager());
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        PropertySourcesPlaceholderConfigurer configurer = new PropertySourcesPlaceholderConfigurer();
        YamlPropertiesFactoryBean yaml = new YamlPropertiesFactoryBean();
        yaml.setResources(new ClassPathResource("application.yml"));
        configurer.setProperties(yaml.getObject());
        return configurer;
    }

    @Bean
    JwtUtil jwtUtil() {
        return new JwtUtil();
    }
}
