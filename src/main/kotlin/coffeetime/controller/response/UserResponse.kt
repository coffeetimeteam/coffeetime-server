package coffeetime.controller.response

import coffeetime.domain.User
import coffeetime.domain.type.RoleType
import java.util.*

data class UserResponse(
    val id: UUID,
    val username: String,
    val nickname: String,
    val role: RoleType
) {

    companion object {
        fun from(user: User): UserResponse {
            return UserResponse(
                user.id,
                user.username,
                user.nickname,
                user.role
            )
        }
    }
}