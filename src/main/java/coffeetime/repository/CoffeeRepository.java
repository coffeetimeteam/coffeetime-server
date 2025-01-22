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
        SELECT c FROM Coffee c 
        WHERE c.user = :user 
        AND c.rememberDate = :date 
        ORDER BY c.rememberTime ASC
        """)
    List<Coffee> findDailyCoffees(@Param("user") User user, @Param("date") LocalDate date);
}
