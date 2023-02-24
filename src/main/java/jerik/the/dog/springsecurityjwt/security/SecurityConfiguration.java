package jerik.the.dog.springsecurityjwt.security;

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

// https://backendstory.com/spring-security-how-to-replace-websecurityconfigureradapter/
// the default security settings: org/springframework/boot/autoconfigure/security/servlet/SpringBootWebSecurityConfiguration.java

@Configuration
//@EnableWebSecurity // enabled in spring boot automatically
//@EnableGlobalMethodSecurity(prePostEnabled = true)
class SecurityConfiguration {

    private final UserDetailsService myUserDetailsService;

    @Autowired
    public SecurityConfiguration(UserDetailsService myUserDetailsService) {
        this.myUserDetailsService = myUserDetailsService;
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
                .anyRequest().authenticated());
        http.csrf().disable();
        return http.build();
    }




                            //authorizationManagerRequestMatcherRegistry.

//
//
//
//
//
//
//                )
//                // disabling csrf since we won't use form login
//                .csrf().disable()
//                // giving permission to every request for /login endpoint
//                .authorizeRequests()//.antMatchers("/login").permitAll()
//                // for everything else, the user has to be authenticated
//                .anyRequest().authenticated()
//                // setting stateless session, because we choose to implement Rest API
//                .and().sessionManagement()
//                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//
//        // adding the custom filter before UsernamePasswordAuthenticationFilter in the filter chain
//        //http.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
//        return http.build();
//    }



//    @Autowired
//    private JwtTokenFilter jwtTokenFilter;

    // Injecting JWT custom authentication provider
//    @Autowired
//    JwtAuthenticationProvider customAuthenticationProvider;

    // Injecting Google custom authentication provider
//    @Autowired
//    GoogleCloudAuthenticationProvider googleCloudAuthenticationProvider;



    // adding our custom authentication providers
    // authentication manager will call these custom provider's
    // authenticate methods from now on.
//    @Autowired
//    void registerProvider(AuthenticationManagerBuilder auth) {
//        System.out.println("why is this even called?");
////        auth.authenticationProvider(customAuthenticationProvider)
////                .authenticationProvider(googleCloudAuthenticationProvider);
//    }

//    @Autowired
//    void booohooohoooo() {
//        System.out.println("REALLY?");
//    }


}
