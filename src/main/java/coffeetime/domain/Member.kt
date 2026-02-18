package coffeetime.domain;

import coffeetime.domain.type.LoginType
import coffeetime.domain.type.RoleType
import java.time.LocalDateTime
import java.util.*

data class Member(
    val id: UUID,
    val username: String,
    val loginType: LoginType,
    val nickname: String,
    val password: String,
    val role: RoleType,
    val createAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val lastLoginDate: LocalDateTime?
) {
    companion object {
        @JvmStatic
        fun createFromClaims(id: UUID, username: String, role: RoleType): Member {
            val now = LocalDateTime.now()
            return Member(
                id = id,
                username = username,
                loginType = LoginType.EMAIL,
                nickname = "",
                password = "",
                role = role,
                createAt = now,
                updatedAt = now,
                lastLoginDate = null
            )
        }
    }
}
