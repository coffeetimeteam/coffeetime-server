package coffeetime.enums

enum class LocationType(
	val location: String
) {
	HOME("집"),
	OFFICE("회사"),
	FRANCHISE("프랜차이즈"),
	LOCAL_CAFE("개인카페");

	companion object {
		fun from(name: String): LocationType = entries.find { it.location == name }
			?: throw IllegalArgumentException("No enum constant for display name: $name")
	}
}
