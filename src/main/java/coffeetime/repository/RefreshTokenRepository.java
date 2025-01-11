package coffeetime.repository;

import coffeetime.domain.RefreshToken;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Integer> {

	Optional<RefreshToken> findByToken(String refreshToken);

	@Query("select rt from RefreshToken rt where rt.user.username = ?1")
	List<RefreshToken> findByUsername(String username);

	@Modifying
	@Query("delete from RefreshToken rt where rt.expiredAt <= CURRENT_TIME")
	int deleteByExpiredAt();
}
