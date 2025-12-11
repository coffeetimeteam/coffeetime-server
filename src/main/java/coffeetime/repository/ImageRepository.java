package coffeetime.repository;

import coffeetime.domain.Coffee;
import coffeetime.domain.Image;
import coffeetime.domain.Member;
import feign.Param;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {

	List<Image> findByCoffee_Member(Member member);

	@Modifying
	void deleteByCoffee(Coffee coffee);

	@Query("DELETE FROM Image image WHERE image.url IN :urls")
	@Modifying
	void deleteByUrls(@Param("urls") List<String> urls);
}