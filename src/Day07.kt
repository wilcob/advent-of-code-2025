fun main() {
    fun part1(input: List<String>): Int {
        var locations = mutableSetOf(input[0].length / 2 to 0)
        var splitCount = 0

        while (locations.first().second < input.size - 1) {
            locations = locations.flatMap { location ->
                val newX = location.first
                val newY = location.second + 1

                val newLocations = mutableSetOf<Pair<Int, Int>>()
                if (input[newY][newX] == '^') {
                    newLocations.add(newX - 1 to newY)
                    newLocations.add(newX + 1 to newY)
                    splitCount++
                } else {
                    newLocations.add(newX to newY)
                }

                newLocations
            }.toMutableSet()
        }

        return splitCount
    }

    fun part2(input: List<String>): Long {
        val grid = input.map { it.toCharArray() }.toTypedArray()
        val pathCache = Array(grid.size) { LongArray(grid[0].size) { -1L } }

        fun dfs(x: Int, y: Int): Long {
            if (y !in grid.indices || x !in grid[0].indices || grid[y][x] != '.' && grid[y][x] != 'S') return 0L

            // The path has reached the end, return 1:
            if (y == grid.size - 1) {
                return 1L
            }

            // If the position already exists in the path cache, used cached value (number of paths from this location):
            if (pathCache[y][x] != -1L) {
                return pathCache[y][x]
            }

            val pathCount = if (grid[y + 1][x] == '^') {
                // Split down left and right:
                dfs(x - 1, y + 1) + dfs(x + 1, y + 1)
            } else {
                // Go straight down:
                dfs(x, y + 1)
            }

            pathCache[y][x] = pathCount
            return pathCount
        }

        return dfs(grid[0].size / 2, 0)
    }

    val testInput = readInput("Day07_test")
    check(part1(testInput) == 21)
    check(part2(testInput) == 40L)

    val input = readInput("Day07")
    part1(input).println()
    part2(input).println()
}
