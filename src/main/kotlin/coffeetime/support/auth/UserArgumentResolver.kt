package coffeetime.support.auth

import coffeetime.domain.User
import org.springframework.core.MethodParameter
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer

class UserArgumentResolver : HandlerMethodArgumentResolver {
    override fun supportsParameter(paramter: MethodParameter): Boolean {
        return paramter.parameterType == User::class.java
    }

    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?
    ): User {
        val auth = SecurityContextHolder.getContext().authentication
        val userDetails = auth.principal as CustomUserDetails
        val user = userDetails.user
        return User(
            user.id,
            user.username,
            user.loginType,
            user.nickname,
            user.password,
            user.role,
            user.createdAt,
            user.updatedAt
        )
    }
}