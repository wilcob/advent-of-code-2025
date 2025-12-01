fun main() {
    fun part1(input: List<String>): Int {
        var totalDial = 50
        val dialAtZeroCount = input.count { rotation ->
            val direction = rotation[0]
            val amount = rotation.substringAfter(direction).toInt()

            if (direction == 'L') totalDial -= amount else totalDial += amount
            totalDial % 100 == 0
        }

        return dialAtZeroCount
    }

    fun part2(input: List<String>): Int {
        var totalDial = 50
        val dialAtZeroCount = input.sumOf { rotation ->
            val direction = rotation[0]
            val amount = rotation.substringAfter(direction).toInt()
            var circleFitsInRotationCount = amount / 100
            val currentDialWithinCircle = totalDial.mod(100)

            if (direction == 'L') totalDial -= amount else totalDial += amount

            val dialEndsAtZero = totalDial % 100 == 0
            val remainderPassesZero = if (currentDialWithinCircle != 0) {
                var rotationRemainder = amount % 100
                val leftPassesZero = direction == 'L' && currentDialWithinCircle - rotationRemainder < 0
                val rightPassesZero = direction == 'R' && currentDialWithinCircle + rotationRemainder > 99

                leftPassesZero || rightPassesZero
            } else {
                false
            }

            circleFitsInRotationCount + (if (dialEndsAtZero || remainderPassesZero) 1 else 0)
        }

        return dialAtZeroCount
    }

    val testInput = readInput("Day01_test")
    check(part1(testInput) == 3)
    check(part2(testInput) == 6)

    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}
