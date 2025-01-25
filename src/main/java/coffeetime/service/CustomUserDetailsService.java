package coffeetime.service;

import coffeetime.domain.User;
import coffeetime.infrastructure.CustomUserDetails;
import coffeetime.repository.UserRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<User> findByUsername = userRepository.findByUsername(username);
		if (findByUsername.isEmpty()) {
			throw new UsernameNotFoundException("No user found with username");
		}
		return new CustomUserDetails(findByUsername.get());
	}

	public CustomUserDetails getCurrentUserDetails() {
		return (CustomUserDetails) SecurityContextHolder.getContext()
			.getAuthentication()
			.getPrincipal();
	}
}
