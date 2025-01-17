package coffeetime.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum EntryPayloadCode {
	SUCCESS_REQUEST(HttpStatus.OK, "성공한 요청입니다."),
	SUCCESS_LOGOUT(HttpStatus.OK, "로그아웃에 성공했습니다."),

	DUPLICATED_USER(HttpStatus.CONFLICT, "이미 가입한 회원입니다."),
	DUPLICATED_NICKNAME(HttpStatus.CONFLICT, "이미 존재하는 닉네임입니다."),

	NOT_FOUND_USER(HttpStatus.NOT_FOUND, "존재하지 않는 회원입니다."),

	INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다."),
	EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "만료된 토큰입니다."),
	NOT_FOUND_TOKEN(HttpStatus.UNAUTHORIZED, "토큰을 찾을 수 없습니다."),
	INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "올바르지 않은 토큰입니다."),
	FAIL_RENEWAL_TOKE(HttpStatus.UNAUTHORIZED, "토큰 갱신을 실패했습니다."),
	FAIL_LOGOUT(HttpStatus.UNAUTHORIZED, "로그아웃에 실패했습니다."),
	FAIL_VALIDATE_TOKE(HttpStatus.UNAUTHORIZED, "토큰 유효성 검사 중 오류가 발생했습니다."),
	NOT_ENOUGH_PERMISSION(HttpStatus.UNAUTHORIZED, "제한된 권한입니다."),
	BAD_CREDENTIAL(HttpStatus.UNAUTHORIZED, "아이디 또는 비밀번호가 올바르지 않습니다."),

	NOT_FOUND_AUTH(HttpStatus.INTERNAL_SERVER_ERROR, "시큐리티 인증 정보가 없습니다");

	private final HttpStatus code;
	private final String message;
}
