package coffeetime.domain;

import coffeetime.controller.request.MemberCreateRequest;
import coffeetime.controller.response.GlobalResponse;
import coffeetime.domain.type.LoginType;
import coffeetime.domain.type.RoleType;
import coffeetime.repository.MemberEntity;
import coffeetime.repository.MemberRepository;
import coffeetime.support.auth.CustomUserDetails;
import coffeetime.support.auth.JwtUtility;
import coffeetime.support.error.CoffeeTimeException;
import coffeetime.support.error.EntryPayloadCode;
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

		MemberEntity member = MemberEntity.create(
			request.getUsername(),
			LoginType.EMAIL,
			defaultNickname.generate(),
			passwordEncoder.encode(request.getPassword()),
			RoleType.GENERAL_USER
		);
		memberRepository.save(member);
		return new GlobalResponse(EntryPayloadCode.SUCCESS_REQUEST);
	}

	@Transactional(readOnly = true)
	public Member getCurrentMember(final String token) {
		if (token.isEmpty()) {
			throw new CoffeeTimeException(EntryPayloadCode.REQUIRED_TOKEN);
		}
		final CustomUserDetails customUserDetails = customUserDetailsService.getCurrentUserDetails();
		final String username = customUserDetails.getUsername();
		final MemberEntity member = memberRepository.findByUsername(username)
			.orElseThrow(() -> new CoffeeTimeException(EntryPayloadCode.NOT_FOUND_USER));

		return new Member(member.getId(), member.getUsername(), member.getLoginType(),
			member.getNickname(), member.getPassword(), member.getRole(), member.getCreatedAt(),
			member.getUpdatedAt(), member.getLastLoginDate());
	}
}
