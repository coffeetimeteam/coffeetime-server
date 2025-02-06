package coffeetime.service;

import coffeetime.domain.DefaultNickname;
import coffeetime.domain.Member;
import coffeetime.domain.type.LoginType;
import coffeetime.domain.type.RoleType;
import coffeetime.dto.GlobalResponse;
import coffeetime.dto.MemberCreateRequest;
import coffeetime.exception.CoffeeTimeException;
import coffeetime.exception.EntryPayloadCode;
import coffeetime.infrastructure.CustomUserDetails;
import coffeetime.infrastructure.JwtUtility;
import coffeetime.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@EnableMethodSecurity
public class MemberService {

	private final MemberRepository memberRepository;
	private final PasswordEncoder passwordEncoder;
	private final DefaultNickname defaultNickname;
	private final CustomUserDetailsService customUserDetailsService;
	private final JwtUtility jwtUtility;

	@Transactional(timeout = 10)
	public GlobalResponse createMember(final MemberCreateRequest request) {
		if (memberRepository.existsByUsername(request.getUsername())) {
			throw new CoffeeTimeException(EntryPayloadCode.DUPLICATED_USER);
		}
		if (!request.getPassword().equals(request.getConfirmPassword())) {
			throw new CoffeeTimeException(EntryPayloadCode.INVALID_PASSWORD);
		}

		memberRepository.save(
			Member.create(
				LoginType.EMAIL,
				request.getUsername(),
				defaultNickname.generate(),
				passwordEncoder.encode(request.getPassword()),
				RoleType.GENERAL_USER)
		);
		return new GlobalResponse(EntryPayloadCode.SUCCESS_REQUEST);
	}

	@Transactional(readOnly = true)
	public Member getCurrentMember(final String token) {
		if (token.isEmpty()) {
			throw new CoffeeTimeException(EntryPayloadCode.REQUIRED_TOKEN);
		}
		final CustomUserDetails customUserDetails = customUserDetailsService.getCurrentUserDetails();
		final String username = customUserDetails.getUsername();
		return memberRepository.findByUsername(username)
			.orElseThrow(() -> new CoffeeTimeException(EntryPayloadCode.NOT_FOUND_USER));
	}
}
