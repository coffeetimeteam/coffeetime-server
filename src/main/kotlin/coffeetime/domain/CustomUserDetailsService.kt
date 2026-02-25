package coffeetime.domain

import coffeetime.repository.UserEntity
import coffeetime.repository.UserRepository
import coffeetime.support.auth.CustomUserDetails
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CustomUserDetailsService (
	private val userRepository: UserRepository,
): UserDetailsService {

	@Transactional(readOnly = true)
	override fun loadUserByUsername(username: String): UserDetails {
		val user: UserEntity = userRepository.findByUsername(username)
			.orElseThrow { throw UsernameNotFoundException("No user found with username") }
		return CustomUserDetails(user)
	}

	@Transactional(readOnly = true)
	fun getCurrentUserDetails(): CustomUserDetails {
        return SecurityContextHolder.getContext().authentication.principal as CustomUserDetails
    }
}
