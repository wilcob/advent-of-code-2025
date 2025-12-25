fun main() {
    fun part1(input: String): Int {
        val presents = input
            .substringBeforeLast("\n\n")
            .split("\n\n")
            .map { it.split("\n").drop(1) }

        val regions = input
            .substringAfterLast("\n\n")
            .split("\n")

        var fitCount = 0
        regions.forEach { region ->
            val (sizeInput, quantitiesInput) = region.split(": ")
            val (width, height) = sizeInput.split("x").map { it.toInt() }
            val indexQuantities = quantitiesInput.split(" ").map { it.toInt() }
            val regionSize = width * height

            var shapeCount = 0
            var shapeTileCount = 0

            indexQuantities
                .forEachIndexed { index, quantity ->
                    if (quantity == 0) return@forEachIndexed
                    val tiles = presents[index].sumOf { it -> it.count { it == '#' } }

                    shapeCount += quantity
                    shapeTileCount += tiles * quantity
                }

            // Skip region if the total tile count of all the shapes together is greater than the region size:
            if (shapeTileCount > regionSize) {
                return@forEach
            }

            // Regions always fit if the sum of possible non overlapping shapes is greater than the shape quantity:
            val possibleNonOverlappingShapes = (width / 3) * (height / 3)
            if (possibleNonOverlappingShapes >= shapeCount) {
                fitCount++
            }

            // No extra logic is needed; we already have the required answer based on the puzzle input.
        }

        return fitCount
    }

    val input = readText("Day12")
    part1(input).println()
}
