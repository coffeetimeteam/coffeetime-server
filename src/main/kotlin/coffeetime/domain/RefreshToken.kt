package coffeetime.domain

import java.util.Date
import java.util.UUID

data class RefreshToken (
    val id: UUID,
    val userId: UUID,
    val token: String,
    val expiredAt: Date,
    val tokenVersion: Int
)