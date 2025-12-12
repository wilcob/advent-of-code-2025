import kotlin.math.sqrt

fun main() {
    data class Location(val x: Int, val y: Int, val z: Int)

    fun distance(locationA: Location, locationB: Location): Double {
        val dx = (locationB.x - locationA.x).toDouble()
        val dy = (locationB.y - locationA.y).toDouble()
        val dz = (locationB.z - locationA.z).toDouble()
        return sqrt(dx * dx + dy * dy + dz * dz)
    }

    fun getLocations(input: List<String>) = input.map {
        val (x, y, z) = it.split(",").map { it.toInt() }
        Location(x, y, z)
    }

    fun getAllPossibleConnectionSortedByDistance(locations: List<Location>): List<Triple<Location, Location, Double>> {
        val allConnections = mutableListOf<Triple<Location, Location, Double>>()
        for (i in locations.indices) {
            for (j in i + 1 until locations.size) {
                val locationA = locations[i]
                val locationB = locations[j]
                allConnections.add(Triple(locationA, locationB, distance(locationA, locationB)))
            }
        }
        return allConnections.sortedBy { it.third }
    }

    fun part1(input: List<String>, numShortestConnections: Int): Int {
        val locations = getLocations(input)
        val shortestConnections = getAllPossibleConnectionSortedByDistance(locations).take(numShortestConnections)
        val circuitGroups = locations.map { mutableSetOf(it) }.toMutableList()

        for (connection in shortestConnections) {
            val (locationA, locationB, _) = connection
            val groupAIndex = circuitGroups.indexOfFirst { it.contains(locationA) }
            val groupBIndex = circuitGroups.indexOfFirst { it.contains(locationB) }

            // Merge if both locations are in different groups:
            if (groupAIndex != groupBIndex) {
                val groupB = circuitGroups[groupBIndex]
                circuitGroups[groupAIndex].addAll(groupB)
                circuitGroups.removeAt(groupBIndex)
            }
        }

        return circuitGroups
            .sortedBy { it.size }
            .takeLast(3)
            .map { it.size }
            .reduce { acc, value -> acc * value }
    }

    fun part2(input: List<String>): Long {
        val locations = getLocations(input)
        val allConnections = getAllPossibleConnectionSortedByDistance(locations)
        val circuitGroups = locations.map { mutableSetOf(it) }.toMutableList()
        lateinit var lastConnection: Triple<Location, Location, Double>

        for (connection in allConnections) {
            val (locationA, locationB, _) = connection
            val groupAIndex = circuitGroups.indexOfFirst { it.contains(locationA) }
            val groupBIndex = circuitGroups.indexOfFirst { it.contains(locationB) }

            // Merge if both locations are in different groups:
            if (groupAIndex != groupBIndex) {
                val groupB = circuitGroups[groupBIndex]
                circuitGroups[groupAIndex].addAll(groupB)
                circuitGroups.removeAt(groupBIndex)
            }

            lastConnection = connection
            if (circuitGroups.size == 1) break
        }

        val (locationA, locationB, _) = lastConnection
        return locationA.x.toLong() * locationB.x.toLong()
    }


    val testInput = readInput("Day08_test")
    check(part1(testInput, 10) == 40)
    check(part2(testInput) == 25272L)

    val input = readInput("Day08")
    part1(input, 1000).println()
    part2(input).println()
}
