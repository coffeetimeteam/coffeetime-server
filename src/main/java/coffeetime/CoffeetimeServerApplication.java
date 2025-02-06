package coffeetime;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class CoffeetimeServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(CoffeetimeServerApplication.class, args);
	}
}
