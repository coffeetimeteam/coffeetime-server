package coffeetime.repository

import coffeetime.domain.type.LoginType
import coffeetime.domain.type.RoleType
import jakarta.persistence.*
import jakarta.validation.constraints.Size
import java.time.LocalDateTime

@Entity
@Table(name = "member")
class MemberEntity(
    @Column(nullable = false, unique = true)
    @field:Size(min = 5, max = 30)
    val username: String,
    @Enumerated(EnumType.STRING)
    @Column(name = "login_type", nullable = false)
    val loginType: LoginType,
    val nickname: String,
    val password: String,
    @Column(name = "role", nullable = false)
    val role: RoleType,
    @Column(name = "last_login_date")
    val lastLoginDate: LocalDateTime? = null,
) : BaseEntity() {

    companion object {
        @JvmStatic
        fun create(
            username: String,
            loginType: LoginType,
            nickname: String,
            password: String,
            role: RoleType
        ): MemberEntity {
            return MemberEntity(
                username,
                loginType,
                nickname,
                password,
                role
            )
        }

        @JvmStatic
        fun createFromClaims(username: String, role: RoleType): MemberEntity {
            return MemberEntity(
                username = username,
                loginType = LoginType.EMAIL,
                nickname = "",
                password = "",
                role = role,
                lastLoginDate = LocalDateTime.now()
            )
        }
    }
}
