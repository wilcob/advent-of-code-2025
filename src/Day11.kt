fun main() {
    fun getInputToOutputs(input: List<String>) = input.associate {
        val (input, outputString) = it.split(": ")
        val outputs = outputString.split(" ")
        input to outputs
    }

    fun part1(input: List<String>): Int {
        val inputToOutputs = getInputToOutputs(input)
        val visitedPaths = mutableSetOf<String>()
        val stack = ArrayDeque<String>()
        stack.add("you")

        val correctPaths = mutableSetOf<String>()
        while (stack.isNotEmpty()) {
            val path = stack.removeLast()
            if (path.endsWith("out")) {
                correctPaths.add(path)
                continue
            }

            val device = path.substringAfterLast(".")
            if (!visitedPaths.contains(path)) {
                visitedPaths.add(path)
                inputToOutputs[device]?.forEach {
                    stack.add("$path.$it")
                }
            }
        }

        return correctPaths.size
    }


    fun part2(input: List<String>): Long {
        val inputToOutputs = getInputToOutputs(input)
        val cache = mutableMapOf<Triple<String, Boolean, Boolean>, Long>()

        fun dfs(inputDevice: String, dac: Boolean = false, fft: Boolean = false): Long {
            val passedDac = dac || inputDevice == "dac"
            val passedFft = fft || inputDevice == "fft"

            val cacheKey = Triple(inputDevice, passedDac, passedFft)
            if (cache.containsKey(cacheKey)) {
                return cache[cacheKey]!!
            }

            if (inputDevice == "out") {
                return if (passedDac && passedFft) 1 else 0
            }

            val pathsTillOutput = inputToOutputs[inputDevice]?.sumOf { device ->
                dfs(device, passedDac, passedFft)
            } ?: 0L

            cache[Triple(inputDevice, passedDac, passedFft)] = pathsTillOutput
            return pathsTillOutput
        }
        
        return dfs("svr")
    }

    val testInput1 = readInput("Day11_test1")
    check(part1(testInput1) == 5)

    val testInput2 = readInput("Day11_test2")
    check(part2(testInput2) == 2L)

    val input = readInput("Day11")
    part1(input).println()
    part2(input).println()
}
