package coffeetime.service;

import coffeetime.domain.DefaultNickname;
import coffeetime.domain.LoginType;
import coffeetime.domain.Role;
import coffeetime.domain.User;
import coffeetime.dto.APIResponse;
import coffeetime.dto.NicknameUpdateRequest;
import coffeetime.dto.PasswordUpdateRequest;
import coffeetime.dto.UserCreateRequest;
import coffeetime.dto.UserResponse;
import coffeetime.exception.CoffeeTimeException;
import coffeetime.exception.EntryPayloadCode;
import coffeetime.infrastructure.CustomUserDetails;
import coffeetime.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@EnableMethodSecurity
public class UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final DefaultNickname defaultNickname;

	public APIResponse createUser(final UserCreateRequest request) {
		if (userRepository.existsByUsername(request.getUsername())) {
			throw new CoffeeTimeException(EntryPayloadCode.DUPLICATED_USER);
		}

		if (!request.getPassword().equals(request.getConfirmPassword())) {
			throw new CoffeeTimeException(EntryPayloadCode.INVALID_PASSWORD);
		}

		userRepository.save(
			new User(
				LoginType.EMAIL,
				request.getUsername(),
				defaultNickname.generate(),
				passwordEncoder.encode(request.getPassword()),
				Role.GENERAL_USER
			)
		);

		return new APIResponse(EntryPayloadCode.SUCCESS_REQUEST);
	}

	public UserResponse getUserInfo() {
		final User currentUser = getCurrentUser();
		if (currentUser == null) {
			throw new CoffeeTimeException(EntryPayloadCode.NOT_FOUND_USER);
		}
		return new UserResponse(currentUser.getId(), currentUser.getUsername(),
			currentUser.getNickname(), currentUser.getRole());
	}

	public void updateNickname(final NicknameUpdateRequest request) {
		final User currentUser = getCurrentUser();
		if (userRepository.existsByNickname(request.nickname())) {
			throw new CoffeeTimeException(EntryPayloadCode.DUPLICATED_NICKNAME);
		}
		final User updatedUser = new User(
			currentUser.getLoginType(),
			currentUser.getId(),
			currentUser.getUsername(),
			request.nickname(),
			currentUser.getRole()
		);
		userRepository.save(updatedUser);
	}

	public void updatePassword(final PasswordUpdateRequest request) {
		final User currentUser = getCurrentUser();
		if (!passwordEncoder.matches(currentUser.getPassword(),
			request.currentPassword())) {
			throw new CoffeeTimeException(EntryPayloadCode.INVALID_PASSWORD);
		}
		final User updateUser = new User(
			currentUser.getUsername(),
			passwordEncoder.encode(request.newPassword())
		);
		userRepository.save(updateUser);
	}

	private User getCurrentUser() {
		CustomUserDetails userDetails = getCurrentUserDetails();
		final String username = userDetails.getUsername();
		return userRepository.findByUsername(username)
			.orElseThrow(
				() -> new CoffeeTimeException(EntryPayloadCode.NOT_FOUND_USER));
	}

	private CustomUserDetails getCurrentUserDetails() {
		return (CustomUserDetails) SecurityContextHolder.getContext()
			.getAuthentication()
			.getPrincipal();
	}


	private void checkDuplicateNickname(final String nickname) {
		if (userRepository.existsByUsername(nickname)) {
			throw new CoffeeTimeException(EntryPayloadCode.DUPLICATED_NICKNAME);
		}
	}

}
