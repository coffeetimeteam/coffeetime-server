package coffeetime.repository

import coffeetime.domain.type.LoginType
import coffeetime.domain.type.RoleType
import jakarta.persistence.*
import jakarta.validation.constraints.Size
import java.util.*

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