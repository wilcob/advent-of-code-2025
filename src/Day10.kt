import com.microsoft.z3.Context
import com.microsoft.z3.IntNum
import com.microsoft.z3.Status

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

    fun solveWithZ3(joltages: Array<Int>, schematics: List<List<Int>>): Int {
        val context = Context()
        val optimizer = context.mkOptimize()

        // Define every joltage and schematic indexes as constants:
        val joltageIndexConstants = joltages.indices.map { context.mkIntConst("joltage_$it") }
        val schematicIndexConstants = schematics.indices.map { context.mkIntConst("schematic_$it") }

        // Add constraints to every schematic, saying it must be >= 0:
        schematicIndexConstants.forEach { optimizer.Add(context.mkGe(it, context.mkInt(0))) }

        // Add constraints to every joltage:
        joltageIndexConstants.forEachIndexed { joltageIndex, joltageVar ->
            val target = context.mkInt(joltages[joltageIndex])

            // Add logic to build a list of additions based on the schematics that affect the specific joltage:
            val additions = schematics.mapIndexed { schematicIndex, affectedJoltages ->
                if (affectedJoltages.contains(joltageIndex)) {
                    context.mkAdd(schematicIndexConstants[schematicIndex])
                } else {
                    context.mkInt(0)
                }
            }

            // Add constraint to add the additions to the joltage variable:
            optimizer.Add(context.mkEq(joltageVar, context.mkAdd(*additions.toTypedArray())))

            // Add constraint to make the joltage variable equal the target:
            optimizer.Add(context.mkEq(joltageVar, target))
        }

        // Add logic to determine total presses by adding all values of all schematic variables:
        val totalPresses = context.mkAdd(*schematicIndexConstants.toTypedArray())

        // Tell z3 to minimize the expression to determine total presses:
        optimizer.MkMinimize(totalPresses)

        val presses = if (optimizer.Check() == Status.SATISFIABLE) {
            (optimizer.model.eval(totalPresses, true) as IntNum).int
        } else {
            0
        }

        context.close()
        return presses
    }

    fun part1(input: List<String>): Int {
        val totalMinimalPressesSum = input.sumOf { line ->
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

        return totalMinimalPressesSum
    }

    fun part2(input: List<String>): Int {
        val totalMinimalPressesSum = input.sumOf { line ->
            val (schematicsInput, joltagesInput) = line
                .substringAfter("] ")
                .drop(1)
                .dropLast(1)
                .split(") {")

            val schematics = schematicsInput
                .split(") (")
                .map { numbers -> numbers.split(',').map { it.toInt() } }
            val joltages = joltagesInput.split(',').map { it.toInt() }.toTypedArray()

            solveWithZ3(joltages, schematics)
        }

        return totalMinimalPressesSum
    }

    val testInput = readInput("Day10_test")
    check(part1(testInput) == 7)
    check(part2(testInput) == 33)

    val input = readInput("Day10")
    part1(input).println()
    part2(input).println()
}
