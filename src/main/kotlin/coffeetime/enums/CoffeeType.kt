package coffeetime.enums

enum class CoffeeType(
	val coffee: String
) {
	COFFEE("coffee"),
	NONE_COFFEE("none-coffee");

	companion object {
		fun from(name: String): CoffeeType = entries.find { it.coffee == name }
			?: throw IllegalArgumentException("No enum constant for display name: $name")
		}
}
