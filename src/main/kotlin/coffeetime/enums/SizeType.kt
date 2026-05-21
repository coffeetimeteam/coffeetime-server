package coffeetime.enums

enum class SizeType(
	val size: String
) {
	LARGE("큰"),
	MEDIUM("중간"),
	SMALL("작은");

	fun fromDisplayName(name: String): SizeType = entries.find { it.size == name }
		?: throw IllegalArgumentException("No enum constant for display name: $name")
}
