fun main() {
    fun BooleanArray.toBitMask(): Int {
        var bitMask = 0
        for (i in indices) {
            if (this[i]) bitMask = bitMask or (1 shl i)
        }
        return bitMask
    }

    fun bfs(expectedLightsState: Int, schematics: List<List<Int>>): Int {
        val startState = 0
        val queue = ArrayDeque<Pair<Int, Int>>()
        val visitedStates = mutableSetOf<Int>()
        queue.add(startState to 0)

        while(queue.isNotEmpty()) {
            val (currentState, pressCount) = queue.removeFirst()
            if (currentState == expectedLightsState) {
                return pressCount
            }

            for (schematic in schematics) {
                var newState = currentState
                schematic.forEach { index ->
                    // Use XOR to toggle the bit (light on/off) at the given index
                    newState = newState xor (1 shl index)
                }

                // If the new state has not been checked yet, add it to the queue:
                if (!visitedStates.contains(newState)) {
                    visitedStates.add(newState)
                    queue.add(newState to pressCount + 1)
                }
            }

        }
        return -1
    }

    fun part1(input: List<String>): Int {
        val totalMinimalPressesCount = input.sumOf { line ->
            val (lightsInput, schematicsInput) = line
                .drop(1)
                .substringBefore(" {")
                .split("] ")

            val expectedLights = lightsInput.map { it == '#' }.toBooleanArray()
            val schematics = schematicsInput
                .drop(1)
                .dropLast(1)
                .split(") (")
                .map { numbers -> numbers.split(',').map { it.toInt() } }

            bfs(expectedLights.toBitMask(), schematics)
        }

        return totalMinimalPressesCount
    }

    fun part2(input: List<String>): Int {
        return 0
    }

    val testInput = readInput("Day10_test")
    check(part1(testInput) == 7)
//    check(part2(testInput) == 33)

    val input = readInput("Day10")
    part1(input).println()
//    part2(input).println()
}
