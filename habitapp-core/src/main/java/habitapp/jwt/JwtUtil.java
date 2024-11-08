package habitapp.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Утилита для работы с JWT (JSON Web Token).
 * Предоставляет методы для генерации, валидации и извлечения информации из токенов.
 */
@Component
public class JwtUtil {

    @Value("${security.jwt.secret}")
    private String SECRET_KEY;

    @Value("${security.jwt.expiration}")
    private long EXPIRATION_TIME;

    /**
     * Генерирует JWT токен для указанного email.
     *
     * @param email Адрес электронной почты пользователя.
     * @return Сгенерированный токен.
     */
    public String generateToken(String email) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, email);
    }

    /**
     * Создает JWT токен с указанными данными.
     *
     * @param claims Данные, которые будут включены в токен.
     * @param subject Тема токена (например, email пользователя).
     * @return Сгенерированный токен.
     */
    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    /**
     * Проверяет валидность токена.
     *
     * @param token Токен для проверки.
     * @param email Адрес электронной почты пользователя, которому принадлежит токен.
     * @return true, если токен валиден; false в противном случае.
     */
    public boolean validateToken(String token, String email) {
        final String username = extractEmail(token);
        return (username.equals(email) && !isTokenExpired(token));
    }

    /**
     * Извлекает адрес электронной почты из токена.
     *
     * @param token Токен, из которого нужно извлечь email.
     * @return Адрес электронной почты.
     */
    public String extractEmail(String token) {
        return extractAllClaims(token).getSubject();
    }

    /**
     * Извлекает все данные (claims) из токена.
     *
     * @param token Токен, из которого нужно извлечь данные.
     * @return Объект Claims, содержащий данные токена.
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
    }

    /**
     * Проверяет, истек ли токен.
     *
     * @param token Токен для проверки.
     * @return true, если токен истек; false в противном случае.
     */
    private boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }
}