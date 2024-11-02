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
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "habitapp")
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
        return new UsersRepository(connectionManager(), LoggerFactory.getLogger(UsersService.class));
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
        return new HabitsRepository(connectionManager(), LoggerFactory.getLogger(HabitsService.class));
    }

    @Bean
    CompletedDaysRepository completedDaysRepository() {
        return new CompletedDaysRepository(connectionManager());
    }
}
