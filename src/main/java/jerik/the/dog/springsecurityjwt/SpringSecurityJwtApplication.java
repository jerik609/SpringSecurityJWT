package jerik.the.dog.springsecurityjwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;

@SpringBootApplication
public class SpringSecurityJwtApplication {

	private final ApplicationContext applicationContext;

	@Autowired
	SpringSecurityJwtApplication(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	public static void main(String[] args) {
		SpringApplication.run(SpringSecurityJwtApplication.class, args);
	}

	@Bean
	public CommandLineRunner runner() {
		return args -> {
			Arrays.stream(applicationContext.getBeanDefinitionNames())
					.filter(name -> !name.toLowerCase().contains("spring"))
					.forEach(System.out::println);
		};
	}

}
