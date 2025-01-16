package coffeetime;

import coffeetime.infrastructure.JwtTokenFilter;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Import(JwtTokenFilter.class)
class CoffeetimeServerApplicationTests {

	@Test
	void contextLoads() {
	}

}
