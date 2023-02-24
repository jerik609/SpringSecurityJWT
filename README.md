# Simple Demo of Spring Security with JWT

Based on: https://youtube.com/watch?v=X80nJ5T7YpE&feature=shares

# Steps

## Step 1
A /authenticate API endpoint
- Accepts user ID and password
- Returns JWT as response

Client logs in and gets a JWT. He keeps the JWT and will include it in the header of subsequent requests to our application.

So in our new request, we will provide the token in header "Authorization" with "Bearer <jwt_value>".

We need to tell spring security to check every request, if it contains a valid jwt in the Authoriation header, if there's a Bearer and blah.
Then it must take out the blah, treat it as jwt, extract the username out of it. Verify if its a valid jwt, if that's the case, the person is trustworthy,
and we can put the username in the security context.

## Step 2
Intercept all incoming requests.
- extract JWT from the header
- Validate and set in execution (security?) context

# Links
- https://curity.io/resources/learn/jwt-best-practices/
- https://backendstory.com/spring-security-how-to-replace-websecurityconfigureradapter/

# Notes
- the default security settings: org/springframework/boot/autoconfigure/security/servlet/SpringBootWebSecurityConfiguration.java
