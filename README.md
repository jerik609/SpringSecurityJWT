# Simple Demo of Spring Security with JWT

Based on: https://youtube.com/watch?v=X80nJ5T7YpE&feature=shares

# Steps

## Step 1
A /authenticate API endpoint
- Accepts user ID and password
- Returns JWT as response

Client logs in and gets a JWT. He keeps the JWT and will include it in the header of subsequent requests to our application.

## Step 2
...



# Links
- https://curity.io/resources/learn/jwt-best-practices/
- https://backendstory.com/spring-security-how-to-replace-websecurityconfigureradapter/

# Notes
- the default security settings: org/springframework/boot/autoconfigure/security/servlet/SpringBootWebSecurityConfiguration.java
