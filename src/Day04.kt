fun main() {
    fun List<CharArray>.isPaper(x: Int, y: Int): Boolean {
        if (x !in this.indices || y !in this.indices) return false
        return this[y][x] == '@'
    }

    fun List<CharArray>.getAccessiblePapers(): Set<Pair<Int, Int>> {
        var accessiblePapers = mutableSetOf<Pair<Int, Int>>()

        for (y in 0 until this.size) {
            for (x in 0 until this.size) {
                if (this[y][x] != '@') continue
                val deltaRange = (-1..1)
                val surroundingCount = deltaRange.sumOf { deltaX ->
                    deltaRange.count { deltaY ->
                        if (deltaX == 0 && deltaY == 0) return@count false
                        isPaper(x + deltaX, y + deltaY)
                    }
                }

                if (surroundingCount < 4) accessiblePapers.add(Pair(x, y))
            }
        }

        return accessiblePapers
    }

    fun part1(input: List<String>) = input
        .map { it.toCharArray() }
        .getAccessiblePapers()
        .size

    fun part2(input: List<String>): Int {
        var grid = input.map { it.toCharArray() }
        var totalAccessCount = 0

        while (true) {
            var accessiblePapers = grid.getAccessiblePapers()
            if (accessiblePapers.isEmpty()) break

            totalAccessCount += accessiblePapers.size
            accessiblePapers.forEach { (x, y) -> grid[y][x] = '.' }
        }

        return totalAccessCount
    }

    val testInput = readInput("Day04_test")
    check(part1(testInput) == 13)
    check(part2(testInput) == 43)

    val input = readInput("Day04")
    part1(input).println()
    part2(input).println()
}
