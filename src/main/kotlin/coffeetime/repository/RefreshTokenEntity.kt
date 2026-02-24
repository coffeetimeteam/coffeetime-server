package coffeetime.repository

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table
import java.util.Date
import java.util.UUID

@Entity
@Table(name = "refresh_token")
class RefreshTokenEntity(
    @Column(columnDefinition = "BINARY(16)", nullable = false)
    val memberId: UUID,
    val token: String,
    val expiredAt: Date,
    val tokenVersion: Int = 0
    ): BaseEntity() {

    companion object {
        fun create(

        )

    }
}