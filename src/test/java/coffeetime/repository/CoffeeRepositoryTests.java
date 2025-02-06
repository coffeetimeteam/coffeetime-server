package coffeetime.repository;

import static org.assertj.core.api.Assertions.assertThat;

import coffeetime.domain.Coffee;
import coffeetime.domain.Member;
import coffeetime.domain.type.CoffeeType;
import coffeetime.domain.type.LocationType;
import coffeetime.domain.type.PriceType;
import coffeetime.domain.type.SizeType;
import coffeetime.domain.type.TasteType;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class CoffeeRepositoryTests {

	@Mock
	private CoffeeRepository coffeeRepository;

	@Autowired
	private MemberRepository memberRepository;

	private Member testMember;
	private Coffee testCoffee;

	@BeforeEach
	void setUp() {
		testMember = memberRepository.save(Member.createUserFromForm("test@email.com", "password"));

		testCoffee = Coffee.create(
			testMember,
			LocalDate.now(),
			LocalTime.now(),
			LocationType.HOME,
			CoffeeType.COFFEE,
			SizeType.MEDIUM,
			TasteType.DELICIOUS,
			PriceType.AVERAGE,
			5,
			null
		);
	}

	@Test
	public void testFindDailyCoffees() {
		// given
		coffeeRepository.save(testCoffee);

		// when
		List<Coffee> coffees = coffeeRepository.findCoffeesByDate(testMember, LocalDate.now());

		// then
		assertThat(coffees).isNotEmpty();
		assertThat(coffees.get(0).getMember().getId()).isEqualTo(testMember.getId());
		assertThat(coffees.get(0).getRememberDate()).isEqualTo(LocalDate.now());
	}
} 