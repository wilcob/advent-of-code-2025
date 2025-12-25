import java.math.BigInteger
import java.security.MessageDigest
import kotlin.io.path.Path
import kotlin.io.path.readText

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String, trim: Boolean = false): List<String> {
    var text = Path("src/$name.txt").readText()
    if (trim) {
        text.trim()
    }
    return text.lines()
}

/**
 * Reads the text from the given input txt file.
 */
fun readText(name: String): String {
    return Path("src/$name.txt").readText()
}

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

/**
 * The cleaner shorthand for printing output.
 */
fun Any?.println() = println(this)
