package jerik.the.dog.springsecurityjwt.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jerik.the.dog.springsecurityjwt.jwt.JwtUtil;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

// intercepts every request (once) and examines the header
public class JwtRequestFilter extends OncePerRequestFilter {

    // takes a request, response and filter chain - can pass the request to next filter in chain or end the request
    // examines the incoming request for jwt in header
    // if it finds a valid jwt, it will get the username and user details out of the user details service
    // it will save the user details in the security context

    private final UserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

    }
}
