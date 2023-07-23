class LoxToken(
	private val type: LoxTokenType,
	val lexeme: String,
	private val literal: Any?,
	val line: Int
) {
	override fun toString(): String {
		return "$type $lexeme $literal"
	}
}