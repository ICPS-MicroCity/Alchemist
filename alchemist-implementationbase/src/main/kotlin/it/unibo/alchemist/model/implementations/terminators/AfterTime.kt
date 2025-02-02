package it.unibo.alchemist.model.implementations.terminators

import it.unibo.alchemist.model.interfaces.Environment
import it.unibo.alchemist.model.interfaces.Time
import java.util.function.Predicate

/**
 * @param endTime the end time.
 */
class AfterTime(val endTime: Time) : Predicate<Environment<*, *>> {

    /**
     * Tries to access the simulation time from the [environment].
     * If the simulation is unaccessible, throws an exception.
     * Otherwise, reads the current time, and flips to true once it got past the provided [endTime].
     */
    override fun test(environment: Environment<*, *>): Boolean =
        environment.simulation?.time?.let { it >= endTime }
            ?: throw IllegalStateException("No simulation available for environment $environment, unable to read time.")
}
