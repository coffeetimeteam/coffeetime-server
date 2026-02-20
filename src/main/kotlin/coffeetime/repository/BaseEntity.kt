package coffeetime.repository

import coffeetime.domain.type.StatusType
import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime
import java.util.*

@MappedSuperclass
abstract class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "BINARY(16)", nullable = false)
    val id: UUID = UUID.randomUUID()

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR")
    private var status: StatusType = StatusType.ACTIVE

    @CreationTimestamp
    val createdAt: LocalDateTime = LocalDateTime.MIN

    @UpdateTimestamp
    val updatedAt: LocalDateTime = LocalDateTime.MIN

    fun active() {
        status = StatusType.ACTIVE
    }

    fun isActive(): Boolean {
        return status == StatusType.ACTIVE
    }

    fun delete() {
        status = StatusType.REMOVED
    }

    fun isDeleted(): Boolean {
        return status == StatusType.REMOVED
    }
}
