package coffeetime.repository

import java.util.Optional
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository: JpaRepository<UserEntity, Long> {
	fun findByUsername(username: String): Optional<UserEntity>
	fun existsByUsername(username: String): Boolean
	fun existsByNickname(username: String): Boolean
}
