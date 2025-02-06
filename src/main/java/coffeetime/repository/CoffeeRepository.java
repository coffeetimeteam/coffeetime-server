package coffeetime.repository;

import coffeetime.domain.Coffee;
import coffeetime.domain.Member;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CoffeeRepository extends JpaRepository<Coffee, Long> {

	@Query("""
		SELECT coffee FROM Coffee coffee
		WHERE coffee.member = :member
		AND DATE(coffee.rememberDate) = :date
		ORDER BY coffee.rememberTime ASC
		""")
	List<Coffee> findCoffeesByDate(@Param("member") Member member, @Param("date") LocalDate date);

	@Query("""
		SELECT coffee FROM Coffee coffee
		WHERE coffee.member = :member
		AND EXTRACT(YEAR FROM coffee.rememberDate) = :year
		AND EXTRACT(MONTH FROM coffee.rememberDate) = :month
		ORDER BY coffee.rememberDate, coffee.rememberTime
		""")
	List<Coffee> findCoffeesByMonth(@Param("member") Member member, @Param("year") int year,
		@Param("month") int month);

	Optional<Coffee> findByIdAndMember(Long id, Member member);
}
