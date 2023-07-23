import java.nio.charset.Charset
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.system.exitProcess

var hadError = false

fun main(args: Array<String>) {
	println("Program arguments: ${args.joinToString()}")
	if (args.size > 1) {
		println("Error")
		exitProcess(64)
	} else if (args.size == 1) {
		runFile(args[0])
	} else {
		runPrompt()
	}
}

fun runFile(fileName: String) {
	val bytes = Files.readAllBytes(Paths.get(fileName))
	runLox(bytes.toString(Charset.defaultCharset()))
	if (hadError) exitProcess(65)
}

fun runPrompt() {
	while (true) {
		print("> ")
		val line = readln()
		if (line == "") break
		runLox(line)
		hadError = false
	}
}

fun runLox(sourceStr: String) {
	val scanner = LoxScanner(sourceStr)
	val tokens = scanner.scanTokens()
	tokens.forEach {
		println(it.toString())
	}
}

fun error(line: Int, msg: String) {
	hadError = true
	System.err.println("[line $line] Error: $msg")
}
