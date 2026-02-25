package coffeetime.enums

enum class TasteType(
	val taste: String,
) {
	SOUR("신맛나는"),
	NUTTY("고소한"),
	DELICIOUS("맛있는"),
	DONT_LIKE("취향이 아닌");

	companion object {
		fun from(name: String): TasteType = entries.find { it.taste == name }
			?: throw IllegalArgumentException("No enum constant for display name: $name")
	}
}
