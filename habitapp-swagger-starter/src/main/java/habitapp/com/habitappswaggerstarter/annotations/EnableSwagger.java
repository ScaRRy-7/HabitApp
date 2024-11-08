package habitapp.com.habitappswaggerstarter.annotations;

import habitapp.com.habitappswaggerstarter.config.SwaggerConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(SwaggerConfig.class)  // Импортируйте класс конфигурации Swagger
public @interface EnableSwagger {
}
