package coffeetime.support.auth;

import coffeetime.repository.UserEntity;
import java.util.UUID;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

data class CustomUserDetails(
	val user: UserEntity
): UserDetails {

	override fun getAuthorities(): Collection<GrantedAuthority> = listOf(
		SimpleGrantedAuthority(user.role.toString())
	)

	fun getId(): UUID = user.id

	override fun getUsername(): String = user.username

	override fun getPassword(): String = user.password

	override fun isAccountNonExpired(): Boolean {
		return super.isAccountNonExpired()
	}

	override fun isAccountNonLocked(): Boolean {
		return super.isAccountNonLocked()
	}

	override fun isCredentialsNonExpired(): Boolean {
		return super.isCredentialsNonExpired()
	}

	override fun isEnabled(): Boolean {
		return super.isEnabled()
	}
}
