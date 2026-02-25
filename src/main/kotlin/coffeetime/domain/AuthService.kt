package coffeetime.domain

import coffeetime.controller.response.TokensResponse
import coffeetime.support.auth.CustomUserDetails
import coffeetime.support.error.CoffeeTimeException
import coffeetime.support.error.EntryPayloadCode
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AuthService(
    private val authenticationManager: AuthenticationManager,
    private val tokenService: TokenService
) {

    @Transactional
    fun login(
        username: String,
        password: String
    ): ResponseEntity<TokensResponse> {
        val authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(
                username,
                password
            )
        ) ?: throw CoffeeTimeException(EntryPayloadCode.BAD_CREDENTIAL)
        val userDetails = authentication.principal as CustomUserDetails
        val token = tokenService.create(User.from(userDetails.user))
        return ResponseEntity.ok().body(TokensResponse(token.access, token.refresh));
    }
}
