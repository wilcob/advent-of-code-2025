import kotlin.math.abs

fun main() {
    data class Point(val x: Int, val y: Int)

    fun getPoints(input: List<String>) = input.map {
        val (x, y) = it.split(",").map { it.toInt() }
        Point(x, y)
    }

    fun isInsideOrOnTheEdge(point: Point, polygon: List<Point>): Boolean {
        for (i in polygon.indices) {
            val pointA = polygon[i]
            val pointB = polygon[(i + 1) % polygon.size]

            val onVerticalEdge = (pointA.x == pointB.x) &&
                    (point.x == pointA.x) &&
                    (point.y in minOf(pointA.y, pointB.y)..maxOf(pointA.y, pointB.y))

            val onHorizontalEdge = (pointA.y == pointB.y) &&
                    (point.y == pointA.y) &&
                    (point.x in minOf(pointA.x, pointB.x)..maxOf(pointA.x, pointB.x))

            if (onVerticalEdge || onHorizontalEdge) {
                return true
            }
        }

        // Use ray casting to see if point is within the polygon:
        var crossings = 0
        for (i in polygon.indices) {
            val pointA = polygon[i]
            val pointB = polygon[(i + 1) % polygon.size]

            // Horizontal ray cast, so we check if the current vertical line includes the y coordinate of the point:
            if ((pointA.y <= point.y && pointB.y > point.y) || (pointB.y <= point.y && pointA.y > point.y)) {
                // If the line is to the right of the point it crosses the line:
                if (point.x < pointA.x) {
                    crossings++
                }
            }
        }
        // If the amount of crossings is odd, this means the point is inside the polygon:
        return crossings % 2 == 1
    }

    fun isRectangleFullyInPolygon(pointA: Point, pointB: Point, polygon: List<Point>): Boolean {
        val pointC = Point(pointA.x, pointB.y)
        val pointD = Point(pointB.x, pointA.y)

        // Check if any corner is not inside the polygon, if so the rectangle is invalid:
        if (listOf(pointA, pointB, pointC, pointD).any { !isInsideOrOnTheEdge(it, polygon) }) {
            return false
        }

        val rectMinX = minOf(pointA.x, pointB.x)
        val rectMaxX = maxOf(pointA.x, pointB.x)
        val rectMinY = minOf(pointA.y, pointB.y)
        val rectMaxY = maxOf(pointA.y, pointB.y)

        // Check if there is any point of the polygon inside the rectangle, if so rectangle is invalid:
        for (p in polygon) {
            if (p.x > rectMinX && p.x < rectMaxX && p.y > rectMinY && p.y < rectMaxY) {
                return false
            }
        }

        // Check if any vertical or horizontal edges crosses the rectangle's interior, if so the rectangle is invalid:
        for (i in polygon.indices) {
            val edgePointA = polygon[i]
            val edgePointB = polygon[(i + 1) % polygon.size]

            if (edgePointA.x == edgePointB.x) { // Vertical edge
                if (edgePointA.x > rectMinY && edgePointA.x < rectMaxY) {
                    val minEdgePointY = minOf(edgePointA.y, edgePointB.y)
                    val maxEdgePointY = maxOf(edgePointA.y, edgePointB.y)

                    if (minEdgePointY <= rectMinY && maxEdgePointY >= rectMaxY) {
                        return false
                    }
                }
            } else if (edgePointA.y == edgePointB.y) { // Horizontal edge
                if (edgePointA.y > rectMinY && edgePointA.y < rectMaxY) {
                    val minEdgePointX = minOf(edgePointA.x, edgePointB.x)
                    val maxEdgePointX = maxOf(edgePointA.x, edgePointB.x)

                    if (minEdgePointX <= rectMinX && maxEdgePointX >= rectMaxX) {
                        return false
                    }
                }
            }
        }

        return true
    }

    fun solve(input: List<String>, mustBeInPolygon: Boolean): Long {
        val points = getPoints(input)
        var biggestSquareSize = 0L

        for (i in points.indices) {
            loop@ for (j in i + 1 until points.size) {
                val pointA = points[j]
                val pointB = points[i]
                val dx = abs(points[j].x - points[i].x).toLong()
                val dy = abs(points[j].y - points[i].y).toLong()

                if (mustBeInPolygon) {
                    if (!isRectangleFullyInPolygon(pointA, pointB, points)) {
                        continue@loop
                    }
                }

                val squareSize = (dx + 1) * (dy + 1)
                if (squareSize > biggestSquareSize) {
                    biggestSquareSize = squareSize
                }
            }
        }

        return biggestSquareSize
    }

    fun part1(input: List<String>) = solve(input, false)

    fun part2(input: List<String>) = solve(input, true)

    val testInput = readInput("Day09_test")
    check(part1(testInput) == 50L)
    check(part2(testInput) == 24L)

    val input = readInput("Day09")
    part1(input).println()
    part2(input).println()
}
