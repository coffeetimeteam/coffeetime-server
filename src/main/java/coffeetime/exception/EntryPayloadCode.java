package coffeetime.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum EntryPayloadCode {
	SUCCESS_REQUEST(HttpStatus.OK, "성공한 요청입니다."),
	SUCCESS_LOGOUT(HttpStatus.OK, "로그아웃에 성공했습니다."),

	SUCCESS_CREATED(HttpStatus.CREATED, "생성에 성공했습니다."),

	DUPLICATED_USER(HttpStatus.CONFLICT, "이미 가입한 회원입니다."),
	DUPLICATED_NICKNAME(HttpStatus.CONFLICT, "이미 존재하는 닉네임입니다."),

	NOT_FOUND_USER(HttpStatus.NOT_FOUND, "존재하지 않는 회원입니다."),
	NOT_FOUND_IMAGE(HttpStatus.NOT_FOUND, "업로드할 이미지를 찾지 못했습니다."),
	NOT_FOUND_IMAG_DELETE(HttpStatus.NOT_FOUND, "삭제할 이미지를 찾지 못했습니다."),
	NOT_FOUND_COFFEE(HttpStatus.NOT_FOUND, "기록을 찾을 수 없습니다."),

	INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다."),
	EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "만료된 토큰입니다."),
	NOT_FOUND_TOKEN(HttpStatus.UNAUTHORIZED, "토큰을 찾을 수 없습니다."),
	REQUIRED_TOKEN(HttpStatus.UNAUTHORIZED, "토큰을 입력해주세요."),
	INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "올바르지 않은 토큰입니다."),
	FAIL_RENEWAL_TOKE(HttpStatus.UNAUTHORIZED, "토큰 갱신을 실패했습니다."),
	FAIL_LOGOUT(HttpStatus.UNAUTHORIZED, "로그아웃에 실패했습니다."),
	FAIL_VALIDATE_TOKE(HttpStatus.UNAUTHORIZED, "토큰 유효성 검사 중 오류가 발생했습니다."),
	NOT_ENOUGH_PERMISSION(HttpStatus.UNAUTHORIZED, "제한된 권한입니다."),
	BAD_CREDENTIAL(HttpStatus.UNAUTHORIZED, "아이디 또는 비밀번호가 올바르지 않습니다."),

	BAD_FORM_DATA(HttpStatus.BAD_REQUEST, "잘못된 형식입니다."),

	FAIL_FEED_MESSAGES(HttpStatus.INTERNAL_SERVER_ERROR, "메세지 생성에 실패했습니다."),
	NOT_FOUND_AUTH(HttpStatus.INTERNAL_SERVER_ERROR, "시큐리티 인증 정보가 없습니다"),
	FAIL_IMAGE_UPLOAD(HttpStatus.INTERNAL_SERVER_ERROR, "이미지 업로드에 실패했습니다."),
	FAIL_SAVE_COFFEE(HttpStatus.INTERNAL_SERVER_ERROR, "커피 기록 저장에 실패했습니다."),
	FAIL_IMAGE_DELETE(HttpStatus.INTERNAL_SERVER_ERROR, "이미지 삭제에 실패했습니다.");

	private final HttpStatus code;
	private final String message;
}
