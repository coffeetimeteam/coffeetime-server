package coffeetime.repository;

import coffeetime.domain.Coffee;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CoffeeRepository extends JpaRepository<Coffee, Long> {

	@Query("""
		SELECT coffee FROM Coffee coffee
		WHERE coffee.memberId = :memberId
		AND DATE(coffee.rememberDate) = :date
		ORDER BY coffee.rememberTime ASC
		""")
	List<Coffee> findCoffeesByDate(@Param("memberId") UUID memberId,
		@Param("date") LocalDate date);

	@Query("""
		SELECT coffee FROM Coffee coffee
		WHERE coffee.memberId = :memberId
		AND EXTRACT(YEAR FROM coffee.rememberDate) = :year
		AND EXTRACT(MONTH FROM coffee.rememberDate) = :month
		ORDER BY coffee.rememberDate, coffee.rememberTime
		""")
	List<Coffee> findCoffeesByMonth(@Param("memberId") UUID member, @Param("year") int year,
		@Param("month") int month);

	Optional<Coffee> findByIdAndMemberId(Long id, UUID memberId);
}
