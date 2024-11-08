package habitapp;

import habitapp.com.habitappswaggerstarter.annotations.EnableSwagger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableSwagger // Включение Swagger
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
