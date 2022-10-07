package com.codingame

import java.util.*

interface State {
    val isFinal: Boolean
    val nextPossibleStates: Iterable<State>
    val minimumCostToFinalState: Int
    override fun equals(other: Any?): Boolean
}

data class Node(val state: State, val depth: Int = 0, val value: Int = 0) : Comparable<Node> {
    override fun compareTo(other: Node): Int {
        return compareValues(value, other.value)
    }
}

data class SearchResult(val graph: DirectedGraph, val target: Node)

class DirectedGraph(private val root: Node) {

    private val parents = mutableMapOf<Node, Node>()

    private fun add(parentNode: Node, node: Node) {
        parents[node] = parentNode
    }

    operator fun plusAssign(pair: Pair<Node, Node>) = add(pair.first, pair.second)

    fun pathTo(target: Node): Path {
        var current = target
        val nodes = mutableListOf<Node>()
        while (current != root) {
            nodes += current
            current = parents[current] ?: throw Throwable("Don't know the parent of $current")
        }
        nodes += root
        return Path(nodes.reversed())
    }
}

@JvmInline
value class Path(private val nodes: List<Node>) {
    operator fun get(i: Int): Node = nodes[i]
}

abstract class SearchAlgorithm(val state: State, val maxDepth: Int) {

    abstract fun launch(): SearchResult
    fun findPathToFinalState(): Path {
        val (graph, target) = launch()
        return graph.pathTo(target)
    }

    companion object {
        fun queue(root: Node): Queue<Node> {
            return LinkedList<Node>().let { it.add(root); it }
        }
    }
}

/**
 * Source : https://github.com/marvingfx/rush-hour-solver/blob/670d748cb1b76335e526e0d8772dce8803ffed57/src/algorithm/algorithm.py#L152
 */
class BeamSearchAlgorithm(state: State, maxDepth: Int = 1000, private val width: Int = 2) :
    SearchAlgorithm(state, maxDepth) {

    override fun launch(): SearchResult {
        var depth = 0
        val root = Node(state, depth)
        val graph = DirectedGraph(root)

        if (root.state.isFinal) {
            return SearchResult(graph, root)
        }

        val queue: Queue<Node> = queue(root)
        val visitedStates = mutableSetOf<State>()

        while (queue.isNotEmpty() && depth < maxDepth) {
            val currentNode = queue.poll()
            depth = currentNode.depth

            val beam = PriorityQueue<Node>()

            for (childBoard in currentNode.state.nextPossibleStates) {

                if (childBoard !in visitedStates) {
                    visitedStates += childBoard
                    val node = Node(
                        state = childBoard,
                        depth = depth + 1,
                        value = childBoard.minimumCostToFinalState + depth,
                    )
                    graph += currentNode to node
                    beam += node

                    if (childBoard.isFinal) {
                        return SearchResult(graph, node)
                    }
                }
            }

            queue.addAll(beam.take(width))
        }

        error("NoSolutionFoundException")
    }

}

class SampleState(private val value: Int) : State {
    override val isFinal: Boolean
        get() = value == 10
    override val nextPossibleStates: Iterable<SampleState>
        get() = listOf(SampleState(value - 1), SampleState(value + 1))
    override val minimumCostToFinalState: Int
        get() = 0

    override fun toString(): String {
        return value.toString()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is SampleState) return false

        if (value != other.value) return false

        return true
    }

    override fun hashCode(): Int {
        return value
    }


}

fun main() {
    val initialState = SampleState(0)
    val algorithm = BeamSearchAlgorithm(initialState)
    val path = algorithm.findPathToFinalState()
}

