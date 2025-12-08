fun main() {
    fun getFreshRanges(input: List<String>): List<LongRange> {
        return input.subList(0, input.indexOf("")).map {
            val (from, till) = it.split("-")
            from.toLong()..till.toLong()
        }
    }

    fun part1(input: List<String>): Int {
        val freshRanges = getFreshRanges(input)
        val ingredients = input.subList(input.indexOf("") + 1, input.size).map { it.toLong() }
        val freshCount = ingredients.count { ingredient ->
            freshRanges.any { range ->
                range.contains(ingredient)
            }
        }
        return freshCount
    }

    fun part2(input: List<String>): Long {
        val nonOverlappingRanges = getFreshRanges(input)
            .sortedBy { it.first }
            .fold(mutableListOf<LongRange>()) { acc, currentRange ->
                val lastRange = acc.lastOrNull()
                if (lastRange == null) {
                    acc.add(currentRange)
                } else {
                    if (currentRange.first > lastRange.last) {
                        acc.add(currentRange)
                    } else {
                        acc[acc.lastIndex] = lastRange.first..maxOf(lastRange.last, currentRange.last)
                    }
                }
                acc
            }

        val freshCount = nonOverlappingRanges.sumOf { it.last - it.first + 1 }
        return freshCount
    }

    val testInput = readInput("Day05_test")
    check(part1(testInput) == 3)
    check(part2(testInput) == 14L)

    val input = readInput("Day05")
    part1(input).println()
    part2(input).println()
}
