package jerik.the.dog.springsecurityjwt.resources;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloResource {

    @RequestMapping(value = "/hello")
    public String hello() {
        return "Hello World";
    }

}
