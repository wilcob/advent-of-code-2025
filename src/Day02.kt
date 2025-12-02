fun main() {
    fun part1(input: List<String>): Long {
        val ranges = input[0].split(",").map {
            val (from, until) = it.split("-").map { it.toLong() }
            from..until
        }

        var invalidIdSum = 0L
        ranges.forEach { range ->
            range.forEach { number ->
                val numberString = number.toString().takeIf { it.length % 2 == 0 } ?: return@forEach
                val half = numberString.substring(0, numberString.length / 2)
                val sameSequence = numberString.split(half).size - 1 > 1
                if (sameSequence) invalidIdSum += number
            }
        }

        return invalidIdSum
    }

    fun part2(input: List<String>): Long {
        val ranges = input[0].split(",").map {
            val (from, until) = it.split("-").map { it.toLong() }
            from..until
        }

        var invalidIdSum = 0L
        ranges.forEach { range ->
            range.forEach { number ->
                val numberString = number.toString()
                var sequence = ""

                for (character in numberString) {
                    sequence += character

                    // Check if the built sequence length is even:
                    if (numberString.length % sequence.length != 0) continue

                    // Check how many times the built sequence fits in the number string (it can't be 1):
                    val sequenceSizeFitCount = (numberString.length / sequence.length).takeIf { it != 1 } ?: continue

                    // Check if the built sequence occurs the same amount of times as it fits in the number string:
                    if (numberString.split(sequence).size - 1 == sequenceSizeFitCount) {
                        invalidIdSum += number
                        break
                    }
                }
            }
        }

        return invalidIdSum
    }

    val testInput = readInput("Day02_test")
    check(part1(testInput) == 1227775554L)
    check(part2(testInput) == 4174379265L)

    val input = readInput("Day02")
    part1(input).println()
    part2(input).println()
}
