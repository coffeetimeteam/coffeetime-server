package coffeetime.repository;

import coffeetime.domain.Member;
import coffeetime.domain.RefreshToken;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Integer> {

	Optional<RefreshToken> findByToken(String refreshToken);

	@Query("select rt from RefreshToken rt where rt.member.username = ?1")
	List<RefreshToken> findByUsername(String username);

	@Modifying
	@Query("delete from RefreshToken rt where rt.expiredAt <= current_timestamp")
	int deleteByExpiredAt();

	@Query(value = "select rt from RefreshToken rt where rt.member = :member order by rt.id desc "
		+ "limit 1")
	Optional<RefreshToken> findLatestByMember(@Param("member") Member member);
}
