package coffeetime.support.response;

import coffeetime.support.error.EntryPayloadCode
import org.springframework.http.HttpStatus
import java.util.*

data class ApiStatus(
    val timestamp: Date,
    val status: HttpStatus,
    val message: String,
) {
    companion object {
        fun from(entryPayLoadCode: EntryPayloadCode): ApiStatus {
            return ApiStatus(
                Date(),
                entryPayLoadCode.code,
                entryPayLoadCode.message
            )
        }
    }
}
