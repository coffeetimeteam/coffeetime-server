package coffeetime.domain

import coffeetime.enums.LoginType
import coffeetime.enums.RoleType
import coffeetime.repository.UserEntity
import java.util.UUID

data class User(
    val id: UUID,
    val username: String,
    val loginType: LoginType,
    val nickname: String,
    val password: String,
    val role: RoleType
) {

    companion object {

        fun from(userEntity: UserEntity): User {
                return User(
                    userEntity.id,
                    userEntity.username,
                    userEntity.loginType,
                    userEntity.nickname,
                    userEntity.password,
                    userEntity.role,
                )
        }
    }
}
