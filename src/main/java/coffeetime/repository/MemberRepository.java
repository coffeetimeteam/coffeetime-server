package coffeetime.repository;

import coffeetime.domain.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

	Optional<Member> findByUsername(String username);

	Boolean existsByUsername(String username);

	Boolean existsByNickname(String nickname);
}
