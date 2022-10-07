package com.codingame.rushhour

import com.codingame.BeamSearchAlgorithm
import debug

class ActionResolver(val initialState: State) {

    private val searchAlgorithm = BeamSearchAlgorithm(initialState, width = 5)

    fun resolveNextAction(): Action {
        val path = searchAlgorithm.findPathToFinalState()
        return path[0].action as Action
    }

}
