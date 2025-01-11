package coffeetime.domain;

public enum LoginType {

	EMAIL,
	SOCIAL;

	public static LoginType from(String loginType) {
		return valueOf(loginType.toUpperCase());
	}
}
