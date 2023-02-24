package jerik.the.dog.springsecurityjwt.security;

import jerik.the.dog.springsecurityjwt.filters.JwtRequestFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// https://backendstory.com/spring-security-how-to-replace-websecurityconfigureradapter/
// the default security settings: org/springframework/boot/autoconfigure/security/servlet/SpringBootWebSecurityConfiguration.java

@Configuration
//@EnableWebSecurity // enabled in spring boot automatically
//@EnableGlobalMethodSecurity(prePostEnabled = true)
class SecurityConfiguration {

    private final UserDetailsService myUserDetailsService;
    private final JwtRequestFilter jwtRequestFilter;

    @Autowired
    public SecurityConfiguration(UserDetailsService myUserDetailsService, JwtRequestFilter jwtRequestFilter) {
        this.myUserDetailsService = myUserDetailsService;
        this.jwtRequestFilter = jwtRequestFilter;
    }

    // configure the authentication manager builder
    @Autowired
    void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(myUserDetailsService);
    }

    @Bean
    AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    // password encoder - not encoded for our test
    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(registry -> registry
                .requestMatchers(HttpMethod.POST, "/authenticate").permitAll()
                .requestMatchers(HttpMethod.GET, "/hello").authenticated() // I'm being explicit here, just for the sake of completeness (no other reason)
                .anyRequest().authenticated());
        // first we need to tell spring security not to manage sessions (we will do it using our jwt implementation)
        // application will not remember anything
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        // we need to add our filter to the chain, not replace it (interject it in the filter chain)
        // we want to add our new filter before the username/password authentication
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
        http.csrf().disable();
        return http.build();
    }

}
