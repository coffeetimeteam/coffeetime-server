package coffeetime.enums

enum class PriceType(
	val value: String
) {
	CHEAP("가성비 있는"),
	AVERAGE("적당한"),
	EXPENSIVE("비싼");

	companion object {
		fun from(name: String): PriceType = entries.find { it.value == name }
			?: throw IllegalArgumentException("No enum constant for display name: $name")
	}
}
