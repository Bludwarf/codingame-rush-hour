package com.codingame

import java.util.*

interface NodeState<SELF : NodeState<SELF>> {
    val isFinal: Boolean
    val nextPossibleStates: Iterable<SELF>
    val minimumCostToFinalState: Int
    override fun equals(other: Any?): Boolean
}

data class Node<State>(val state: State, val depth: Int = 0, val value: Int = 0) : Comparable<Node<*>> {
    override fun compareTo(other: Node<*>): Int {
        return compareValues(value, other.value)
    }
}

data class SearchResult<STATE : NodeState<STATE>>(val graph: DirectedGraph<STATE>, val target: Node<STATE>)

private fun <State> queue(root: Node<State>): Queue<Node<State>> {
    return LinkedList<Node<State>>().let { it.add(root); it }
}

class DirectedGraph<STATE : NodeState<STATE>>(private val root: Node<STATE>) {

    private val parents = mutableMapOf<Node<STATE>, Node<STATE>>()

    private fun add(parentNode: Node<STATE>, node: Node<STATE>) {
        parents[node] = parentNode
    }

    operator fun plusAssign(pair: Pair<Node<STATE>, Node<STATE>>) = add(pair.first, pair.second)

    fun pathTo(target: Node<STATE>): Path<STATE> {
        var current = target
        val nodes = mutableListOf<Node<STATE>>()
        while (current != root) {
            nodes += current
            current = parents[current] ?: throw Throwable("Don't know the parent of $current")
        }
        nodes += root
        return Path(nodes.reversed())
    }
}

@JvmInline
value class Path<STATE : NodeState<STATE>>(val nodes: List<Node<STATE>>) {
    operator fun get(i: Int): Node<STATE> = nodes[i]
}

abstract class SearchAlgorithm<STATE : NodeState<STATE>>(val board: STATE, val maxDepth: Int) {
    abstract fun launch(): SearchResult<STATE>
    fun findPathToFinalState(): Path<STATE> {
        val (graph, target) = launch()
        return graph.pathTo(target)
    }
}

/**
 * Source : https://github.com/marvingfx/rush-hour-solver/blob/670d748cb1b76335e526e0d8772dce8803ffed57/src/algorithm/algorithm.py#L152
 */
class BeamSearchAlgorithm<STATE : NodeState<STATE>>(state: STATE, maxDepth: Int = 1000, private val width: Int = 2) :
    SearchAlgorithm<STATE>(state, maxDepth) {

    override fun launch(): SearchResult<STATE> {
        var depth = 0
        val root = Node(board, depth)
        val graph = DirectedGraph(root)

        if (root.state.isFinal) {
            return SearchResult(graph, root)
        }

        val queue: Queue<Node<STATE>> = queue(root)
        val visitedStates = mutableSetOf<STATE>()

        while (queue.isNotEmpty() && depth < maxDepth) {
            val currentNode = queue.poll()
            depth = currentNode.depth

            val beam = PriorityQueue<Node<STATE>>()

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

class SampleState(private val value: Int) : NodeState<SampleState> {
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

