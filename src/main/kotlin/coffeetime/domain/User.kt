package coffeetime.domain

import coffeetime.domain.type.LoginType
import coffeetime.domain.type.RoleType
import java.time.LocalDateTime
import java.util.UUID

data class User(
    val id: UUID,
    val username: String,
    val loginType: LoginType,
    val nickname: String,
    val password: String,
    val role: RoleType,
    val createAt: LocalDateTime,
    val updatedAt: LocalDateTime
)
