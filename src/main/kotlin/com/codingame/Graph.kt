package com.codingame

import java.util.*

data class Node(val state: State, val depth: Int = 0, val value: Int = 0) : Comparable<Node> {

    interface State {
        val isFinal: Boolean
        val nextPossibleStates: Iterable<Pair<DirectedEdge.Action, State>>
        val minimumCostToFinalState: Int
        override fun equals(other: Any?): Boolean
    }

    override fun compareTo(other: Node): Int {
        return compareValues(value, other.value)
    }
}

data class DirectedEdge(val source: Node, val action: Action, val target: Node) {
    interface Action

    override fun toString(): String {
        return "$source -> $target"
    }
}

data class SearchResult(val graph: DirectedGraph, val target: Node)

class DirectedGraph(private val root: Node) {

    private val incomingEdges = mutableMapOf<Node, DirectedEdge>()

    operator fun plusAssign(edge: DirectedEdge) {
        incomingEdges[edge.target] = edge
    }

    fun pathTo(target: Node): Path {
        var current = target
        val edges = mutableListOf<DirectedEdge>()
        do {
            val incomingEdge = incomingEdges[current] ?: throw Throwable("Don't know the incoming edge of $current")
            edges += incomingEdge
            current = incomingEdge.source
        } while (current != root)
        return Path(edges.reversed())
    }
}

@JvmInline
value class Path(private val edges: List<DirectedEdge>) {
    operator fun get(i: Int): DirectedEdge = edges[i]
    fun subPath(i: Int): Path {
        return Path(edges.subList(i, edges.size))
    }
}

abstract class SearchAlgorithm(val state: Node.State, val maxDepth: Int) {

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
class BeamSearchAlgorithm(state: Node.State, maxDepth: Int = 1000, private val width: Int = 2) :
    SearchAlgorithm(state, maxDepth) {

    override fun launch(): SearchResult {
        var depth = 0
        val root = Node(state, depth)
        val graph = DirectedGraph(root)

        if (root.state.isFinal) {
            return SearchResult(graph, root)
        }

        val queue: Queue<Node> = queue(root)
        val visitedStates = mutableSetOf<Node.State>()

        while (queue.isNotEmpty() && depth < maxDepth) {
            val currentNode = queue.poll()
            depth = currentNode.depth

            val beam = PriorityQueue<Node>()

            for ((action, childState) in currentNode.state.nextPossibleStates) {

                if (childState !in visitedStates) {
                    visitedStates += childState
                    val node = Node(
                        state = childState,
                        depth = depth + 1,
                        value = childState.minimumCostToFinalState + depth,
                    )
                    val edge = DirectedEdge(
                        source = currentNode,
                        action = action,
                        target = node
                    )
                    graph += edge
                    beam += node

                    if (childState.isFinal) {
                        return SearchResult(graph, node)
                    }
                }
            }

            queue.addAll(beam.take(width))
        }

        error("NoSolutionFoundException")
    }

}

class SampleState(private val value: Int) : Node.State {
    override val isFinal: Boolean
        get() = value == 10
    override val nextPossibleStates: Iterable<Pair<DirectedEdge.Action, Node.State>>
        get() = listOf(
            SampleAction.DEC to SampleState(value - 1),
            SampleAction.INC to SampleState(value + 1)
        )
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

enum class SampleAction: DirectedEdge.Action {
    INC, DEC
}

fun main() {
    val initialState = SampleState(0)
    val algorithm = BeamSearchAlgorithm(initialState)
    val path = algorithm.findPathToFinalState()
    path[0].action
}

