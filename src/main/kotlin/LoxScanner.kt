import LoxTokenType.*
import java.util.HashMap

val digits = '0'..'9'

class LoxScanner(private val source: String) {
	private var keywords: MutableMap<String, LoxTokenType> = HashMap()

	init {
		keywords["and"] = AND
		keywords["class"] = CLASS
		keywords["else"] = ELSE
		keywords["false"] = FALSE
		keywords["for"] = FOR
		keywords["fun"] = FUN
		keywords["if"] = IF
		keywords["nil"] = NIL
		keywords["or"] = OR
		keywords["print"] = PRINT
		keywords["return"] = RETURN
		keywords["super"] = SUPER
		keywords["this"] = THIS
		keywords["true"] = TRUE
		keywords["var"] = VAR
		keywords["while"] = WHILE
	}

	private val tokens: MutableList<LoxToken> = ArrayList()
	private var current = 0
	private var start = 0
	private var line = 1

	private fun isAtEnd(): Boolean {
		return source.length <= current
	}

	private fun isAtEnd(position: Int): Boolean {
		return source.length <= position
	}

	fun scanTokens(): List<LoxToken> {
		while (!isAtEnd()) {
			start = current
			scanToken()
		}

		tokens.add(LoxToken(EOF, "", null, line))
		return tokens
	}

	private fun scanToken() {
		when (val c = advance()) {
			'(' -> addToken(LEFT_PAREN)
			')' -> addToken(RIGHT_PAREN)
			'{' -> addToken(LEFT_BRACE)
			'}' -> addToken(RIGHT_BRACE)
			',' -> addToken(COMMA)
			'.' -> addToken(DOT)
			'-' -> addToken(MINUS)
			'+' -> addToken(PLUS)
			';' -> addToken(SEMICOLON)
			'*' -> addToken(STAR)
			'!' -> addToken(if (match('=')) BANG_EQUAL else BANG)
			'=' -> addToken(if (match('=')) EQUAL_EQUAL else EQUAL)
			'<' -> addToken(if (match('=')) LESS_EQUAL else LESS)
			'>' -> addToken(if (match('=')) GREATER_EQUAL else GREATER)
			'/' -> {
				if (match('/')) {
					while (peek() != '\n' && !isAtEnd()) advance()
				} else if (match('*')) {
					while (!(peek() == '*' && peekNext() == '/') && !isAtEnd()) advance()
					if (!isAtEnd()) {
						advance()
						advance()
					}
				} else {
					addToken(SLASH)
				}
			}

			' ', '\r', '\t' -> {}
			'\n' -> line++
			'"' -> string()
			in digits -> number()

			else -> {
				when {
					isDigit(c) -> number()
					isAlpha(c) -> identifier()
					else -> error(line, "Unexpected character.")
				}
			}
		}
	}

	private fun peek(): Char {
		if (isAtEnd()) return Char.MIN_VALUE
		return source[current]
	}

	private fun peekNext(): Char {
		if (isAtEnd(current + 1)) return Char.MIN_VALUE
		return source[current + 1]
	}

	private fun advance(): Char {
		return source[current++]
	}

	private fun addToken(type: LoxTokenType) {
		addToken(type, null)
	}

	private fun addToken(type: LoxTokenType, literal: Any?) {
		val text = source.substring(start, current)
		tokens.add(LoxToken(type, text, literal, line))
	}

	private fun match(expected: Char): Boolean {
		if (isAtEnd()) return false
		if (source[current] != expected) return false
		current += 1
		return true
	}

	private fun string() {
		while (peek() != '"' && !isAtEnd()) {
			if (peek() == '\n') line++
			advance()
		}
		if (isAtEnd()) {
			error(line, "Unterminated string.")
			return
		}

		advance()

		val value = source.substring(start + 1, current - 1)
		addToken(STRING, value)
	}

	private fun number() {
		while (peek() in digits) advance()
		if (peek() == '.' && peekNext() in digits) {
			// consume the '.'
			advance()
			while (peek() in digits) advance()
		}

		addToken(NUMBER, (source.substring(start, current)).toDouble())
	}

	private fun isDigit(c: Char): Boolean {
		return c in digits
	}

	private fun isAlpha(c: Char): Boolean {
		return c in 'a'..'z' || c in 'A'..'Z' || c == '_'
	}

	private fun isAlphaNumeric(c: Char): Boolean {
		return isAlpha(c) || isDigit(c)
	}

	private fun identifier() {
		while (isAlphaNumeric(peek())) advance()
		val text = source.substring(start, current)
		var type = keywords[text]
		if (type == null) type = IDENTIFIER
		addToken(type)
	}
}
