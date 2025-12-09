fun main() {
    fun solve(input: List<String>, cephalopodMath: Boolean): Long {
        val maxLength = input.maxOf { it.length }
        val splitIndexes = mutableSetOf<Int>(0)
        for (i in 0 until maxLength) {
            if (input.all { row -> row.getOrNull(i)?.isWhitespace() == true }) {
                splitIndexes += i + 1
            }
        }
        splitIndexes += maxLength + 1

        val problemRanges = splitIndexes.windowed(2) { (from, till) -> from until till - 1 }
        val numberBlocks = problemRanges.map { range -> input.dropLast(1).map { line -> line.substring(range) } }
        val symbols = input.last().split("\\s{1,4}".toRegex())

        var grandTotal = 0L
        numberBlocks.forEachIndexed { problemIndex, block ->
            var numbers = if (!cephalopodMath) {
                block.map { it.trim().toInt() }
            } else {
                val reversedBlock = block.map { it.reversed() }
                (0 until reversedBlock[0].length).mapNotNull { columnIndex ->
                    val numberString = buildString {
                        reversedBlock.forEach { row ->
                            row.getOrNull(columnIndex)?.takeIf { !it.isWhitespace() }?.let(::append)
                        }
                    }
                    numberString.toIntOrNull()
                }
            }

            val symbol = symbols[problemIndex]
            if (symbol == "*") {
                grandTotal += numbers.fold(1L) { acc, number -> acc * number }
            } else if (symbol == "+") {
                grandTotal += numbers.sum()
            }
        }
        return grandTotal
    }

    fun part1(input: List<String>) = solve(input, false)

    fun part2(input: List<String>) = solve(input, true)

    val testInput = readInput("Day06_test", trim = false)
    check(part1(testInput) == 4277556L)
    check(part2(testInput) == 3263827L)

    val input = readInput("Day06")
    part1(input).println()
    part2(input).println()
}
