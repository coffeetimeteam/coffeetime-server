package coffeetime.controller.response

import coffeetime.domain.User
import java.util.*

data class CreateUserResponse(
    val id: UUID,
    val username: String
) {

    fun from(user: User): CreateUserResponse {
        return CreateUserResponse(user.id, user.username)
    }
}
