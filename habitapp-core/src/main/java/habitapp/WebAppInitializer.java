package habitapp;

import habitapp.jwt.JwtRequestFilter;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRegistration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

/**
 * Инициализация веб-приложения.
 * Настраивает контекст приложения и регистрирует фильтры.
 */
@Configuration
public class WebAppInitializer implements WebApplicationInitializer {

    @Autowired
    JwtRequestFilter jwtRequestFilter;

    /**
     * Метод, вызываемый при запуске приложения.
     * Настраивает контекст приложения и регистрирует диспетчер сервлетов.
     *
     * @param servletContext Контекст сервлета.
     * @throws ServletException В случае ошибки при инициализации.
     */
    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        // Создание и настройка контекста приложения
        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
        context.register(AppConfig.class);

        // Создание и регистрация диспетчера сервлетов
        DispatcherServlet dispatcherServlet = new DispatcherServlet(context);
        ServletRegistration.Dynamic registration = servletContext.addServlet("dispatcher", dispatcherServlet);
        registration.setLoadOnStartup(1);
        registration.addMapping("/");
    }

    /**
     * Регистрация фильтра JWT.
     *
     * @return Объект FilterRegistrationBean для фильтра JWT.
     */
    @Bean
    public FilterRegistrationBean<JwtRequestFilter> jwtFilter() {
        FilterRegistrationBean<JwtRequestFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(jwtRequestFilter);
        registrationBean.addUrlPatterns("/*"); // Применение фильтра ко всем URL
        return registrationBean;
    }
}