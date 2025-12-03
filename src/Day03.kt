fun main() {
    fun solve(input: List<String>, amountOfDigits: Int): Long {
        val totalOutput = input.sumOf { bank ->
            val digits = Array<Int>(amountOfDigits) { -1 }

            bank.forEachIndexed { index, digitChar ->
                val digit = digitChar.digitToInt()
                for (i in 0 until amountOfDigits) {
                    val maxPossibleIndex = bank.length - amountOfDigits + i
                    if (digit > digits[i] && (index <= maxPossibleIndex)) {
                        digits[i] = digit

                        // Remove all the digits after index i, as index i now has a higher digit:
                        for (j in i + 1 until amountOfDigits) {
                            digits[j] = -1
                        }
                        break
                    }
                }
            }
            digits.asList().joinToString("").toLong()
        }

        return totalOutput
    }

    fun part1(input: List<String>) = solve(input, 2)

    fun part2(input: List<String>) = solve(input, 12)

    val testInput = readInput("Day03_test")
    check(part1(testInput) == 357L)
    check(part2(testInput) == 3121910778619)

    val input = readInput("Day03")
    part1(input).println()
    part2(input).println()
}
