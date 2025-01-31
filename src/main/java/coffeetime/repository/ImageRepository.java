package coffeetime.repository;

import coffeetime.domain.Image;
import coffeetime.domain.User;
import feign.Param;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {

	List<Image> findByCoffee_User(User user);

	@Modifying
	@Query("""
		UPDATE Image image
		SET image.status = 'DELETED'
		WHERE image.coffee.id = :coffeeId
		""")
	void deleteByCoffeeId(@Param("coffeeId") final Long coffeeId);

	@Modifying
	@Query("""
		UPDATE Image image
		SET image.status = 'DELETED'
		WHERE image.coffee.id = :coffeeIds
		""")
	void deleteAllByCoffeeId(@Param("coffeeIds") final List<Long> coffeeIds);
}
