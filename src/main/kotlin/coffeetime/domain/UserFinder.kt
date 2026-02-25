package coffeetime.domain

import coffeetime.repository.UserRepository
import coffeetime.support.auth.CustomUserDetails
import coffeetime.support.error.CoffeeTimeException
import coffeetime.support.error.EntryPayloadCode
import org.springframework.stereotype.Component

@Component
class UserFinder(
    private val userRepository: UserRepository,
    private val customUserDetailsService: CustomUserDetailsService,
) {

    fun findUserInfo(): User {
        val customUserDetails: CustomUserDetails = customUserDetailsService.getCurrentUserDetails()
        val username = customUserDetails.getUsername()
        val user = userRepository.findByUsername(username).orElseThrow { CoffeeTimeException(EntryPayloadCode.NOT_FOUND_USER) }
        return User(
            user.id,
            user.username,
            user.loginType,
            user.nickname,
            user.password,
            user.role,
            user.createdAt,
            user.updatedAt
        )
    }

    fun existsByUsername(username: String) {
        if (userRepository.existsByUsername(username)) {
            throw CoffeeTimeException(EntryPayloadCode.DUPLICATED_USER)
        }
    }

}