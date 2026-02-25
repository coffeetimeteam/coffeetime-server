package coffeetime.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.util.UUID

interface RefreshTokenRepository: JpaRepository<RefreshTokenEntity, Long> {

	fun findByToken(refreshToken: String): RefreshTokenEntity

	@Query("SELECT token FROM RefreshTokenEntity token WHERE token.userId = ?1")
	fun findByUserId(useId: Long): List<RefreshTokenEntity>

	@Modifying
	@Query("DELETE FROM RefreshTokenEntity toekn WHERE toekn.expiredAt <= current_timestamp")
	fun deleteByExpiredAt(): Int

	@Query("""
		SELECT token FROM RefreshTokenEntity token
		WHERE token.userId = :userId 
		ORDER BY token.id DESC LIMIT 1
		""",
	)
	fun findLatestRefreshTokenByUserId(memberId: UUID): RefreshTokenEntity
}
