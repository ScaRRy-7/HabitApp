package habitapp.jwt;

import habitapp.exceptions.UserIllegalRequestException;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class JwtRequestFilter implements Filter {

    @Autowired
    private JwtUtil jwtUtil;



    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
    throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String requestURI = httpRequest.getRequestURI();
        if (requestURI.equals("/habitapp/register")) {
            chain.doFilter(request, response);
            return;
        }
        final String authorizationHeader = httpRequest.getHeader("Authorization");

        String email = null;
        String jwt = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);
            email = jwtUtil.extractUsername(jwt);
        }

        if (email == null || jwt == null || !jwtUtil.validateToken(jwt, email)) {
            throw new UserIllegalRequestException(HttpServletResponse.SC_CONFLICT, "incorrect user data");
        }

        chain.doFilter(request, response);
    }


}
