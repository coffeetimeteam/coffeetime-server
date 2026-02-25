package coffeetime.repository

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table
import java.util.*

@Entity
@Table(name = "refresh_token")
class RefreshTokenEntity(
    @Column(columnDefinition = "BINARY(16)", nullable = false)
    val userId: UUID,
    token: String,
    expiredAt: Date,
    tokenVersion: Int = 0
) : BaseEntity() {

    @Column(unique = true, nullable = false, length = 256)
    var token: String = token
        private set

    @Column(nullable = false)
    var expiredAt: Date = expiredAt
        private set

    @Column(nullable = false)
    var tokenVersion: Int = tokenVersion
        private set

    fun incrementVersion(): Int {
        tokenVersion++
        return tokenVersion
    }

    fun update(token: String, expiredAt: Date) {
        this.token = token
        this.expiredAt = expiredAt
    }
}