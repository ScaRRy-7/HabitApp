package habitapp.jwt;

import habitapp.exceptions.UserIllegalRequestException;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Фильтр для обработки JWT (JSON Web Token) запросов.
 * Проверяет наличие и валидность токена в заголовке авторизации.
 */
@Component
@Slf4j
public class JwtRequestFilter implements Filter {

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * Метод фильтрации запросов.
     * Проверяет наличие токена и его валидность.
     *
     * @param request  Запрос от клиента.
     * @param response Ответ для клиента.
     * @param chain    Цепочка фильтров для продолжения обработки запроса.
     * @throws IOException      В случае ошибки ввода-вывода.
     * @throws ServletException В случае ошибки сервлета.
     */
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String requestURI = httpRequest.getRequestURI();

        // Пропускаем запросы на регистрацию и вход
        if (requestURI.equals("/habitapp/register") || requestURI.equals("/habitapp/login")) {
            chain.doFilter(request, response);
            return;
        }

        final String authorizationHeader = httpRequest.getHeader("Authorization");

        String email = null;
        String jwt = null;

        // Проверяем наличие и формат токена
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);
            try {
                email = jwtUtil.extractEmail(jwt);
            } catch (ExpiredJwtException e) {
                throw new UserIllegalRequestException(HttpServletResponse.SC_BAD_REQUEST, "Токен истек: " + e.getMessage());
            }
        }

        // Проверяем валидность токена
        if (email == null || jwt == null || !jwtUtil.validateToken(jwt, email)) {
            throw new UserIllegalRequestException(HttpServletResponse.SC_CONFLICT, "Некорректные данные пользователя");
        }

        chain.doFilter(request, response);
    }
}