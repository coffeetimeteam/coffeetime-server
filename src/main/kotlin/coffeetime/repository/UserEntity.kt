package coffeetime.repository

import coffeetime.enums.LoginType
import coffeetime.enums.RoleType
import jakarta.persistence.*
import jakarta.validation.constraints.Size

@Entity
@Table(name = "member")
class UserEntity(
    @Column(nullable = false, unique = true)
    @field:Size(min = 5, max = 30)
    val username: String,
    @Enumerated(EnumType.STRING)
    @Column(name = "login_type", nullable = false)
    val loginType: LoginType,
    val nickname: String,
    val password: String,
    @Column(name = "role", nullable = false)
    val role: RoleType
) : BaseEntity()