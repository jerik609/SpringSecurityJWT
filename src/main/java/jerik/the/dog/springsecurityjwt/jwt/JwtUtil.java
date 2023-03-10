package jerik.the.dog.springsecurityjwt.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {

    private final Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    // https://siddharthac6.medium.com/json-web-token-jwt-the-right-way-of-implementing-with-node-js-65b8915d550e

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    // it seems that this guy extracts all claims - even some set of "built in" claims
    // e.g. username (subject) and expiration
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(SECRET_KEY).build()
                .parseClaimsJws(token)
                .getBody();
    }

    // compares expiration on token vs current time
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(Date.from(LocalDateTime.now().toInstant(ZoneOffset.UTC)));
    }

    // we want to create a token which has the user details (user info) baked in
    // at this moment, we just "bake in" the username - we keep the  claim empty for now
    // of course, we can pass in any claims we require (e.g. specific access details?)
    // every time we pass user details, we get a jwt for that user
    // when somebody authenticates, we can generate a token for them
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userDetails.getUsername()); // call actual create token for the username
    }

    private String createToken(Map<String, Object> claims, String subject) {
        final var creationTime = LocalDateTime.now();
        return Jwts.builder() // jwt api from
                .setClaims(claims) // set the claim (empty for now)
                .setSubject(subject) // subject = person who has/tries to authenticate
                .setIssuedAt(Date.from(creationTime.toInstant(ZoneOffset.UTC)))
                .setExpiration(Date.from(creationTime.plusHours(1).toInstant(ZoneOffset.UTC)))
                .signWith(SECRET_KEY) // actual signing of the jwt
                .compact(); // build and serialize to compact representation
        // note - no password is stored, just the claims (username (subject) is an implicit claim here)
        // and everything is signed by our private key
        // this "composition of claims" could have been created only by us, during the authentication process
        // there's no way this could have been forged (unless our key is compromised)
        // so when we get this back and verify the signature, we can trust it - and use it e.g. for authentication
        // (and much more - e.g. setting up the session - as we have all the claims we can trust)
    }

    // validates the token
    // I ASSUME: first validation is technical and is performed by spring - if the token has not been tampered with
    // we validate against provided user details & current time
    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

}
