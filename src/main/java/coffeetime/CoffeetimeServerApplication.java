package coffeetime;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class CoffeetimeServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(CoffeetimeServerApplication.class, args);

		HikariDataSource dataSource = new HikariDataSource();
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			if (!dataSource.isClosed()) {
				dataSource.close();
				System.out.println("HikariDataSource closed on JVM shutdown.");
			}
		}));
	}
}
