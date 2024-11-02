package habitapp;

import com.fasterxml.jackson.databind.ObjectMapper;
import habitapp.dbconnection.ConnectionManager;
import habitapp.repositories.CompletedDaysRepository;
import habitapp.repositories.HabitsRepository;
import habitapp.repositories.UsersRepository;
import habitapp.services.HabitsService;
import habitapp.services.UsersService;
import habitapp.validators.UserValidator;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePropertySource;
import org.springframework.util.PropertyPlaceholderHelper;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.util.Properties;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "habitapp")
@PropertySource("classpath:application.yml")
public class AppConfig {

    @Bean
    @Scope("prototype")
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public UsersService usersService() {
        return new UsersService(userValidator(), usersRepository());
    }

    @Bean
    public UserValidator userValidator() {
        return new UserValidator();
    }

    @Bean
    public UsersRepository usersRepository() {
        return new UsersRepository(connectionManager());
    }

    @Bean
    ConnectionManager connectionManager() {
        return new ConnectionManager();
    }

    @Bean
    public HabitsService habitsService() {
        return new HabitsService(habitsRepository(), usersRepository(), completedDaysRepository());
    }

    @Bean
    HabitsRepository habitsRepository() {
        return new HabitsRepository(connectionManager());
    }

    @Bean
    CompletedDaysRepository completedDaysRepository() {
        return new CompletedDaysRepository(connectionManager());
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        PropertySourcesPlaceholderConfigurer configurer = new PropertySourcesPlaceholderConfigurer();
        YamlPropertiesFactoryBean yaml = new YamlPropertiesFactoryBean();
        yaml.setResources(new ClassPathResource("application.yml"));
        configurer.setProperties(yaml.getObject());
        return configurer;
    }
}
