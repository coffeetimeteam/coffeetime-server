package coffeetime.service;

import coffeetime.domain.Member;
import coffeetime.infrastructure.CustomUserDetails;
import coffeetime.repository.MemberRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	MemberRepository memberRepository;

	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<Member> findByUsername = memberRepository.findByUsername(username);
		if (findByUsername.isEmpty()) {
			throw new UsernameNotFoundException("No user found with username");
		}
		return new CustomUserDetails(findByUsername.get());
	}

	@Transactional(readOnly = true)
	public CustomUserDetails getCurrentUserDetails() {
		return (CustomUserDetails) SecurityContextHolder.getContext()
			.getAuthentication()
			.getPrincipal();
	}
}
