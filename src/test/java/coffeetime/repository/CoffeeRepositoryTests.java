package coffeetime.repository;

import static org.assertj.core.api.Assertions.assertThat;

import coffeetime.domain.Coffee;
import coffeetime.domain.type.CoffeeType;
import coffeetime.domain.type.LocationType;
import coffeetime.domain.type.LoginType;
import coffeetime.domain.type.PriceType;
import coffeetime.domain.type.RoleType;
import coffeetime.domain.type.SizeType;
import coffeetime.domain.type.TasteType;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class CoffeeRepositoryTests {

	@Autowired
	private CoffeeRepository coffeeRepository;

	@Autowired
	private MemberRepository memberRepository;

	private UUID testMemberId;
	private Coffee testCoffee;

	@BeforeEach
	void setUp() {
		MemberEntity savedMember = memberRepository.save(
			MemberEntity.create(
				"test@email.com",
				LoginType.EMAIL,
				"tester",
				"password",
				RoleType.GENERAL_USER
			));
		testMemberId = savedMember.getId();

		testCoffee = Coffee.create(
			testMemberId,
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
		List<Coffee> coffees = coffeeRepository.findCoffeesByDate(testMemberId, LocalDate.now());

		// then
		assertThat(coffees).isNotEmpty();
		assertThat(coffees.get(0).getMemberId()).isEqualTo(testMemberId);
		assertThat(coffees.get(0).getRememberDate()).isEqualTo(LocalDate.now());
	}
}
