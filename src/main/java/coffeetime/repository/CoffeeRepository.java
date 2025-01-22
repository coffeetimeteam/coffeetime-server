package coffeetime.repository;

import coffeetime.domain.Coffee;
import coffeetime.domain.User;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CoffeeRepository extends JpaRepository<Coffee, Long> {

	@Query(""" 
		SELECT coffee FROM Coffee coffee
		WHERE coffee.user = :user
		AND coffee.rememberDate = :date
		ORDER BY coffee.rememberTime ASC
		""")
	List<Coffee> findCoffeesByDate(@Param("user") User user, @Param("date") LocalDate date);

	@Query("""
		SELECT coffee FROM Coffee coffee
		WHERE coffee.user = :user
		AND FUNCTION('YEAR', coffee.rememberDate) = :year
		AND FUNCTION('MONTH', coffee.rememberDate) = :month
		""")
	List<Coffee> findCoffeesByMonth(@Param("user") User user, @Param("year") int year,
		@Param("month") int month);
}
