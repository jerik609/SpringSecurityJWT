package jerik.the.dog.springsecurityjwt.resources;

import jerik.the.dog.springsecurityjwt.dto.AuthenticationRequest;
import jerik.the.dog.springsecurityjwt.dto.AuthenticationResponse;
import jerik.the.dog.springsecurityjwt.jwt.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationResource {

    private AuthenticationManager authenticationManager;
    private UserDetailsService userDetailsService;
    private JwtUtil jwtUtil;

    @Autowired
    AuthenticationResource(
            AuthenticationManager authenticationManager,
            UserDetailsService userDetailsService,
            JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping(value = "/hello")
    public ResponseEntity<String> hello() {
        System.out.println("hello!!!");
        return new ResponseEntity<>("hello there!", HttpStatus.I_AM_A_TEAPOT);
    }

    // we must open this endpoint for non-authenticated users (so that they can authenticate)
    // TODO: can we attack the application by requesting many tokens? jwt as session tokens? what are session tokens ...
    //  (I think I know what they are - they must be remembered by the system, while jwt not)
    @PostMapping(value = "/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
        System.out.println("what?!");
        // here I can extract the login information from the authenticationRequest
        // to actually authenticate it, I need to get a handle of the authentication manager
        System.out.println(authenticationRequest);
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
            ); // if this fails, it throws an exception - we need to handle it
        } catch (BadCredentialsException e) {
            System.out.println("bad credentials");
            throw new Exception("Incorrect username or password", e);
        }

        // if we get here - authentication is successful
        // now we create the jwt out of user details - we need to fetch the user details from the user details service
        final var userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());

        // now the jtw util to get our jwt
        final var jwt = jwtUtil.generateToken(userDetails);

        // and then the response, containing the jwt
        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }

}
