package jerik.the.dog.springsecurityjwt.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jerik.the.dog.springsecurityjwt.jwt.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
// intercepts every request (once) and examines the header
public class JwtRequestFilter extends OncePerRequestFilter {

    // takes a request, response and filter chain - can pass the request to next filter in chain or end the request
    // examines the incoming request for jwt in header
    // if it finds a valid jwt, it will get the username and user details out of the user details service
    // it will save the user details in the security context

    private final UserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;

    @Autowired
    JwtRequestFilter(UserDetailsService userDetailsService, JwtUtil jwtUtil) {
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        final String authorizationHeader = request.getHeader("Authorization");

        String username = null;
        String jwt = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7); // reading in everything after "Bearer " (note the space)
            username = jwtUtil.extractUsername(jwt);
        }

        // if username is valid and no user is authenticated yet
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            final var userDetails = userDetailsService.loadUserByUsername(username);

            // now validate the token with the user details
            // this checks if jwt is valid for the given user
            if (jwtUtil.validateToken(jwt, userDetails)) { // we're getting the username twice, I assume this is done differently in production

                // this token is a default thing spring security uses for managing authentication
                final var usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                // all this ^^^ would be performed by spring security under the hood, but I took over for my jwt
                // implementation so I need to perform it on my own (TODO: learn the details)
            }

        }
        // now call subsequent filters - my job here is done
        filterChain.doFilter(request, response);
        // now we need to go to security configuration and tell it to use this chain we just prepared
    }
}
