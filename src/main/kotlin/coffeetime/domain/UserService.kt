package coffeetime.domain

import coffeetime.controller.request.CreateUserRequest
import coffeetime.domain.type.LoginType
import coffeetime.domain.type.RoleType
import coffeetime.repository.UserEntity
import coffeetime.repository.UserRepository
import coffeetime.support.auth.CustomUserDetails
import coffeetime.support.error.CoffeeTimeException
import coffeetime.support.error.EntryPayloadCode
import lombok.RequiredArgsConstructor
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*


@Service
@RequiredArgsConstructor
@EnableMethodSecurity
class UserService(
	private val passwordEncoder: PasswordEncoder,
	private val defaultNickname: DefaultNickname,
	private val userFinder: UserFinder,
	private val userRepository: UserRepository
) {

	@Transactional(timeout = 10)
	fun create(username: String, password: String, confirmPassword: String): UUID {
		userFinder.existsByUsername(username)
		if (password != confirmPassword) {
			throw CoffeeTimeException(EntryPayloadCode.INVALID_PASSWORD)
		}

		val saved = userRepository.save(
			UserEntity(
			username,
			LoginType.EMAIL,
			defaultNickname.generate(),
			passwordEncoder.encode(password),
			RoleType.GENERAL_USER
		))
		return saved.id
	}

	fun getCurrentUser(): User {
        return userFinder.getUserInfo()
	}
}
